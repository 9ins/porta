package org.chaostocosmos.porta.web.handlers;

import java.util.Map;
import org.chaostocosmos.porta.properties.PropertiesHelper;
import org.eclipse.jetty.server.Handler;

/**
 * HandlerManager object
 */
public class HandlerManager {

    PropertiesHelper configsHelper;

    Map<String, Handler> handlerMap;
    
    public HandlerManager(PropertiesHelper configsHelper) {
        this.configsHelper = configsHelper;
    }



}
