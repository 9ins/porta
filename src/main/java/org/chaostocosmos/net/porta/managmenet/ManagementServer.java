package org.chaostocosmos.net.porta.managmenet;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.EnumSet;

import org.chaostocosmos.net.porta.ResourceUtils;
import org.chaostocosmos.net.porta.PortaMain;
import org.chaostocosmos.net.porta.config.ConfigHandler;
import org.chaostocosmos.net.porta.credential.CredentialsHandler;
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

	PortaMain portaMain;
	Server server;
	ServerConnector connector;
	QueuedThreadPool threadPool;
	BasicSecurityManager securityManager;
	ConfigHandler configHandler;
	CredentialsHandler credentialsHandler;
	DefaultSessionIdManager sessionIdManager;
	HouseKeeper houseKeeper;

	/**
	 * Constructor
	 * 
	 * @param tcpProxy
	 * @param configHandler
	 * @param credentialsHandler
	 * @throws Exception
	 */
	public ManagementServer(PortaMain tcpProxy, ConfigHandler configHandler, CredentialsHandler credentialsHandler)
			throws Exception {
		this.portaMain = tcpProxy;
		this.configHandler = configHandler;
		this.credentialsHandler = credentialsHandler;
		init();
	}

	public void init() throws Exception {
		// Initialize thread pool
		this.threadPool = new QueuedThreadPool(30, 30, 10000);
		this.threadPool.setDaemon(true);

		// Jetty server create and set connector
		this.server = new Server(this.threadPool);
		this.sessionIdManager = new DefaultSessionIdManager(this.server);
		this.sessionIdManager.setWorkerName("TCPProxy");
		this.server.setSessionIdManager(this.sessionIdManager);

		// Setting house keeper
		this.houseKeeper = new HouseKeeper();
		this.houseKeeper.setIntervalSec(10L);
		this.sessionIdManager.setSessionHouseKeeper(this.houseKeeper);

		// Initialize server connector
		this.connector = new ServerConnector(this.server);
		this.connector.setAccepting(true);
		this.connector.setHost(this.configHandler.getConfig().getManagementAddress());
		this.connector.setPort(this.configHandler.getConfig().getManagementPort());
		this.server.setConnectors(new Connector[] { this.connector });

		// initialize security manager
		this.securityManager = new BasicSecurityManager(this.credentialsHandler.getCredentials());

		// Setting ManagementServelt & Handler
		Path resourcePath = Paths.get(this.configHandler.getConfig().getManagementResourceBase());

		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setResourceBase(resourcePath.toString());		
		webAppContext.setSessionHandler(new SessionHandler());
		LoginServlet mServlet = new LoginServlet(this.portaMain);		
		ServletHolder servletHolder = new ServletHolder(mServlet);		
		webAppContext.addServlet(servletHolder, "/login/*");
		webAppContext.setWelcomeFiles(new String[] { "index.html" });
		webAppContext.setContextPath("/");

		//WebAppContext webAppContext2 = new WebAppContext();
		//webAppContext2.setResourceBase(resourcePath.toString());		
		//webAppContext2.setSessionHandler(new SessionHandler());
		ResourceUsageServlet mServlet2 = new ResourceUsageServlet(this.portaMain);		
		ServletHolder servletHolder2 = new ServletHolder(mServlet2);		
		
		webAppContext.addServlet(servletHolder2, "/resources/*");
		FilterHolder filterHolder = new FilterHolder(LoginFilter.class);
		filterHolder.setInitParameter("Access-Control-Allow-Origin", "*");
		filterHolder.setInitParameter("Access-Control-Allow-Methods", "GET, POST");
		webAppContext.addFilter(filterHolder, "/*", EnumSet.of(DispatcherType.REQUEST));


		HandlerList list = new HandlerList();
		list.addHandler(webAppContext);

		this.server.setHandler(list);
		Arrays.asList(this.server.getChildHandlers()).stream().forEach(h -> System.out.println(h.toString()));

		this.server.start();
		webAppContext.getSessionHandler().setMaxInactiveInterval(5);
	}

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