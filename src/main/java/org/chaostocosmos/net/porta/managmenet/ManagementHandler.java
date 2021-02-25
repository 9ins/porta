package org.chaostocosmos.net.porta.managmenet;

import java.nio.file.Path;

import org.chaostocosmos.net.porta.PortaMain;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class ManagementHandler extends ServletContextHandler {

    ManagementServlet servlet;    
    String contextPath;
    Path resourceBase;
    
    public ManagementHandler(PortaMain tcpProxy, String contextPath, Path resourceBase) {
        super(ServletContextHandler.SESSIONS);
        this.servlet = new ManagementServlet(tcpProxy);
        this.contextPath = contextPath;
        this.resourceBase = resourceBase;
        ServletHolder holder = new ServletHolder(servlet);
        this.setWelcomeFiles(new String[]{"index.html"});
        this.addServlet(holder, contextPath+"*");
        this.setResourceBase(this.resourceBase.toString());
    }


}
