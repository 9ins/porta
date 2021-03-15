package org.chaostocosmos.porta;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.chaostocosmos.porta.properties.Configs;
import org.chaostocosmos.porta.properties.PropertiesHelper;
import org.chaostocosmos.porta.properties.SessionMappingConfigs;

/**
 * 
 * ProxySessionHandler
 *
 * @author 9ins 2020. 11. 19.
 */
public class PortaSessionHandler {

	boolean isDone = false;
	PropertiesHelper configHandler;
	ServerSocket proxyServer;
	PortaThreadPool proxyThreadPool;
	Map<String, PortaSession> sessionMap;
	Logger logger;

	/**
	 * Constructor
	 * 
	 * @param proxy
	 * @throws FileNotFoundException
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public PortaSessionHandler(PropertiesHelper configHandler, PortaThreadPool proxyThreadPool)
			throws FileNotFoundException {
		this.configHandler = configHandler;
		this.proxyThreadPool = proxyThreadPool;
		this.sessionMap = new HashMap<>();
		this.logger = Logger.getInstance();
		initProxySessions(this.configHandler.getConfigs());
	}

	/**
	 * Create proxy sessions
	 * 
	 * @param config
	 */
	public void initProxySessions(Configs config) {
		for (String sessionName : config.getSessionMapping().keySet()) {
			this.sessionMap.put(sessionName, createProxySession(sessionName, config.getSessionMappingConfigs(sessionName)));
		}
	}

	/**
	 * Create proxy session
	 * 
	 * @param sessionName
	 * @param sessionMapping
	 * @return
	 */
	public PortaSession createProxySession(String sessionName, SessionMappingConfigs sessionMapping) {
		PortaSession ps = new PortaSession(sessionName, sessionMapping, this.proxyThreadPool);
		return ps;
	}

	/**
	 * Start handler
	 */
	public void start() {
		this.sessionMap.values().forEach(p -> p.start());
	}

	/**
	 * Restart handler
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void restart() throws IOException, InterruptedException {
		closeAllSessions();
		start();
	}

	/**
	 * Get interactive session map
	 * 
	 * @return
	 */
	public Map<String, PortaSession> getPortaSessionMap() {
		return this.sessionMap;
	}

	/**
	 * Close specified session
	 * 
	 * @param sessionName
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void closeSession(String sessionName) throws IOException, InterruptedException {
		this.sessionMap.get(sessionName).closeSession();
	}

	/**
	 * Close all sessions
	 * 
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public void closeAllSessions() throws IOException, InterruptedException {
		for (PortaSession session : this.sessionMap.values()) {
			session.closeSession();
		}
	}
}
