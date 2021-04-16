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
import org.chaostocosmos.porta.ModuleProvider;
import org.chaostocosmos.porta.PortaException;
import org.chaostocosmos.porta.RESOURCE;
import org.chaostocosmos.porta.UNIT;
import org.chaostocosmos.porta.UtilBox;
import org.chaostocosmos.porta.properties.Credentials;
import org.chaostocosmos.porta.properties.Messages;
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
    public Map<Object, Object> toDoGet(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramMap, Credentials credentials, Messages messages) throws ServletException, IOException, PortaException {
        System.out.println("############# GET : " + request.getQueryString());
        System.out.println(request.getParameterMap().toString());
        String typeParam = request.getParameter("type");
        String sessionName = request.getParameter("name");
        if(typeParam == null) {
            throw new PortaException("ERRCODE010", null);
        }
        RESOURCE type = RESOURCE.valueOf(typeParam);
        try {
            switch (type) {
                case SESSION_INFO:
                paramMap = getSessionInfo(sessionName);
                break;
                case SESSIONS_INFO:
                paramMap = getSessionsInfo();
                break;
                case SESSION_SIMPLE:
                paramMap = getSessionSimple();
                break;
                case SESSION_USAGE:
                paramMap = getSessionUsage(sessionName);                
                break;
                case SESSIONS_USAGE:
                paramMap = getSessionsUsage();
                break;
                case SESSION_THROUGHPUT:
                paramMap = getSessionThroughput(sessionName);
                break;
                case SESSIONS_THROUGHPUT:
                paramMap = getSessionsThroughput();
                break;
                default:
                throw new PortaException("ERRCODE001", new Object[]{type});
            }
            paramMap.put(RESPONSE.CONTENT_TYPE, "application/json");
            paramMap.put(RESPONSE.RESPONSE_CODE, HttpServletResponse.SC_OK);
            paramMap.put(RESPONSE.RESPONSE_TYPE, RESPONSE_TYPE.JSON);
            paramMap.put(RESPONSE.RESPONSE_CONTENT, this.gson.toJson(paramMap));        
            return paramMap;
        } catch (Exception e) {
            Logger.getInstance().throwable(e);
            paramMap.put(RESPONSE.CONTENT_TYPE, "application/json");
            paramMap.put(RESPONSE.RESPONSE_CODE, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            paramMap.put(RESPONSE.RESPONSE_TYPE, RESPONSE_TYPE.JSON);
            paramMap.put(RESPONSE.RESPONSE_CONTENT, e.getMessage());
            return paramMap;            
        }
    }

    @Override
    public Map<Object, Object> toDoPost(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramValueMap, String body, Credentials credentials, Messages messages) throws ServletException, IOException {
        return null;
    }

    @Override
    public Map<Object, Object> toDoPostJson(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramValueMap, String json, Credentials credentials, Messages messages) throws ServletException, IOException {
        return null;
    }

    @Override
    public Map<Object, Object> toDoPostFile(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramValueMap,  File file, Credentials credentials, Messages messages) throws ServletException, IOException {
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

    @Override
    public Map<Object, Object> getSessionSimple() throws Exception {        
        return ModuleProvider.getResourceManager().getSessionSimple();
    }
}
