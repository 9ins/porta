package org.chaostocosmos.porta.web.handlers;

import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import jakarta.servlet.http.HttpServlet;

/**
 * PortaSessionHandler object
 */
public class PortaSessionHandler extends ServletContextHandler {

    /**
     * constructor
     * @param contextPath
     * @param resourceBase
     * @param welcomeFiles
     */
    public PortaSessionHandler(String contextPath, String resourceBase, String[] welcomeFiles) {
        super();
        super.setResourceBase(resourceBase);
        super.setContextPath(contextPath);
        super.setWelcomeFiles(welcomeFiles);
    }

    /**
     * Add servlet with pathSpec
     * @param servlet
     * @param pathSpec
     */
    public void addServlet(HttpServlet servlet, String pathSpec) {
        ServletHolder holder = new ServletHolder(servlet);
        super.addServlet(holder, pathSpec);
    }
}
