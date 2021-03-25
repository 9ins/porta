package org.chaostocosmos.porta.web.handlers;

import java.util.EnumSet;
import java.util.Map;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.session.DefaultSessionCache;
import org.eclipse.jetty.server.session.NullSessionDataStore;
import org.eclipse.jetty.server.session.SessionCache;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServlet;

/**
 * PortaSessionHandler object
 */
public class PortaHandler {

    ServletContextHandler servletContextHandler;

    /**
     * constructor
     * @param contextPath
     * @param resourceBase
     * @param welcomeFiles
     */
    public PortaHandler(String contextPath, String resourceBase, String[] welcomeFiles, Integer maxInactiveIntervalSeconds) {
        this.servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        SessionHandler sessionHandler = this.servletContextHandler.getSessionHandler();
        sessionHandler.setMaxInactiveInterval(maxInactiveIntervalSeconds);
        DefaultSessionCache sessionCache = new DefaultSessionCache(sessionHandler);
        NullSessionDataStore nullSessionDataStore = new NullSessionDataStore();
        sessionCache.setSessionDataStore(nullSessionDataStore);
        sessionHandler.setSessionCache(sessionCache);
        this.servletContextHandler.setResourceBase(resourceBase);
        this.servletContextHandler.setContextPath(contextPath);
        this.servletContextHandler.setWelcomeFiles(welcomeFiles);
    }

    /**
     * Add filter to handler
     * @param filter
     * @param pathSpec
     * @param initialParamsMap
     */
    public void attachFilter(Filter filter, String pathSpec, Map<String, Object> initialParamsMap) {
        FilterHolder filterHolder = new FilterHolder(filter);
        for(Map.Entry<String, Object> entry : initialParamsMap.entrySet()) {
            filterHolder.setInitParameter(entry.getKey(), entry.getValue()+"");
        }
        this.servletContextHandler.addFilter(filterHolder, pathSpec, EnumSet.of(DispatcherType.REQUEST));
    }

    /**
     * Add servlet with pathSpec
     * @param servlet
     * @param pathSpec
     */
    public void attachServlet(HttpServlet servlet, String pathSpec) {
        ServletHolder holder = new ServletHolder(servlet);
        this.servletContextHandler.addServlet(holder, pathSpec);
    }

    /**
     * Get handler
     */
    public Handler getHandler() {
        return this.servletContextHandler;
    }
}
