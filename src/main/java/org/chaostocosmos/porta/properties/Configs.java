package org.chaostocosmos.porta.properties;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Configs implements Serializable {

	AppConfigs appConfigs;
	ManagementConfigs managementConfigs;
	ThreadPoolConfigs threadPoolConfigs;
	Map<String, SessionMappingConfigs> sessionMappingConfigs = new HashMap<>();

	public Configs() {
	}

	public AppConfigs getAppConfigs() {
		return this.appConfigs;
	}

	public void setAppConfigs(AppConfigs appConfigs) {
		this.appConfigs = appConfigs;
	}

	public ManagementConfigs getManagementConfigs() {
		return this.managementConfigs;
	}

	public void setManagementConfigs(ManagementConfigs managementConfigs) {
		this.managementConfigs = managementConfigs;
	}

	public ThreadPoolConfigs getThreadPoolConfigs() {
		return this.threadPoolConfigs;
	}

	public void setThreadPoolConfigs(ThreadPoolConfigs threadPoolConfigs) {
		this.threadPoolConfigs = threadPoolConfigs;
	}

	public Map<String,SessionMappingConfigs> getSessionMapping() {
		return this.sessionMappingConfigs;
	}

	public void setSessionMappingConfigs(Map<String,SessionMappingConfigs> sessionMappingConfigs) {
		this.sessionMappingConfigs = sessionMappingConfigs;
	}

	public SessionMappingConfigs getSessionMappingConfigs(String name) {
		return this.sessionMappingConfigs.get(name);
	}

	@Override
	public String toString() {
		return "{" +
			" appConfigs='" + appConfigs + "'" +
			", managementConfigs='" + managementConfigs + "'" +
			", threadPoolConfigs='" + threadPoolConfigs + "'" +
			", sessionMappingConfigs='" + sessionMappingConfigs + "'" +
			"}";
	}
}

