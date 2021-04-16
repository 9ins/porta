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
public class PortaSocket {
	String remoteHost;
	int port;
	int connectionTimeout;
	int sessionIndex;
	InetSocketAddress remoteAddress;
	SessionMappingConfigs sm;
	Socket client, remote;

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
		String r = sm.getRemoteHosts().get(sessionIndex);
		if (r.indexOf(":") == -1) {
			throw new PortaException("remoteHosts",	new Object[]{"Remote host must be defined like HOST:PORT format!!! Defined format is: " + r});
		}
		this.client = client;
		this.remoteHost = r.substring(0, r.lastIndexOf(":"));
		this.port = Integer.parseInt(r.substring(r.lastIndexOf(":") + 1));
		this.sm = sm;
		this.sessionIndex = sessionIndex;
		this.connectionTimeout = this.sm.getConnectionTimeout();
	}

	public void connect() throws IOException {	
		this.remote = new Socket();
		this.remote.setSoTimeout(sm.getSoTimeout());
		this.remote.setKeepAlive(sm.isKeepAlive());
		if(sm.getBufferSize() != 0) {
			this.remote.setReceiveBufferSize(sm.getBufferSize());
			this.remote.setSendBufferSize(sm.getBufferSize());
		}
		this.remote.setTcpNoDelay(sm.isTcpNoDelay());
		this.remote.connect(new InetSocketAddress(this.remoteHost, this.port), this.connectionTimeout);
	}

	public void close() throws IOException {
		if(this.client != null) {
			this.client.close();
		}
		if(this.remote != null) {
			this.remote.close();
		}
	}

	public Socket getClientSocket() {
		return this.client;
	}

	public boolean isClientConnected() {
		return this.client.isConnected();
	}

	public Socket getRemoteSocket() {
		return this.remote;
	}

	public boolean isRemoteConnected() {
		return this.remote.isConnected();
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
