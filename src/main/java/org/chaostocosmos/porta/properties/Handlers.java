package org.chaostocosmos.porta.properties;

import java.util.List;
import java.util.Map;

/**
 * Handlers
 * 
 */
public class Handlers {

    List<Map<String, Object>> handlers;

    public Handlers() {}

    public List<Map<String,Object>> getHandlers() {
        return this.handlers;
    }

    public void setHandlers(List<Map<String,Object>> handlers) {
        this.handlers = handlers;
    }

    @Override
    public String toString() {
        return "{" +
            " handlers='" + handlers + "'" +
            "}";
    }    
}
