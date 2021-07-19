package org.chaostocosmos.porta;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.chaostocosmos.porta.properties.Configs;
import org.chaostocosmos.porta.properties.PropertiesHelper;

/**
 * 
 * AdminServerHandler
 *
 * @author 9ins
 * 2020. 11. 18.
 */
public class AdminServerHandler implements Runnable {
	
	boolean isDone = false;
	PortaApp proxy;
	PropertiesHelper configHandler;
	ServerSocket adminServer; 
	Socket adminClient;
	Thread thread;
	
	/**
	 * Constructor
	 * @param proxy
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public AdminServerHandler(PortaApp proxy) throws IOException, ClassNotFoundException {
		this.proxy = proxy;
		this.configHandler = PropertiesHelper.getInstance();
	}
	
	@Override
	public void run() {
		try {
			while(!this.isDone) {
				this.adminServer = new ServerSocket(this.configHandler.getConfigs().getManagementConfigs().getManagementPort(), 1, InetAddress.getByName(this.configHandler.getConfigs().getManagementConfigs().getManagementAddress()));
				Socket socket = this.adminServer.accept();
				if(this.adminClient  ==  null) {
					this.adminClient = socket;
				} else {
					String res = "Previous athenticated connection exist!! Please check out the connection.";
					OutputStream os = socket.getOutputStream();
					os.write(res.getBytes());
					os.close();
					socket.close();
				}
				handleClient(new ObjectInputStream(this.adminClient.getInputStream()));
			}
		} catch(IOException ioe) {
			Logger.getInstance().throwable(ioe);
		}
	}
	
	/**
	 * Handler administrator client 
	 * @param ois
	 */
	private void handleClient(ObjectInputStream ois) {
		while(!this.isDone) {
			try {
				Object readObject = ois.readObject();
				if(readObject instanceof Configs) {
					AdminCommand cmd = (AdminCommand)readObject;
				} else {
					throw new SocketException("Admin managing object must be Config or SessionMapping object. "+readObject.toString());
				}
			} catch(Exception e) {
				Logger.getInstance().throwable(e);
			}
		}
			
	}
	
	/**
	 * Start handler
	 */
	public void start() {
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	/**
	 * Restart handler
	 */
	public void restart() {
		close();
		start();
	}
	
	public void close() {
		this.isDone = true;
		try {
			this.adminClient.close();
			this.adminServer.close();
			this.thread.join();
		} catch(Exception e) {
			Logger.getInstance().throwable(e);
		}
	}
}
