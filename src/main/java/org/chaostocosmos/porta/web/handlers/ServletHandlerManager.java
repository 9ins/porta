package org.chaostocosmos.porta.web.handlers;

import java.lang.reflect.InvocationTargetException;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import org.chaostocosmos.porta.Context;
import org.chaostocosmos.porta.Logger;
import org.chaostocosmos.porta.PortaException;
import org.chaostocosmos.porta.properties.Handlers;
import org.chaostocosmos.porta.web.HTTP.METHOD;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletHolder;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServlet;

/**
 * HandlerManager object
 */
public class ServletHandlerManager {

    Context context;
    ServletHandlerLoader loader;
    HandlerList handlerList = new HandlerList();
    
    /**
     * Constructor
     * @param context
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws PortaException
     */
    public ServletHandlerManager(Context context) throws Exception {
        this.context = context; 
        this.loader = ServletHandlerLoader.getInstance();         
        loadHandlersAndServlets();
    }

    /**
     * Load handlers
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws PortaException
     */
    public void loadHandlersAndServlets() throws Exception {
        Handlers handlers = this.context.getPropertiesHelper().getHandlers();
        for(Map<String, Object> map : handlers.getHandlers()) {
            //load handler
            String handlerClass = map.get("handlerClass")+"";
            PortaHandler handler = loader.loadHandler(handlerClass, map.get("contextPath")+"", map.get("resourceBase")+"", new String[]{map.get("welcomePath")+""}, (int)map.get("maxInactiveIntervalSeconds"));            

            //load filter and add filter to handler
            List<Map<String, Object>> filterList = (List<Map<String, Object>>)map.get("filters");
            for(Map<String, Object> filterMap : filterList) {
                Filter filter = loader.loadFilter(filterMap.get("filterClass")+"");
                String filterPathSpec = filterMap.get("filterPathSpec")+"";
                Map<String, Object> initialParamsMap = (Map<String, Object>)filterMap.get("initialParams");
                handler.attachFilter(filter, filterPathSpec, initialParamsMap);
            }
    
            //load servlet and add servlet to handler
            List<Map<String, Object>> servletList = (List<Map<String, Object>>)map.get("servlets");
            for(Map<String, Object> servletMap : servletList) {
                String servletClass = servletMap.get("servletClass")+"";
                String pathSpec = servletMap.get("pathSpec")+"";
                List<METHOD> methods = this.context.getPropertiesHelper().getHandlers().getHttpMethods(handlerClass, servletClass);
                List<String> paramsKeys = this.context.getPropertiesHelper().getHandlers().getHttpParameterKeys(handlerClass, servletClass);
                HttpServlet servlet = loader.loadServlet(servletClass, this.context, methods, paramsKeys, this.context.getCredentials());
                handler.attachServlet(servlet, pathSpec);
                Logger.getInstance().info("////////// addServlet "+servletClass);
            }

            this.handlerList.addHandler(handler.getHandler());
        }
    }

    /**
     * Get handlerList
     * @return
     */
    public HandlerList getHandlerList() {
        return this.handlerList;
    }
}
