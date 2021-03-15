package org.chaostocosmos.porta.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import org.chaostocosmos.porta.Context;
import org.chaostocosmos.porta.Logger;
import org.chaostocosmos.porta.PortaMain;
import org.chaostocosmos.porta.properties.PropertiesHelper;
import org.chaostocosmos.porta.properties.Credential;
import org.chaostocosmos.porta.properties.Grant;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginServlet extends HttpServlet {

    PortaMain tcpProxy;
    Gson gson = new Gson();

    public LoginServlet(PortaMain tcpProxy) {
        this.tcpProxy = tcpProxy;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> reqMap = gson.fromJson(request.getReader(), Map.class);
        Logger.getInstance().info("REQ MAP: "+reqMap);
        String user = reqMap.get("username");
        String password = reqMap.get("password"); 
        Credential userCredential = signIn(user, password);
        if(userCredential == null) {
            Logger.getInstance().info("LOG-IN FAILED......");
            response.setContentType("application/json");
            response.getWriter().println("{ \"status\": \"false\", \"redirect\": \"/\", \"message\": \"Username or password isn't collect.\"}");
            response.setStatus(HttpServletResponse.SC_OK);    
        } else {
            Logger.getInstance().info("LOG-IN SUCCESSED......");
            response.setContentType("application/json");
            response.getWriter().println("{ \"status\": \"true\", \"redirect\": \"/\", \"message\": \"Sign-In Success!!!\"}");
            response.setStatus(HttpServletResponse.SC_OK);    
        }
    }

    public Credential signIn(String user, String password) {
        PropertiesHelper configHelper = Context.propertiesHelper;  
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
}
