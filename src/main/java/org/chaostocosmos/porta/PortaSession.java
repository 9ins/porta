package org.chaostocosmos.porta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.chaostocosmos.porta.properties.SessionMappingConfigs;

/**
 * 
 * Porta session object
 *
 * @author 9ins 2020. 11. 18.
 */
public class PortaSession implements Runnable, PortaThreadPoolExceptionHandler {

	boolean isDone = false;
	String sessionName;
	Thread thread;
	SessionMappingConfigs sessionMapping;
	Logger logger;
	Map<String, ChannelWorker> sendChannelMap; 
	Map<String, ChannelWorker> receiveChannelMap;
	ServerSocket proxyServer;
	PortaThreadPool threadPool;
	boolean standAloneFailed = false;
	boolean masterSessionFailed = false;
	boolean slaveSessionFailed = false;
	Map<Integer, Boolean> loadBalancedStatus;
	
	long sessionStartMillis, 
	sessionCurrentMillis;

	long SESSION_SEND_SIZE_TOTAL, 
	SESSION_RECEIVE_SIZE_TOTAL, 
	SESSION_TOTAL_SUCCESS, 
	SESSION_TOTAL_FAIL,
	SESSION_SEND_SUCCESS_COUNT,
	SESSION_RECEIVE_SUCCESS_COUNT,
	SESSION_SEND_FAIL_COUNT,
	SESSION_RECEIVE_FAIL_COUNT;	

	/**
	 * Constructor
	 * 
	 * @param proxy
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public PortaSession(String sessionName, SessionMappingConfigs sm, PortaThreadPool proxyThreadPool) {
		this.logger = Logger.getInstance();
		this.sessionName = sessionName;
		this.sessionMapping = sm;
		this.threadPool = proxyThreadPool;
		this.sendChannelMap = new HashMap<>();
		this.receiveChannelMap = new HashMap<>();
		this.loadBalancedStatus = IntStream.range(0, this.sessionMapping.getRemoteHosts().size()).boxed()
				.map(i -> new AbstractMap.SimpleEntry<Integer, Boolean>(i, true))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		this.threadPool.addExceptionHandler(this);						
		//this.logger.setInfo(false);
	}

	/**
	 * Start
	 */
	public void start() {
		this.thread = new Thread(this);
		this.thread.start();
	}

	/**
	 * Close this session
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void closeSession() throws IOException, InterruptedException {
		closeAllChannels();
		if(this.proxyServer != null) {
			this.proxyServer.close();
		}
	}

	/**
	 * Close
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void closeAllChannels() throws IOException, InterruptedException {
		for (int i = 0; i < sendChannelMap.size(); i++) {
			sendChannelMap.get(i).close();
			receiveChannelMap.get(i).close();
		}
	}

	/**
	 * Close Socket
	 * 
	 * @param socket
	 * @param msg
	 * @throws IOException
	 */
	public void closeSocket(Socket socket, String msg) throws IOException {
		if (socket != null) {
			OutputStream os = socket.getOutputStream();
			os.write(msg.getBytes());
			os.flush();
			os.close();
			socket.close();
		}
	}

	/**
	 * Get session
	 * 
	 * @return
	 */
	public String getSessionName() {
		return this.sessionName;
	}

