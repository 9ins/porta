package org.chaostocosmos.porta;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.chaostocosmos.porta.properties.SessionMappingConfigs;

/**
 * 
 * ProxySocket
 *
 * @author 9ins 2020. 11. 30.
 */
public class PortaSocket extends Socket {
	int connectionTimeout;
	int sessionIndex;
	InetSocketAddress remoteAddress;
	SessionMappingConfigs sm;
	Socket client;

	/**
	 * Constructor
	 * 
	 * @param remote
	 * @param port
	 * @param sessionMode
	 * @param socketIndex
	 * @param channelRatio
	 * @throws PortaException
	 */
	public PortaSocket(Socket client, SessionMappingConfigs sm, int sessionIndex) throws PortaException {
		String remote = sm.getRemoteHosts().get(sessionIndex);
		if (remote.indexOf(":") == -1) {
			throw new PortaException("remoteHosts",	new Object[]{"Remote host must be defined like HOST:PORT format!!! Defined format is: " + remote});
		}
		this.client = client;
		String host = remote.substring(0, remote.lastIndexOf(":"));
		int port = Integer.parseInt(remote.substring(remote.lastIndexOf(":") + 1));
		this.sm = sm;
		this.sessionIndex = sessionIndex;
		this.remoteAddress = new InetSocketAddress(host, port);
		this.connectionTimeout = this.sm.getConnectionTimeout();
	}

	public Socket connect() throws IOException {
		System.out.println(this.remoteAddress.toString());
		this.setSoTimeout(sm.getSoTimeout());
		this.setKeepAlive(sm.isKeepAlive());
		int bufferSize = sm.getBufferSize() == 0 ? 1024 : sm.getBufferSize();
		this.setReceiveBufferSize(bufferSize);
		this.setSendBufferSize(bufferSize);
		this.setTcpNoDelay(sm.isTcpNoDelay());
		this.connect(this.remoteAddress, this.connectionTimeout);
		return this;
	}

	public void close() throws IOException {
		if(this.client != null) {
			this.client.close();
		}
		super.close();
	}

	public Socket getClientSocket() {
		return this.client;
	}

	public boolean isClientConnected() {
		return this.client.isConnected();
	}

	public InetSocketAddress getRemoteAddress() {
		return this.remoteAddress;
	}

	public int getSessionIndex() {
		return this.sessionIndex;
	}

	public float getChannelRatio() throws PortaException {
		if (this.sm.getSessionModeEnum() != SESSION_MODE.LOAD_BALANCE_SEPARATE_RATIO) {
			throw new PortaException("sessionMode", "This method must use with LOAD_BALANCE_RATIO mode.");
		}
		return this.sm.getLoadBalanceRatioList().get(this.sessionIndex);
	}
}
