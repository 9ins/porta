package org.chaostocosmos.porta;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.chaostocosmos.porta.properties.SessionMappingConfigs;

/**
 * InteractiveThread 
 *
 * @author 9ins 2020. 11. 19.
 */
public class ChannelWorker extends Thread {

    Logger logger = Logger.getInstance();
	final String channelName;
	final SessionMappingConfigs sessionMappingConfigs;
	final Socket client;
	PortaSocket channel;
	int bufferSize;
	long startMillis;
	Channel sendChannel, receiveChannel;

    /**
     * Constructor
     * @param channelName
     * @param client
     * @param sessionMappingConfigs
     * @param startMillis
     */
	public ChannelWorker(String channelName, Socket client, SessionMappingConfigs sessionMappingConfigs, long startMillis) {
		this.channelName = channelName;
		this.client = client;
		this.sessionMappingConfigs = sessionMappingConfigs;
		this.bufferSize = sessionMappingConfigs.getBufferSize();        
		this.startMillis = startMillis;	
	}

	@Override
	public void run() {
        int retry = this.sessionMappingConfigs.getRetry();
        for(int i=0; i<retry; i++) {
            try {				
                this.channel = new PortaSocket(this.client, this.sessionMappingConfigs, 0); 
                this.channel.connect(); 
                this.sendChannel = new Channel(this.channelName+"-"+this.channelName, this.channel.getClientSocket().getInputStream(), this.channel.getRemoteSocket().getOutputStream(), this.bufferSize);
                this.sendChannel.start();
                this.receiveChannel = new Channel(this.channelName+"-"+this.channelName, this.channel.getRemoteSocket().getInputStream(), this.channel.getClientSocket().getOutputStream(), this.bufferSize);
                this.receiveChannel.start();
                break;
            } catch(IOException e) {
                logger.info("[" + channelName + "][SESSION MODE: " + this.sessionMappingConfigs.getSessionModeEnum() + "] Retry channel... Interval: " + Constants.RETRY_INTERVAL + " milliseconds.");
                logger.throwable(e);
            }
        }
    }

    /**
     * Get session mapping configuration.
     * @return
     */
	public SessionMappingConfigs getSessionMappingConfigs() {
		return this.sessionMappingConfigs;
	}

    /**
     * Close send/receive channel.
     */
    public void close() {
        try {
            this.sendChannel.close();
            this.receiveChannel.close();
            logger.info("[" + this.channelName + "] Channel Closed.  Channel Elapse Time Millis: " + (System.currentTimeMillis() - startMillis));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	/**
	 * Send/Receive channel
	 */
	private class Channel extends Thread {
		String channelName;
		InputStream in;
		OutputStream out;
		int bufferSize;
		long totalBytes;
			
		/**
		 * Constructor
		 * @param channelName
		 * @param in
		 * @param out
		 * @param bufferSize
		 */
		public Channel(String channelName, InputStream in, OutputStream out, int bufferSize) {
			this.channelName = channelName;
			this.in = in;
			this.out = out;
			this.bufferSize = bufferSize;
		}

		@Override
		public void run() {
			try {
				logger.info("[" + this.channelName + "] Channel Open......");
				final int bufferSize = sessionMappingConfigs.getBufferSize() == 0 ? 1024 : sessionMappingConfigs.getBufferSize();
				byte[] buffer = new byte[bufferSize];
				int read;
				while (( read = this.in.read(buffer) ) > 0) {
					this.out.write(buffer, 0, read);
					this.out.flush();
					totalBytes += read;
				}
			} catch (Exception e) {
				// May socket closed. Afterword, the logic might to be modified.
				e.printStackTrace();
				if(!e.getMessage().startsWith("Socket closed")) {
					//Logger.getInstance().throwable(e);
				}
			    throw new RuntimeException(e);
			} finally {
				try {
                    close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
			}
		}

        /**
         * Close
         * @throws IOException
         */
		public void close() throws IOException {
            if(this.in != null) {
                this.in.close();
            }
            if(this.out != null) {
                this.out.close();
            }			
		}			
	}    
}

