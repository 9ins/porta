package org.chaostocosmos.porta;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.chaostocosmos.porta.web.ManagementServer;

/**
 * Porta - Ultimate Relay gateway & Load-balance service
 * 
 * Description : Porta is relay gateway and load-balance service. 
 * Porta relay TCP Connection between local and remote. Use when you didn't connect remote directly, First, install this
 * proxy at relay host(able to connect to remote) Second, This Proxy must have
 * 'mapping.properties' file.(which program argument) edit 'mapping.properties'
 * file what you want to mapping to remote host. Third, connect relay host port
 * from your host. finally enjoy to benefit. when you might ask for something,
 * Don't hesitate to ask by e-mail.(chaos930@gmail.com)
 * 
 * Modification Information --------- --------- -------------------------------
 * 20180627 9ins First draft 20201118 9ins Modify configuration to operate into
 * yaml style. And improve functionalities.
 * 
 * @author 9ins
 * @since 20180627
 * @version 1.0 *
 * @copyright All right reserved by Author
 * @email chaos930@gmail.com
 */
public class PortaMain implements AdminCommandListener {

	Logger logger = Logger.getInstance();

	Context context;

	ServerSocket proxyServer;
	ServerSocket adminServer;
	List<AdminCommandListener> configChangeListeners;
	Map<String, PortaSession> sessionMap;
	PortaSessionHandler sessionHandler;
	PortaThreadPool proxyThreadPool;
	ManagementServer managementServer;

	boolean isDone = false;
	
	/**
	 * Constructor
	 * 
	 * @param mappingFilename
	 * @throws Exception
	 */
	public PortaMain(String configPath_) throws Exception {
		Path configPath = Paths.get(configPath_);
		this.context = new Context(configPath);
	}

	/**
	 * Restart proxy
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	private void restartProxy() throws IOException, InterruptedException {
		closeServer();
		start();
	}

	/**
	 * Start Porta
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void start() throws IOException, InterruptedException {
		try {
			logger.info(Context.TRADE_MARK);
			this.proxyThreadPool = new PortaThreadPool(Context.propertiesHelper.getConfigs().getThreadPoolConfigs().getThreadPoolCoreSize(),
			Context.configs.getThreadPoolConfigs().getThreadPoolMaxSize(),
			Context.configs.getThreadPoolConfigs().getThreadPoolIdleSecond(),
			Context.configs.getThreadPoolConfigs().getThreadPoolQueueSize());
			logger.info("[Thread-Pool] Thread Pool initialized. Core size: "
					+ Context.configs.getThreadPoolConfigs().getThreadPoolCoreSize() + " Max size: "
					+ Context.configs.getThreadPoolConfigs().getThreadPoolMaxSize() + " Idle size: "
					+ Context.configs.getThreadPoolConfigs().getThreadPoolIdleSecond() + " Waiting Queue size: "
					+ Context.configs.getThreadPoolConfigs().getThreadPoolQueueSize());
			this.sessionHandler = new PortaSessionHandler(Context.propertiesHelper, this.proxyThreadPool); 
			this.sessionMap = this.sessionHandler.getPortaSessionMap();
			if (Context.configs.getManagementConfigs().getManagementActivation()) {
				this.managementServer = new ManagementServer(this, Context.propertiesHelper);
				this.managementServer.start();
			}
			this.sessionHandler.start();
		} catch (Exception e) {
			Logger.getInstance().throwable(e);
			shutdown();
		}
	}

	/**
	 * Shutdown Porta
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void shutdown() throws IOException, InterruptedException { 
		closeAllSession();
		this.proxyThreadPool.shutdown();
	}

	/**
	 * Close server socket of Porta
	 * 
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void closeServer() throws IOException, InterruptedException {
		this.sessionHandler.closeAllSessions();
	}

	/**
	 * 
	 * Close all session
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void closeAllSession() throws IOException, InterruptedException {
		this.sessionHandler.closeAllSessions();
	}

	/**
	 * Get proxy session handler
	 * 
	 * @return
	 */
	public PortaSessionHandler getPortaSessionHandler() {
		return this.sessionHandler;
	}

	/**
	 * Convert byte array to Hex string
	 * 
	 * @param a
	 * @return
	 */
	public static String byteArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder(a.length * 2);
		for (byte b : a) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	/**
	 * Add config change listener
	 * 
	 * @param listener
	 */
	public void addConfigChangeListener(AdminCommandListener listener) {
		if (!this.configChangeListeners.contains(listener)) {
			this.configChangeListeners.add(listener);
		}
	}

	/**
	 * Remove config change listener
	 * 
	 * @param listener
	 */
	public void removeConfigChangeListener(AdminCommandListener listener) {
		this.configChangeListeners.remove(listener);
	}

	/**
	 * Dispatch config change event
	 * 
	 * @param configHandler
	 */
	public void dispachConfigChangeEvent(AdminCommand cmd) {
		this.configChangeListeners.stream().forEach(l -> l.receiveConfigChangeEvnet(new AdminCommandEvent(this, cmd)));
	}

	@Override
	public void receiveConfigChangeEvnet(AdminCommandEvent ace) {
		try {
			if (ace.getAdminCommand().isRestartProxy()) {
				restartProxy();
			} else if (ace.getAdminCommand().isRestartServerSocket()) {
				this.sessionHandler.restart();
			}
		} catch (Exception e) {
			Logger.getInstance().throwable(e);
		}
	}

	/**
	 * Main
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception {
		if (args.length == 1) {
			PortaMain proxy = new PortaMain(args[0]);
			proxy.start();
		} else {
			System.out.println("Useage : Porta must have one program parameter which name is 'config.yml'");
		}
	}
}
