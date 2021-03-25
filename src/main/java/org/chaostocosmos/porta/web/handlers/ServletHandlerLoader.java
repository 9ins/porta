package org.chaostocosmos.porta.web.handlers;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.chaostocosmos.porta.Context;
import org.chaostocosmos.porta.Logger;
import org.chaostocosmos.porta.properties.Credentials;
import org.chaostocosmos.porta.web.HTTP.METHOD;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServlet;

/**
 * Servlet & Handler loader class
 */
public class ServletHandlerLoader {

    Logger logger = Logger.getInstance();    
    ClassLoader classLoader;
    private static ServletHandlerLoader loader;

    /**
     * Constructor
     */
    private ServletHandlerLoader() {      
        this.classLoader =  ClassLoader.getSystemClassLoader();
    }

    /**
     * Get loader instance
     * @return
     */
    public static ServletHandlerLoader getInstance() {
        if(loader == null) {
            loader = new ServletHandlerLoader();
        }
        return loader;
    }

    /**
     * Load servlet instance
     * @param className
     * @param context
     * @return
     * @throws ClassNotFoundException
     * @throws NoSuchMethodException
     * @throws SecurityException 
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public HttpServlet loadServlet(String className, Context context, List<METHOD> methods, List<String> paramKeys, Credentials credentials) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> clazz = classLoader.loadClass(className);
        Constructor<?> constructor = clazz.getConstructor(new Class[]{context.getClass(), List.class, List.class, credentials.getClass()});
        return (HttpServlet)constructor.newInstance(context, methods, paramKeys, credentials);
    }

    /**
     * Load handler instnace
     * @param className
     * @param contextPath
     * @param resourceBase
     * @param welcomeFiles
     * @param maxInactiveIntervalSeconds
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public PortaHandler loadHandler(String className, String contextPath, String resourceBase, String[] welcomeFiles, Integer maxInactiveIntervalSeconds) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> clazz = classLoader.loadClass(className);
        Constructor<?> constructor = clazz.getConstructor(new Class[]{contextPath.getClass(), resourceBase.getClass(), welcomeFiles.getClass(), maxInactiveIntervalSeconds.getClass()});
        return (PortaHandler)constructor.newInstance(contextPath, resourceBase, welcomeFiles, maxInactiveIntervalSeconds);
    }

    /**
     * Load filter class
     * @param className
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public Filter loadFilter(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class<?> clazz = classLoader.loadClass(className);
        return (Filter) clazz.getConstructor().newInstance();
    }    
}
