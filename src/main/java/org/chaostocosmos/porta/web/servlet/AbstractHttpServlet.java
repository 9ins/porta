package org.chaostocosmos.porta.web.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.io.InputStream;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import org.chaostocosmos.porta.Context;
import org.chaostocosmos.porta.PortaException;
import org.chaostocosmos.porta.properties.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.chaostocosmos.porta.web.HTTP.METHOD;
import org.chaostocosmos.porta.web.HTTP.RESPONSE;
import org.chaostocosmos.porta.web.HTTP.RESPONSE_TYPE;;

public abstract class AbstractHttpServlet extends HttpServlet {

    Context context;
    List<METHOD> methods;
    List<String> paramKeys;
    Credentials credentials;
    Gson gson = new Gson();

    /**
     * Constructor
     * @param context
     * @param methods
     * @param paramKeys
     * @param credentials
     */
    public AbstractHttpServlet(Context context, List<METHOD> methods, List<String> paramKeys, Credentials credentials) {
        super();
        this.context = context;
        this.paramKeys = paramKeys;
        this.credentials = credentials; 
    }

    @Override
    public final void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {        
        Map<Object, Object> map = getParamValueMap(request);                
        try {
            map = toDoGet(request, response, map, this.credentials, this.context.getMessages());
            doResponse(response, map);
        } catch(Exception e) {
            throw new ServletException(e.getMessage());
        }
    } 
    
    @Override
    public final void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {        
        Map<Object, Object> map = getParamValueMap(request);
        try {
            switch(request.getContentType()) {
                case "application/json" :                 
                    map = toDoPostJson(request, response, map, getJson(request), this.context.getCredentials(), this.context.getMessages());
                break;
                case "multipart/formed-data" :
                    String fileName = request.getParameter("filePath");
                    byte[] bytes = new byte[1024];
                    InputStream is = request.getInputStream();
                    File file = new File(fileName);
                    FileOutputStream os = new FileOutputStream(file);
                    int read;
                    while((read = is.read(bytes)) > 0) {
                        os.write(bytes, 0, read);
                    }
                    os.close();
                    map = toDoPostFile(request, response, map, file, this.credentials, this.context.getMessages());
                break;
                default:
                    String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
                    map = toDoPost(request, response, map, body, this.credentials, this.context.getMessages());
            }
            doResponse(response, map);
        } catch(Exception e) {
            throw new ServletException(e.getMessage());
        }
    }

    /**
     * To do response process
     * @param response
     * @param resMap
     * @throws IOException
     */
    private void doResponse(HttpServletResponse response, Map<Object, Object> resMap) throws IOException, PortaException {
        if(resMap == null || resMap.size() == 0) {
            throw new PortaException("ERRCODE004", new Object[]{this.getClass().asSubclass(this.getClass())});
        }
        if(!resMap.containsKey(RESPONSE.CONTENT_TYPE)) {
            throw new PortaException("ERRCODE005", new Object[]{this.getClass().asSubclass(this.getClass())});
        }
        if(!resMap.containsKey(RESPONSE.RESPONSE_CODE)) {
            throw new PortaException("ERRCODE006", new Object[]{this.getClass().asSubclass(this.getClass())});
        }
        String contentType = (String)resMap.remove(RESPONSE.CONTENT_TYPE);
        int responseCode = (Integer)resMap.remove(RESPONSE.RESPONSE_CODE);
        RESPONSE_TYPE type = (RESPONSE_TYPE)resMap.remove(RESPONSE.RESPONSE_TYPE);
        //TEXT, JSON, HTML, XML, YAML, FILE
        Object responseObject = resMap.remove(RESPONSE.RESPONSE_CONTENT);

        if(type == RESPONSE_TYPE.TEXT) {

        } else if(type == RESPONSE_TYPE.JSON) {
            response.getWriter().println(responseObject);
        } else if(type == RESPONSE_TYPE.HTML) {

        } else if(type == RESPONSE_TYPE.XML) {

        } else if(type == RESPONSE_TYPE.YAML) {

        } else if(type == RESPONSE_TYPE.FILE) {

        } else {
            throw new PortaException("ERRCODE007", new Object[]{type.name()});
        }        
        response.setContentType(contentType);
        response.setStatus(responseCode);       
    }

    /**
     * Get parameter value map from request
     * @param request
     * @return
     */
    public Map<Object, Object> getParamValueMap(HttpServletRequest request) {
        Handlers handlers = this.context.getHandlers();
        String subClassName = this.getClass().asSubclass(this.getClass()).getName();
        List<String> paramKeys = handlers.getHandlers()
                                .stream()
                                .map(h -> h.get("servlets"))
                                .flatMap(a -> ((List<Map<String, Object>>)a).stream())
                                .filter(s -> s.get("servletClass")
                                .equals(subClassName))
                                .map(k -> (List<String>)k.get("paramKeys"))
                                .findAny()
                                .orElse(null);
        if(paramKeys != null) {
            Map<Object, Object> map = new HashMap<Object, Object>();
            for(String key : paramKeys) {
                map.put(key, request.getParameter(key));
            }        
            return map;
        } else {
            return null;
        }
    } 

    /**
     * Get json from request body
     * @param request
     * @return
     * @throws IOException
     */
    public String getJson(HttpServletRequest request) throws IOException {
        return request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
    }

    /**
     * Process get request. 
     * It must be overrided at inherited class.
     * It must be returned Map which must be specified 'contentType' and 'resCode'.
     * @param request
     * @param response
     * @param paramValueMap
     * @param credentials
     * @param messages
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public abstract Map<Object, Object> toDoGet(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramValueMap, Credentials credentials, Messages messages) throws Exception;

    /**
     * Process get request. 
     * It must be overrided at inherited class.
     * It must be returned Map which must be specified 'contentType' and 'resCode'.
     * @param request
     * @param response
     * @param body
     * @param paramValueMap
     * @param credentials
     * @param messages
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public abstract Map<Object, Object> toDoPost(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramValueMap, String body, Credentials credentials, Messages messages) throws Exception;

    /**
     * Process get request. 
     * It must be overrided at inherited class.
     * It must be returned Map which must be specified 'contentType' and 'resCode'.
     * @param request
     * @param response
     * @param json
     * @param credentials
     * @param messages
     * @param paramValueMap
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public abstract Map<Object, Object> toDoPostJson(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramValueMap, String json, Credentials credentials, Messages messages) throws Exception;

    /**
     * Process get request. 
     * It must be overrided at inherited class.
     * It must be returned Map which must be specified 'contentType' and 'resCode'.
     * @param request
     * @param response
     * @param file
     * @param credentials
     * @param messages
     * @param paramValueMap
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public abstract Map<Object, Object> toDoPostFile(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramValueMap, File file, Credentials credentials, Messages messages) throws Exception;
}
