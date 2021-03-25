package org.chaostocosmos.porta.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import org.chaostocosmos.porta.Context;
import org.chaostocosmos.porta.ISessionStatus;
import org.chaostocosmos.porta.Logger;
import org.chaostocosmos.porta.PortaException;
import org.chaostocosmos.porta.RESOURCE;
import org.chaostocosmos.porta.UNIT;
import org.chaostocosmos.porta.UtilBox;
import org.chaostocosmos.porta.properties.Credentials;
import org.chaostocosmos.porta.properties.Messages;
import org.chaostocosmos.porta.properties.PropertiesHelper;
import org.chaostocosmos.porta.properties.SessionMappingConfigs;
import org.chaostocosmos.porta.web.HTTP.METHOD;
import org.chaostocosmos.porta.web.HTTP.RESPONSE;
import org.chaostocosmos.porta.web.HTTP.RESPONSE_TYPE;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * SessionStatusServlet
 * 
 */
public class SessionStatusServlet extends AbstractHttpServlet implements ISessionStatus {

    Gson gson = new Gson();
    Context context;

    public SessionStatusServlet(Context context, List<METHOD> methods, List<String> paramKeys, Credentials credentials) {
        super(context, methods, paramKeys, credentials);
    }

    @Override
    public Map<Object, Object> toDoGet(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramValueMap, Credentials credentials, Messages messages) throws ServletException, IOException {
        System.out.println("############# GET : " + request.getQueryString());
        System.out.println(request.getParameterMap().toString());
        RESOURCE type = RESOURCE.valueOf(request.getParameter("type"));
        UNIT unit = UNIT.valueOf(request.getParameter("unit"));
        String sessionName = request.getParameter("name");
        Map<Object, Object> map = new LinkedHashMap<>();
        try {
            switch (type) {
                case SESSION_INFO:
                map = getSessionInfo(sessionName);
                break;
                case SESSIONS_INFO:
                map = getSessionsInfo();
                break;
                case SESSION_USAGE:
                map = getSessionUsage(sessionName);
                break;
                case SESSIONS_USAGE:
                map = getSessionsUsage();
                break;
                case SESSION_THROUGHPUT:
                map = getSessionThroughput(sessionName);
                break;
                case SESSIONS_THROUGHPUT:
                map = getSessionsThroughput();
                break;
                default:
                throw new PortaException("ERRCODE001", new Object[]{type});
            }
        } catch (Exception e) {
            Logger.getInstance().throwable(e);
        }
        map.put(RESPONSE.CONTENT_TYPE, "application/json");
        map.put(RESPONSE.RESPONSE_CODE, HttpServletResponse.SC_OK);
        map.put(RESPONSE.RESPONSE_TYPE, RESPONSE_TYPE.JSON);
        map.put(RESPONSE.RESPONSE_CONTENT, this.gson.toJson(map));        
        return null;
    }

    @Override
    public Map<Object, Object> toDoPost(HttpServletRequest request, HttpServletResponse response, String body, Credentials credentials, Messages messages) throws ServletException, IOException {
        return null;
    }

    @Override
    public Map<Object, Object> toDoPostJson(HttpServletRequest request, HttpServletResponse response, String json, Credentials credentials, Messages messages) throws ServletException, IOException {
        return null;
    }

    @Override
    public Map<Object, Object> toDoPostFile(HttpServletRequest request, HttpServletResponse response, File file, Credentials credentials, Messages messages) throws ServletException, IOException {
        return null;
    }    

    @Override
    public Map<Object, Object> getSessionInfo(String sessionName) throws Exception {
        return null;
    }

    @Override
    public Map<Object, Object> getSessionsInfo() throws Exception {        
        return null;
    }

    @Override
    public Map<Object, Object> getSessionUsage(String sessionName) throws Exception {
        return null;
    }

    @Override
    public Map<Object, Object> getSessionsUsage() throws Exception {
        
        return null;
    }

    @Override
    public Map<Object, Object> getSessionThroughput(String sessionName) throws Exception {
        return null;
    }

    @Override
    public Map<Object, Object> getSessionsThroughput() throws Exception {
        return null;
    }
}
