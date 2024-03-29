package org.chaostocosmos.porta.web;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumSet;

import org.chaostocosmos.porta.BasicSecurityManager;
import org.chaostocosmos.porta.Context;
import org.chaostocosmos.porta.PortaApp;
import org.chaostocosmos.porta.ResourceUtils;
import org.chaostocosmos.porta.properties.PropertiesHelper;
import org.chaostocosmos.porta.web.filters.LoginFilter;
import org.chaostocosmos.porta.web.handlers.PortaHandler;
import org.chaostocosmos.porta.web.handlers.ServletHandlerManager;
import org.chaostocosmos.porta.web.servlet.LoginServlet;
import org.chaostocosmos.porta.web.servlet.ResourceUsageServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.DefaultSessionIdManager;
import org.eclipse.jetty.server.session.HouseKeeper;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;

import jakarta.servlet.DispatcherType;

/**
 * ManagementServer
 */
public class ManagementServer {

	Context context;
	Server server;
	ServerConnector connector;
	QueuedThreadPool threadPool;
	BasicSecurityManager securityManager;
	PropertiesHelper configHelper;
	DefaultSessionIdManager sessionIdManager;
	ServletHandlerManager servletHandlerManager;
	HouseKeeper houseKeeper;

	/**
	 * Constructor
	 * 
	 * @param tcpProxy
	 * @param configHandler
	 * @param credentialsHandler
	 * @throws Exception
	 */
	public ManagementServer(Context context) throws Exception {
		this.context = context;
		this.configHelper = this.context.getPropertiesHelper();
		init();
	}

	public void init() throws Exception {
		// Initialize thread pool
		this.threadPool = new QueuedThreadPool(30, 30, 10000);
		this.threadPool.setDaemon(true);

		// Jetty server create and set connector
		this.server = new Server(this.threadPool);
		this.sessionIdManager = new DefaultSessionIdManager(this.server);
		this.sessionIdManager.setWorkerName("Porta");
		this.server.setSessionIdManager(this.sessionIdManager);

		// Setting house keeper
		this.houseKeeper = new HouseKeeper();
		this.houseKeeper.setIntervalSec(10L);
		this.sessionIdManager.setSessionHouseKeeper(this.houseKeeper);

		// Initialize server connector
		this.connector = new ServerConnector(this.server);
		this.connector.setAccepting(true);
		this.connector.setHost(this.configHelper.getConfigs().getManagementConfigs().getManagementAddress());
		this.connector.setPort(this.configHelper.getConfigs().getManagementConfigs().getManagementPort());
		this.server.setConnectors(new Connector[] { this.connector });

		// initialize security manager
		this.securityManager = new BasicSecurityManager(this.configHelper.getCredentials());

		// Setting ManagementServelt & Handler
		Path resourcePath = Paths.get(this.configHelper.getConfigs().getManagementConfigs().getManagementResourceBase());

		// initialize servlet handler manager
		this.servletHandlerManager = new ServletHandlerManager(this.context); 
		HandlerList handlerList = this.servletHandlerManager.getHandlerList();

		this.server.setHandler(handlerList);
		Arrays.asList(this.server.getChildHandlers()).stream().forEach(h -> System.out.println(h.toString()));
	}

	/**
	 * Start management server
	 * @throws Exception
	 */
	public void start() throws Exception {
		this.server.start();
	}

	/**
	 * Stop management server
	 * @throws Exception
	 */
	public void stop() throws Exception {
		this.server.stop();
		this.server.join();
	}

	/**
	 * New and get ResourceHandler object. parameter e.g. "static-upload/index.html"
	 * 
	 * @param resourcePath
	 * @return
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 */
	public static Handler newResourceHandler(String resourcePath) throws URISyntaxException, MalformedURLException {
		return newResourceHandler(Paths.get(resourcePath));
	}

	/**
	 * New and get ResourceHandler
	 * 
	 * @param resourcBase
	 * @return
	 * @throws URISyntaxException
	 * @throws MalformedURLException
	 */
	public static Handler newResourceHandler(Path resourcBase) throws URISyntaxException, MalformedURLException {
        URI indexUri = ResourceUtils.findClassLoaderResource(resourcBase.toString());
        URI staticUploadBaseUri = indexUri.resolve("./").normalize();
        Resource baseResource = Resource.newResource(staticUploadBaseUri);
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setBaseResource(baseResource);
        return resourceHandler;
	}
}