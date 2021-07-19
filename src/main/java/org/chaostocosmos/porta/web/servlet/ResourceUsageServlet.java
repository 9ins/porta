package org.chaostocosmos.porta.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.chaostocosmos.porta.Context;
import org.chaostocosmos.porta.IResourceUsage;
import org.chaostocosmos.porta.ISessionStatus;
import org.chaostocosmos.porta.Logger;
import org.chaostocosmos.porta.ModuleProvider;
import org.chaostocosmos.porta.UNIT;
import org.chaostocosmos.porta.PortaException;
import org.chaostocosmos.porta.RESOURCE;
import org.chaostocosmos.porta.properties.Credentials;
import org.chaostocosmos.porta.properties.MSG_TYPE;
import org.chaostocosmos.porta.properties.Messages;
import org.chaostocosmos.porta.properties.PropertiesHelper;
import org.chaostocosmos.porta.web.HTTP.METHOD;
import org.chaostocosmos.porta.web.HTTP.RESPONSE;
import org.chaostocosmos.porta.web.HTTP.RESPONSE_TYPE;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ResourceUsageServlet extends AbstractHttpServlet implements IResourceUsage {

    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    Context context;

    /**
     * Constructor
     * @param context
     * @param methods
     * @param paramKeys
     * @param credentials
     */
    public ResourceUsageServlet(Context context, List<METHOD> methods, List<String> paramKeys, Credentials credentials) {
        super(context, methods, paramKeys, credentials);
    }

    @Override
    public Map<Object, Object> toDoGet(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramMap, Credentials credentials, Messages messages) throws ServletException, IOException {
        RESOURCE type = RESOURCE.valueOf(request.getParameter("type"));
        UNIT unit = UNIT.valueOf(request.getParameter("unit"));
        Map<Object, Object> resMap = new LinkedHashMap<>();
        try {
            switch(type) {
                case CPU:
                resMap = getCpuUsage(unit);
                break;
                case MEMORY:
                resMap = getMemoryUsage(unit);
                break;
                case THREAD:
                resMap = getThreadPoolUsage(unit);
                break;
                default:
                throw new PortaException("ERRCODE001", new Object[]{type});
            }
        } catch (Exception e) {
            Logger.getInstance().throwable(e);
        }
        paramMap.put(RESPONSE.CONTENT_TYPE, "application/json");
        paramMap.put(RESPONSE.RESPONSE_CODE, HttpServletResponse.SC_OK);
        paramMap.put(RESPONSE.RESPONSE_TYPE, RESPONSE_TYPE.JSON);
        paramMap.put(RESPONSE.RESPONSE_CONTENT, this.gson.toJson(resMap));
        return paramMap;
    }

    @Override
    public Map<Object, Object> toDoPost(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramMap, String body, Credentials credentials, Messages messages) throws ServletException, IOException {                
        Map<String, String> reqMap = gson.fromJson(body, Map.class);

        Logger.getInstance().info("REQ MAP: "+reqMap);
        return paramMap;
    }

    @Override
    public Map<Object, Object> toDoPostJson(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramMap, String json, Credentials credentials, Messages messages) throws ServletException, IOException {
        Map<String, Object> reqMap = gson.fromJson(json, Map.class);
        RESOURCE type = RESOURCE.valueOf(request.getParameter("type"));
        try {
            switch(type) {
                case THREAD_POOL:
                    double coreSize = (double)reqMap.get("corePoolSize");
                    double maxSize = (double)reqMap.get("maximumPoolSize");
                    this.setCorePoolSize((int)coreSize);
                    this.setMaximumPoolSize((int)maxSize);
                    PropertiesHelper.getInstance().dump("configs");
                    reqMap.put("message", messages.getMessage(MSG_TYPE.message, "MSGCODE001"));
                break;
                default:
                    throw new PortaException("ERRCODE001", new Object[]{type});
            }
        } catch (Exception e) {
            Logger.getInstance().throwable(e);
            paramMap.put(RESPONSE.CONTENT_TYPE, "application/json");
            paramMap.put(RESPONSE.RESPONSE_CODE, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            paramMap.put(RESPONSE.RESPONSE_TYPE, RESPONSE_TYPE.JSON);
            paramMap.put(RESPONSE.RESPONSE_CONTENT, e.getMessage());
            return paramMap;
        }
        reqMap.put("status", "success");
        paramMap.put(RESPONSE.CONTENT_TYPE, "application/json");
        paramMap.put(RESPONSE.RESPONSE_CODE, HttpServletResponse.SC_OK);
        paramMap.put(RESPONSE.RESPONSE_TYPE, RESPONSE_TYPE.JSON);
        paramMap.put(RESPONSE.RESPONSE_CONTENT, gson.toJson(reqMap));
        return paramMap;
    }

    @Override
    public Map<Object, Object> toDoPostFile(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramMap, File file, Credentials credentials, Messages messages) throws ServletException, IOException {
        return null;
    }

    @Override
    public Map<Object, Object> getCpuUsage(UNIT unit) throws Exception {
        return ModuleProvider.getResourceManager().getCpuUsage(unit);
    }

    @Override
    public Map<Object, Object> getMemoryUsage(UNIT unit) throws Exception {
        return ModuleProvider.getResourceManager().getMemoryUsage(unit);
    }

    @Override
    public Map<Object, Object> getThreadPoolUsage(UNIT unit) throws Exception {
        return ModuleProvider.getResourceManager().getThreadPoolUsage(unit);
    }

    @Override
    public void setCorePoolSize(int size) throws Exception {
        ModuleProvider.getResourceManager().setCorePoolSize(size);
        PropertiesHelper.getInstance().getConfigs().getThreadPoolConfigs().setThreadPoolCoreSize(size);
    }

    @Override
    public void setMaximumPoolSize(int size) throws Exception {
        ModuleProvider.getResourceManager().setMaximumPoolSize(size);
        PropertiesHelper.getInstance().getConfigs().getThreadPoolConfigs().setThreadPoolMaxSize(size);
    }
}
