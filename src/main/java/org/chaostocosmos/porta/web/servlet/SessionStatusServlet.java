package org.chaostocosmos.porta.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.chaostocosmos.porta.Context;
import org.chaostocosmos.porta.ISessionStatus;
import org.chaostocosmos.porta.Logger;
import org.chaostocosmos.porta.ModuleProvider;
import org.chaostocosmos.porta.PortaException;
import org.chaostocosmos.porta.RESOURCE;
import org.chaostocosmos.porta.properties.Credentials;
import org.chaostocosmos.porta.properties.MSG_TYPE;
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

    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    ObjectMapper om = new ObjectMapper();

    Context context;

    public SessionStatusServlet(Context context, List<METHOD> methods, List<String> paramKeys, Credentials credentials) {
        super(context, methods, paramKeys, credentials);
    }

    @Override
    public Map<Object, Object> toDoGet(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramMap, Credentials credentials, Messages messages)
            throws ServletException, IOException, PortaException {
        //System.out.println("############# GET : " + request.getQueryString());
        //System.out.println(request.getParameterMap().toString());
        String typeParam = request.getParameter("type");
        String sessionName = request.getParameter("name");
        if (typeParam == null) {
            throw new PortaException("ERRCODE010", null);
        }
        RESOURCE type = RESOURCE.valueOf(typeParam);
        Map<Object, Object> resMap = new LinkedHashMap<>();
        try {
            switch (type) {
                case SESSION_INFO:
                    resMap = getSessionInfo(sessionName);
                    break;
                case SESSIONS_INFO:
                    resMap = getSessionsInfo();
                    break;
                case SESSION_SIMPLE:
                    resMap = getSessionSimple();
                    System.out.println("////////////// "+resMap.toString());
                    break;
                case SESSION_USAGE:
                    resMap = getSessionUsage(sessionName);
                    break;
                case SESSIONS_USAGE:
                    resMap = getSessionsUsage();
                    break;
                case SESSION_THROUGHPUT:
                    resMap = getSessionThroughput(sessionName);
                    break;
                case SESSIONS_THROUGHPUT:
                    resMap = getSessionsThroughput();
                    break;
                default:
                    throw new PortaException("ERRCODE001", new Object[] { type });
            }
            paramMap.put(RESPONSE.CONTENT_TYPE, "application/json");
            paramMap.put(RESPONSE.RESPONSE_CODE, HttpServletResponse.SC_OK);
            paramMap.put(RESPONSE.RESPONSE_TYPE, RESPONSE_TYPE.JSON);

            // System.out.println("##############"+resMap.toString());
            // resMap.entrySet().forEach(e -> System.out.println(e.getKey()+"
            // 6666666666666666666666666666666 "+e.getValue()));
            //String json = this.om.writeValueAsString(resMap);
            String json = this.gson.toJson(resMap, resMap.getClass());
            paramMap.put(RESPONSE.RESPONSE_CONTENT, json);
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
    public Map<Object, Object> toDoPost(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramMap, String body, Credentials credentials, Messages messages) throws ServletException, IOException {
        String typeParam = request.getParameter("type");
        String sessionName = request.getParameter("sessionName");
        if(typeParam == null) {
            throw new PortaException("ERRCODE010", null);
        }
        RESOURCE type = RESOURCE.valueOf(typeParam);
        Map<Object, Object> bodyMap = this.gson.fromJson(body, Map.class);
        try {
            Map<Object, Object> resMap = new LinkedHashMap<>();
            switch(type) {
                case APPLY_SESSION_INFO :
                applySessionInfo(sessionName, bodyMap);    
                saveSessionInfo(sessionName, bodyMap);
                resMap.put("message", messages.getMessage(MSG_TYPE.message, "MSGCODE002"));
                break;
                case APPLY_SESSIONS_INFO :
                break;
                case SAVE_SESSION_INFO :
                break;
                case SAVE_SESSIONS_INFO :
                break;
                case APPLY_SAVE_SESSION_INFO :
                break;
                case APPLY_SAVE_SESSIONS_INFO :
                break;
                default:
            }            
            String json = this.gson.toJson(resMap, resMap.getClass());
            // System.out.println("////////////// "+json);
            paramMap.put(RESPONSE.RESPONSE_CONTENT, json);
            return null;
        } catch(Exception e) {
            Logger.getInstance().throwable(e);
            paramMap.put(RESPONSE.CONTENT_TYPE, "application/json");
            paramMap.put(RESPONSE.RESPONSE_CODE, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            paramMap.put(RESPONSE.RESPONSE_TYPE, RESPONSE_TYPE.JSON);
            paramMap.put(RESPONSE.RESPONSE_CONTENT, e.getMessage());
            return paramMap;
        }
    }

    @Override
    public Map<Object, Object> toDoPostJson(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramMap, String json, Credentials credentials, Messages messages)
            throws ServletException, IOException {
        return null;
    }

    @Override
    public Map<Object, Object> toDoPostFile(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramMap, File file, Credentials credentials, Messages messages)
            throws ServletException, IOException {
        return null;
    }

    @Override
    public Map<Object, Object> getSessionInfo(String sessionName) throws Exception {
        return null;
    }

    @Override
    public Map<Object, Object> getSessionsInfo() throws Exception {
        return ModuleProvider.getResourceManager().getSessionsInfo();
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

    @Override
    public void applySessionInfo(String sessionName, Map<Object, Object> sessionInfoMap) throws Exception {
        ModuleProvider.getResourceManager().applySessionInfo(sessionName, sessionInfoMap);        
    }

    @Override
    public void saveSessionInfo(String sessionName, Map<Object, Object> sessionInfoMap) throws Exception {
        ModuleProvider.getResourceManager().saveSessionInfo(sessionName, sessionInfoMap);        
    }

    @Override
    public void applySessionInfos(Map<String, Map<Object, Object>> sessionsInfoMap) throws Exception {
        ModuleProvider.getResourceManager().applySessionInfos(sessionsInfoMap);
    }

    @Override
    public void saveSessionInfos(Map<String, Map<Object, Object>> sessionsInfoMap) throws Exception {
        ModuleProvider.getResourceManager().saveSessionInfos(sessionsInfoMap);        
    }
}
