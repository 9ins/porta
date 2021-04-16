package org.chaostocosmos.porta.properties;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.chaostocosmos.porta.PortaException;
import org.chaostocosmos.porta.SESSION_MODE;

/**
 * SessionMapping
 *
 * @author 9ins 2020. 11. 30.
 */
public class SessionMappingConfigs implements Serializable {
	
	private List<String> allowedHosts = new ArrayList<>();
	private List<String> forbiddenHosts = new ArrayList<>();
	private boolean keepAlive;
	private boolean tcpNoDelay;
	private String bindAddress;
	private int port;
	private List<String> remoteHosts = new ArrayList<>();
	private String sessionMode;
	private String loadBalanceRatio;
	private int retry;
	private long retryInterval;
	private int failedCircularRetry;
	private int bufferSize;
	private int connectionTimeout;
	private int soTimeout;

	SessionMappingConfigs() {}

	public Map<Object, Object> getSessionMappingMap() throws IllegalArgumentException, IllegalAccessException {
		Map<Object, Object> map = new HashMap<>();
		Field[] fields = this.getClass().getDeclaredFields();
		for (Field f : fields) {
			map.put(f.getName(), f.get(this));
		}
		return map;
	}

	public List<String> getAllowedHosts() {
		return allowedHosts;
	}

	public void setAllowedHosts(List<String> allowedHosts) {
		this.allowedHosts = allowedHosts;
	}

	public List<String> getForbiddenHosts() {
		return forbiddenHosts;
	}

	public void setForbiddenHosts(List<String> forbiddenHosts) {
		this.forbiddenHosts = forbiddenHosts;
	}

	public boolean isKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

	public boolean isTcpNoDelay() {
		return tcpNoDelay;
	}

	public void setTcpNoDelay(boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}

	public String getBindAddress() {
		return bindAddress;
	}

	public void setBindAddress(String bindAddress) {
		this.bindAddress = bindAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public List<String> getRemoteHosts() {
		return remoteHosts;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public int getSoTimeout() {
		return soTimeout;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public String getSessionMode() {
		return sessionMode;
	}

	public String getLoadBalanceRatio() {
		return this.loadBalanceRatio;
	}

	public int getFailedCircularRetry() {
		return this.failedCircularRetry;
	}

	public void setFailedCircularRetry(int failedCircularRetry) {
		this.failedCircularRetry = failedCircularRetry;
	}

	public void setRemoteHosts(List<String> remoteHosts) {
		this.remoteHosts = remoteHosts;
	}

	public void setSessionMode(String sessionMode) {
		this.sessionMode = sessionMode;
	}

	public void setLoadBalanceRatio(String loadBalanceRatio) {
		this.loadBalanceRatio = loadBalanceRatio;
	}

	public boolean getKeepAlive() {
		return this.keepAlive;
	}

	public boolean getTcpNoDelay() {
		return this.tcpNoDelay;
	}

	public long getRetryInterval() {
		return this.retryInterval;
	}

	public void setRetryInterval(long retryInterval) {
		this.retryInterval = retryInterval;
	}

	public SESSION_MODE getSessionModeEnum() throws PortaException {
		if (sessionMode.equalsIgnoreCase("SA")) {
			return SESSION_MODE.STAND_ALONE;
		} else if (sessionMode.equalsIgnoreCase("HA_FO")) {
			return SESSION_MODE.HIGH_AVAILABLE_FAIL_OVER;
		} else if (sessionMode.equalsIgnoreCase("HA_FB")) {
			return SESSION_MODE.HIGH_AVAILABLE_FAIL_BACK;
		} else if (sessionMode.equalsIgnoreCase("LB_LR")) {
			return SESSION_MODE.LOAD_BALANCE_ROUND_ROBIN;
		} else if (sessionMode.equalsIgnoreCase("LB_SR")) {
			return SESSION_MODE.LOAD_BALANCE_SEPARATE_RATIO;
		} else {
			try {
				return SESSION_MODE.valueOf(sessionMode); 
			} catch (Exception e) {
				throw new PortaException("remoteMode", new Object[]{"remoteMOde must be among STAND_ALONE / HIGH_AVAILABLE_FAIL_OVER / HIGH_AVAILABLE_FAIL_BACK / LOAD_BALANCE_ROUND_ROBIN / LOAD_BALANCE_SEPARATE_RATIO."});
			}
		}
	}

	/**
	 * Get load balance ratio list.
	 * 
	 * @return
	 * @throws PortaException
	 */
	public List<Float> getLoadBalanceRatioList() throws PortaException {
		if (loadBalanceRatio == null || remoteHosts.size() == 0 || remoteHosts.size() != loadBalanceRatio.split(":").length) {
			throw new PortaException("loadBalanceRatio", new Object[]{"It must be same count between remote hosts count and Load-Balanced Ratio tokens in LOAD_BALANCE_SEPARATE_RATIO mode."});
		}
		return Arrays.asList(loadBalanceRatio.split(":")).stream().map(f -> Float.parseFloat(f)).collect(Collectors.toList());
	}

	/**
	 * Get loadbalance ratio map on mapping with remote host.
	 * 
	 * @return
	 */
	public Map<String, Float> getLoadBalanceRatioMap() {
		Map<String, Float> map = new HashMap<>();
		List<Float> ratioList = Arrays.asList(loadBalanceRatio.split(":")).stream().map(f -> Float.parseFloat(f)).collect(Collectors.toList());
		float sum = 0.0f;
		for (Float f : ratioList) {
			sum += f;
			if (sum > 100f) {
				f = 100f - (sum - f);
			}
			if (remoteHosts.size() > 0) {
				String remote = remoteHosts.remove(0);
				map.put(remote, f);
			}
		}
		if (remoteHosts.size() > 0) {
			for (String remote : remoteHosts) {
				map.put(remote, 0f);
			}
		}
		return map;
	}

	public boolean isForbiddenHost(InetSocketAddress socketAddr) {
		return isForbiddenHost(socketAddr.getHostName(), socketAddr.getPort());
	}

	public boolean isForbiddenHost(String hostPort) {
		return isForbiddenHost(hostPort.substring(0, hostPort.lastIndexOf(":")),
				Integer.parseInt(hostPort.substring(hostPort.lastIndexOf(":") + 1)));
	}

	public boolean isForbiddenHost(String host, int port) {
		if (this.forbiddenHosts.contains(host + ":" + port) || this.forbiddenHosts.contains(host + ":*")) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "{" +
			" allowedHosts='" + allowedHosts + "'" +
			", forbiddenHosts='" + forbiddenHosts + "'" +
			", keepAlive='" + keepAlive + "'" +
			", tcpNoDelay='" + tcpNoDelay + "'" +
			", bindAddress='" + bindAddress + "'" +
			", port='" + port + "'" +
			", remoteHosts='" + remoteHosts + "'" +
			", sessionMode='" + sessionMode + "'" +
			", loadBalanceRatio='" + loadBalanceRatio + "'" +
			", retry='" + retry + "'" +
			", retryInterval='" + retryInterval + "'" +
			", failedCircularRetry='" + failedCircularRetry + "'" +
			", bufferSize='" + bufferSize + "'" +
			", connectionTimeout='" + connectionTimeout + "'" +
			", soTimeout='" + soTimeout + "'" +
			"}";
	}
}