	/**
	 * Set session
	 * 
	 * @param sessionName
	 */
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}

	@Override
	public void handleException(Thread worker, Throwable t) {
		t.printStackTrace();
	}

	@Override
	public void run() {
		try {
			logger.info("[" + sessionName + "] [Session type: " + sessionMapping.getSessionModeEnum().name()+ "] Proxy server waiting [Port] : " + sessionMapping.getPort() + "   Target: "	+ sessionMapping.getRemoteHosts().toString());
			this.proxyServer = new ServerSocket(this.sessionMapping.getPort(), 100,	InetAddress.getByName(sessionMapping.getBindAddress()));
		} catch (Exception e) {
			logger.throwable(e);
			return;
		}
		while (!this.isDone) {
			try {
				this.sessionStartMillis = System.currentTimeMillis();
				Socket client = proxyServer.accept();
				if (sessionMapping.getRemoteHosts().stream().anyMatch(h -> sessionMapping.isForbiddenHost(h))) {
					String err = "Forbidden remote host request detected. Forbidden List: "	+ sessionMapping.getRemoteHosts().toString();
					logger.info(err);
					closeSocket(client, err);
				} else {
					if (sessionMapping.getAllowedHosts().size() == 0 || sessionMapping.getAllowedHosts().stream().anyMatch(h -> h.equals(client.getLocalAddress().getHostAddress()))) {
						logger.info("[" + sessionName + "][SESSION MODE: " + this.sessionMapping.getSessionModeEnum() + "] Client channel connected. " + client.getRemoteSocketAddress().toString());
						ChannelWorker worker = new ChannelWorker(sessionName, client, sessionMapping);
						this.threadPool.execute(worker);
						logger.info("[" + sessionName + "] New Channel Worker started. Session: "+worker.getSessionName()+"   Worker: "+worker.getName()+"  "+worker.getId());
					} else {
						String err = "Not allowed host connected: " + client.getRemoteSocketAddress().toString();
						logger.info(err);
						closeSocket(client, err);
					}
				}
			} catch (Exception e) {
				logger.throwable(e);
			}
		}
	}

	/**
	 * Get remote host index by ratio list
	 * 
	 * @param ratioList
	 * @return
	 */
	public int getRandomRatioIndex(List<Float> ratioList) {
		Random random = new Random();
		int sum = ratioList.stream().reduce((a, b) -> Float.sum(a, b)).get().intValue();
		int start = 0;
		int bound = 0;
		int ran = random.nextInt(sum);
		for (int i = 0; i < ratioList.size(); i++) {
			int ratio = ratioList.get(i).intValue();
			bound += ratio;
			if (ran >= start && ran < bound) {
				// System.out.println("VAl: "+ran+" Start: "+start+" End : "+bound);
				return i;
			}
			start += ratio;
		}
		return 0;
	}

	/**
	 * Channel worker class
	 */
	public class ChannelWorker extends Thread {

		ChannelTask channelTask;
		String sessionName;
		SessionMappingConfigs sessionMapping;
		Socket client;
		List<String> remotes;		
		long transactionStartMillis;

		/**
		 * Constructor
		 * @param sessionName
		 * @param client
		 * @param sessionMapping
		 */
		public ChannelWorker(String sessionName, Socket client, SessionMappingConfigs sessionMapping) {
			this.transactionStartMillis = System.currentTimeMillis();
			this.sessionName = sessionName;
			this.sessionMapping = sessionMapping;
			this.client = client;
			this.remotes = sessionMapping.getRemoteHosts();			
		}

		/**
		 * Get session name
		 * 
		 * @return
		 */
		public String getSessionName() {
			return this.sessionName;
		}

		/**
		 * Close channel task
		 * @throws IOException
		 */
		public void close() throws IOException {
			this.interrupt();
			this.channelTask.close();
		}

		@Override
		public void run() {
			long roundRobinIndex = 0;
			try {
				switch (sessionMapping.getSessionModeEnum()) {
				case STAND_ALONE:  
				{
					if (remotes.size() != 1) {
						throw new PortaException(this.sessionMapping.getSessionModeEnum().name(), "[" + sessionName	+ "][SESSION MODE: " + this.sessionMapping.getSessionModeEnum()	+ "] Stand Alone session must have just one remote channel. Check the seesion configuration in config yaml!!!");
					}
					PortaSocket channel = new PortaSocket(this.client, this.sessionMapping, 0);
					int retry = sessionMapping.getRetry();
					for (int i = 0; i < retry; i++) {
						try {
							channel.connect();
							channelTask = new ChannelTask(sessionName, channel);
							logger.info("[" + sessionName + "][SESSION MODE: " + this.sessionMapping.getSessionModeEnum() + "] Remote channel connected. " + this.sessionMapping.getRemoteHosts().toString());
							standAloneFailed = false;
							SESSION_TOTAL_SUCCESS++;
							break;
						} catch (Exception e) {
							standAloneFailed = true;
							logger.info("[" + sessionName + "][SESSION MODE: " + this.sessionMapping.getSessionModeEnum() + "] Retry channel... Interval: " + Constants.RETRY_INTERVAL + " milliseconds.");
						}
						Thread.sleep(Constants.RETRY_INTERVAL);
					}
					if (standAloneFailed) {
						SESSION_TOTAL_FAIL++;
						if (channel != null) {
							channel.close();
						}
					}
				}
				break;

				case HIGH_AVAILABLE_FAIL_BACK:
					masterSessionFailed = false;
					slaveSessionFailed = false;

				case HIGH_AVAILABLE_FAIL_OVER: 
				{
					if (remotes.size() != 2) {
						throw new PortaException(this.sessionMapping.getSessionModeEnum().name(), "[" + sessionName + "][SESSION MODE: " + this.sessionMapping.getSessionModeEnum()	+ "] HA Fail Over session must have just two remote channel. Check the seesion configuration in config yaml!!!");
					}
					if (masterSessionFailed && slaveSessionFailed) {
						logger.info("[" + sessionName + "][SESSION MODE: " + this.sessionMapping.getSessionModeEnum() + "] Master/Slave channel disable now. Please reboot or rivival using Management console.");
						return;
					}
					if (!masterSessionFailed) {
						PortaSocket master = new PortaSocket(this.client, sessionMapping, 0);
						try {
							master.connect();
							channelTask = new ChannelTask(sessionName, master);
							logger.info("[" + sessionName + "][SESSION MODE: " + this.sessionMapping.getSessionModeEnum() + "] Master channel connected. " + master.getRemoteAddress().toString());
							masterSessionFailed = false;
						} catch (Exception e) {
							masterSessionFailed = true;
							if (master != null) {
								master.close();
							}
							logger.info("[" + sessionName + "][SESSION MODE: " + this.sessionMapping.getSessionModeEnum() + "] Master channel failed. Slave channel be used at next connection.");
							logger.throwable(e);
						}
					}
					if (masterSessionFailed && !slaveSessionFailed) {
						PortaSocket slave = new PortaSocket(this.client, sessionMapping, 1);
						try {
							slave.connect();
							channelTask = new ChannelTask(sessionName, slave);
							logger.info("[" + sessionName + "][SESSION MODE: " + this.sessionMapping.getSessionModeEnum() + "] Slave channel connected. " + slave.getRemoteAddress().toString());
							slaveSessionFailed = false;
						} catch (Exception e) {
							if (slave != null) {
								slave.close();
							}
							slaveSessionFailed = true;
							logger.info("[" + sessionName + "][SESSION MODE: " + this.sessionMapping.getSessionModeEnum() + "] Slave channel failed. Master/Slave failed.");
							logger.throwable(e);
						}
					}
					if (masterSessionFailed && slaveSessionFailed) {
						SESSION_TOTAL_FAIL++;
					} else {
						SESSION_TOTAL_SUCCESS++;
					}
				}
				break;

				case LOAD_BALANCE_ROUND_ROBIN: 
				{
					while (loadBalancedStatus.values().stream().anyMatch(b -> b == true)) {
						int channelIndex = (int) (roundRobinIndex % (long) sessionMapping.getRemoteHosts().size());
						if (!loadBalancedStatus.get(channelIndex)) {
							roundRobinIndex++;
							continue;
						}
						PortaSocket channel = new PortaSocket(this.client, sessionMapping, channelIndex);
						try {
							channel.connect();
							channelTask = new ChannelTask(sessionName, channel);
							SESSION_TOTAL_SUCCESS++;
							logger.info("[" + sessionName + "][SESSION MODE: " + this.sessionMapping.getSessionModeEnum() + "] Load-Balanced Round-Robin channel connected. Channel index: " + channelIndex + "  Host: " + channel.getRemoteAddress().toString());
							break;
						} catch (Exception e) {
							logger.info("[" + sessionName + "][SESSION MODE: " + sessionMapping.getSessionModeEnum() + "] Load-Balance Round-Robin Channel failed. Round-Robin channel index: "	+ channelIndex + "  Host: " + channel.getRemoteAddress().toString());
							SESSION_TOTAL_FAIL++;
							if (channel != null) {
								channel.close();
							}
							loadBalancedStatus.put(channelIndex, false);
						} finally {
							roundRobinIndex++;
							if (!loadBalancedStatus.values().stream().anyMatch(b -> b == true)) {
								logger.info("[" + sessionName + "][SESSION MODE: " + sessionMapping.getSessionModeEnum() + "] All of Load-Balanced remote channel failed. Please check remote hosts healthy. eg. Network, Server status...");
								if (channel != null) {
									channel.close();
								}
								break;
							}
						}
					}
				}
				break;

				case LOAD_BALANCE_SEPARATE_RATIO: 
				{
					List<Float> ratioList = sessionMapping.getLoadBalanceRatioList();
					while (loadBalancedStatus.values().stream().anyMatch(b -> b == true)) {
						int randomIndex = getRandomRatioIndex(ratioList);
						randomIndex = randomIndex >= sessionMapping.getRemoteHosts().size() ? 0 : randomIndex;
						if (!loadBalancedStatus.get(randomIndex)) {
							continue;
						}
						PortaSocket channel = new PortaSocket(this.client, sessionMapping, randomIndex);
						try {
							channel.connect();
							channelTask = new ChannelTask(sessionName, channel);
							SESSION_TOTAL_SUCCESS++;
							logger.info("[" + sessionName + "][SESSION MODE: " + this.sessionMapping.getSessionModeEnum() + "] Load-Balance Separate Ratio Distribution Channel connected. Channel index: " + randomIndex + "  Host: " + sessionMapping.getRemoteHosts().get(randomIndex).toString());
							break;
						} catch (Exception e) {
							logger.info("[" + sessionName + "][SESSION MODE: " + this.sessionMapping.getSessionModeEnum() + "] Load-Balance Separate Ratio Distribution Channel failed. Channel index: " + randomIndex + "  Host: "	+ sessionMapping.getRemoteHosts().get(randomIndex).toString());
							SESSION_TOTAL_FAIL++;
							if (channel != null) {
								channel.close();
							}
							loadBalancedStatus.put(randomIndex, false);
							ratioList.set(randomIndex, 0f);
						} finally {
							if (!loadBalancedStatus.values().stream().anyMatch(b -> b == true)) {
								logger.info("[" + sessionName + "][SESSION MODE: " + this.sessionMapping.getSessionModeEnum()+ "] All of Load-Balance Separate Ratio Distribution remote channel failed. Please check remote host healthy. eg. Network, Server status...");
								if (channel != null) {
									channel.close();
								}
								break;
							}
						}
					}
				}
				break;

				default:
					logger.error("Wrong Session Mode is input: " + this.sessionMapping.getSessionModeEnum().name());
				}
				logger.info("[" + sessionName + "][SESSION MODE: " + this.sessionMapping.getSessionModeEnum() + "] Statistics:  Success = " + SESSION_TOTAL_SUCCESS + "  Fail = " + SESSION_TOTAL_FAIL+ "");
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				this.sessionMapping.putStatistics(RESOURCE.SESSION_TOTAL_SUCCESS, SESSION_TOTAL_SUCCESS);
				this.sessionMapping.putStatistics(RESOURCE.SESSION_TOTAL_FAIL, SESSION_TOTAL_FAIL);
				this.sessionMapping.putStatistics(RESOURCE.SESSION_SEND_SUCCESS_COUNT, SESSION_SEND_SUCCESS_COUNT);
				this.sessionMapping.putStatistics(RESOURCE.SESSION_SEND_FAIL_COUNT, SESSION_SEND_FAIL_COUNT);
				this.sessionMapping.putStatistics(RESOURCE.SESSION_RECEIVE_SUCCESS_COUNT, SESSION_RECEIVE_SUCCESS_COUNT);
				this.sessionMapping.putStatistics(RESOURCE.SESSION_RECEIVE_FAIL_COUNT, SESSION_RECEIVE_FAIL_COUNT);
				this.sessionMapping.putStatistics(RESOURCE.SESSION_SEND_SIZE_TOTAL, SESSION_SEND_SIZE_TOTAL);
				this.sessionMapping.putStatistics(RESOURCE.SESSION_RECEIVE_SIZE_TOTAL, SESSION_RECEIVE_SIZE_TOTAL);
				//System.out.println(this.sessionMapping.getStatistics().toString());
				System.out.println(SESSION_SEND_SIZE_TOTAL+"  "+SESSION_RECEIVE_SIZE_TOTAL);
			}
		}
	}	

	/**
	 * ChannelTask
	 *
	 * @author 9ins 2020. 11. 19.
	 */
	public class ChannelTask { 

		boolean sendDone, receiveDone;
		String channelName;
		Socket client, remote;
		int bufferSize;
		long startMillis;
		Thread sendThr;

		/**
		 * Constructor
		 * 
		 * @param channelName
		 * @param channel
		 * @throws IOException
		 * @throws InterruptedException
		 */
		public ChannelTask(String channelName, PortaSocket channel) throws IOException, InterruptedException {
			this.channelName = channelName;
			this.client = channel.getClientSocket();
			this.remote = channel.getRemoteSocket();
			this.startMillis = System.currentTimeMillis();
			this.bufferSize = sessionMapping.getBufferSize() == 0 ? 1024 * 8 : sessionMapping.getBufferSize();
			CountDownLatch latch = new CountDownLatch(1);		
			this.sendThr = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						logger.info("[" + channelName + "] Send channel opening to: "+remote.getInetAddress().toString());
						SESSION_SEND_SIZE_TOTAL += new InterBroker().process(client.getInputStream(), remote.getOutputStream());
						if(client.isClosed() || remote.isClosed()) {
							return;							
						}
						SESSION_SEND_SUCCESS_COUNT++;
						sendDone = true;
					} catch(IOException e) {
						SESSION_TOTAL_FAIL++;
						SESSION_SEND_FAIL_COUNT++;
						throw new RuntimeException(e);
					} finally {
						latch.countDown();
					} 
				}
			});
			this.sendThr.start();
			logger.info("[" + channelName + "] Receive channel opening from: "+remote.getInetAddress().toString());
			SESSION_RECEIVE_SIZE_TOTAL += new InterBroker().process(remote.getInputStream(), client.getOutputStream());
			if(client.isClosed() || remote.isClosed()) {
				return;
			}
			SESSION_RECEIVE_SUCCESS_COUNT++;
			receiveDone = true;
			latch.await();
			close();
		}

		private class InterBroker {			

			long totalBytes = 0;

			public long process (InputStream is, OutputStream os) throws IOException {				
				int read;
				byte[] buffer = new byte[bufferSize];
				try {
					while (( read = is.read(buffer) ) > 0) {
						os.write(buffer, 0, read);
						os.flush();
						totalBytes += read;
					}
					is.close();
					os.close();
				} catch(Exception e) {
					if( e instanceof SocketException && e.getMessage().contains("Socket closed")) {	} 
					else {
						throw e;
					}
				}
				return totalBytes;
			}
		}

		public void close() throws IOException {
			if(this.sendThr != null) {
				this.sendThr.interrupt();
			}
			if(this.remote != null) {
				this.remote.close();
			}
			if(this.client != null) {
				this.client.close();
			}
			logger.info("[" + this.channelName + "] Channel Closed.  Channel Elapse Time Millis: " + (System.currentTimeMillis() - startMillis));
		}
	}
}
