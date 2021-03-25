package org.chaostocosmos.porta.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import org.chaostocosmos.porta.Context;
import org.chaostocosmos.porta.IResourceUsage;
import org.chaostocosmos.porta.Logger;
import org.chaostocosmos.porta.ModuleProvider;
import org.chaostocosmos.porta.UNIT;
import org.chaostocosmos.porta.PortaException;
import org.chaostocosmos.porta.RESOURCE;
import org.chaostocosmos.porta.properties.Credentials;
import org.chaostocosmos.porta.properties.Messages;
import org.chaostocosmos.porta.web.HTTP.METHOD;
import org.chaostocosmos.porta.web.HTTP.RESPONSE;
import org.chaostocosmos.porta.web.HTTP.RESPONSE_TYPE;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ResourceUsageServlet extends AbstractHttpServlet implements IResourceUsage {

    Gson gson = new Gson();
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
        Map<Object, Object> map = new LinkedHashMap<>();
        try {
            switch(type) {
                case CPU:
                map = getCpuUsage(unit);
                break;
                case MEMORY:
                map = getMemoryUsage(unit);
                break;
                case THREAD:
                map = getThreadPoolUsage(unit);
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
        paramMap.put(RESPONSE.RESPONSE_CONTENT, this.gson.toJson(map));
        return paramMap;
    }

    @Override
    public Map<Object, Object> toDoPost(HttpServletRequest request, HttpServletResponse response, String body, Credentials credentials, Messages messages) throws ServletException, IOException {
        Map<String, String> reqMap = gson.fromJson(request.getReader(), Map.class);
        Logger.getInstance().info("REQ MAP: "+reqMap);
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

}
