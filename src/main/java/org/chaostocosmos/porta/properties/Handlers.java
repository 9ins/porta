package org.chaostocosmos.porta.properties;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.chaostocosmos.porta.PortaException;
import org.chaostocosmos.porta.web.HTTP.METHOD;

/**
 * Handlers 
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

    public String getFirstHandlerName() {
        return (String)getHandlers().get(0).get("handlerClass");
    }

    public List<METHOD> getHttpMethods(String servletClassName) throws PortaException {
        return getHttpMethods(getFirstHandlerName(), servletClassName);
    }

    public List<String> getHttpParameterKeys(String servletClassName) throws PortaException {
        return getHttpParameterKeys(getFirstHandlerName(), servletClassName);
    }

    public List<METHOD> getHttpMethods(String handlerClassName, String servletClassName) throws PortaException {
        Map<String, Object> handlerMap = this.handlers.stream().filter(m -> m.get("handlerClass").equals(handlerClassName)).findAny().orElse(null);
        if(handlerMap == null) {
            throw new PortaException("ERRCODE002", new Object[]{handlerClassName});
        }
        List<Map<String, Object>> servletList = (List<Map<String, Object>>)handlerMap.get("servlets");
        Map<String, Object> servletMap = (Map<String, Object>)servletList.stream().filter(m -> m.get("servletClass").equals(servletClassName)).findAny().orElse(null);
        if(servletMap == null) {
            throw new PortaException("ERRCODE003", new Object[]{servletClassName});
        }
        return ((List<String>)servletMap.get("httpMethod")).stream().map(s -> METHOD.valueOf(s)).collect(Collectors.toList());
    }

    public List<String> getHttpParameterKeys(String handlerClassName, String servletClassName) throws PortaException {
        Map<String, Object> handlerMap = this.handlers.stream().filter(m -> m.get("handlerClass").equals(handlerClassName)).findAny().orElse(null);
        if(handlerMap == null) {
            throw new PortaException("ERRCODE002", new Object[]{handlerClassName});
        }
        List<Map<String, Object>> servletList = (List<Map<String, Object>>)handlerMap.get("servlets");
        Map<String, Object> servletMap = (Map<String, Object>)servletList.stream().filter(m -> m.get("servletClass").equals(servletClassName)).findAny().orElse(null);
        if(servletMap == null) {
            throw new PortaException("ERRCODE003", new Object[]{servletClassName});
        }
        return (List<String>)servletMap.get("paramKeys");
    }

    @Override
    public String toString() {
        return "{" +
            " handlers='" + handlers + "'" +
            "}";
    }    
}
