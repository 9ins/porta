package org.chaostocosmos.porta.web.servlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.chaostocosmos.porta.Context;
import org.chaostocosmos.porta.properties.PropertiesHelper;
import org.chaostocosmos.porta.web.HTTP.METHOD;
import org.chaostocosmos.porta.properties.Credential;
import org.chaostocosmos.porta.properties.Credentials;
import org.chaostocosmos.porta.properties.Grant;
import org.chaostocosmos.porta.properties.Messages;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginServlet extends AbstractHttpServlet {

    public LoginServlet(Context context, List<METHOD> methods, List<String> paramKeys, Credentials credentials) {
        super(context, methods, paramKeys, credentials);
    }

    public Credential signIn(String user, String password) {
        PropertiesHelper configHelper = super.context.getPropertiesHelper();
        if(configHelper.isSuperUser(user, password)) {
            List<String> grants = new ArrayList<String>();
            grants.add(Grant.SUPER.name());
            return new Credential(user, password, grants); 
        } else {
            Credential credential = configHelper.isValidUser(user, password);
            if(credential != null) {
                return credential;
            }
        }
        return null;
    }

    @Override
    public Map<Object, Object> toDoGet(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramValueMap, Credentials credentials, Messages messages) throws ServletException, IOException {
        /*
       Map<String, String> reqMap = super.gson.fromJson(request.getReader(), Map.class);
                Logger.getInstance().info("REQ MAP: "+reqMap);
                String user = reqMap.get("username");
                String password = reqMap.get("password"); 
                Credential userCredential = signIn(user, password);
                if(userCredential == null) {
                    Logger.getInstance().info("LOG-IN FAILED......");
                    //response.setContentType("application/json");
                    //response.getWriter().println("{ \"status\": \"false\", \"redirect\": \"/\", \"message\": \"Username or password isn't collect.\"}");
                    //response.setStatus(HttpServletResponse.SC_OK);    
                } else {
                    Logger.getInstance().info("LOG-IN SUCCESSED......");
                    //response.setContentType("application/json");
                    //response.getWriter().println("{ \"status\": \"true\", \"redirect\": \"/\", \"message\": \"Sign-In Success!!!\"}");
                    //response.setStatus(HttpServletResponse.SC_OK);    
                }
                */
        return null;
    }

    @Override
    public Map<Object, Object> toDoPost(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramMap, String body, Credentials credentials, Messages messages) throws ServletException, IOException {
        return null;
    }

    @Override
    public Map<Object, Object> toDoPostJson(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramMap,  String json, Credentials credentials, Messages messages) throws ServletException, IOException {
        return null;
    }

    @Override
    public Map<Object, Object> toDoPostFile(HttpServletRequest request, HttpServletResponse response, Map<Object, Object> paramMap,  File file, Credentials credentials, Messages messages) throws ServletException, IOException {
        return null;
    }
}
