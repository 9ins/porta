package org.chaostocosmos.porta;

import java.util.Map;

import org.chaostocosmos.porta.web.ManagementServer;

/**
 * ModuleProvider class
 */
public class ModuleProvider {

    private static PortaApp portaMain;
	private static Context context;
	private static PortaSessionHandler portaSessionHandler;
	private static Map<String, PortaSession> portaSessionMap;
	private static PortaThreadPool portaThreadPool;
	private static ManagementServer managementServer;
    private static ResourceManager resourceManager;

    /**
     * Constructor
     * @param portaMain_
     */
    public ModuleProvider(PortaApp portaMain_) {
        portaMain = portaMain_;
        context = portaMain.getContext();
        portaSessionHandler = portaMain.getPortaSessionHandler();
        portaSessionMap = portaSessionHandler.getPortaSessionMap();
        portaThreadPool = portaMain.getPortaThreadPool();
        managementServer = portaMain.getManagementServer();
        resourceManager = portaMain.getResourceManager(); 
    }

    public static PortaApp getPortaMain() {
        return portaMain;
    }    

    public static Context getContext() {
        return context;
    }

    public static PortaSessionHandler getPortaSessionHandler() {
        return portaSessionHandler;
    }

    public static Map<String, PortaSession> getPortaSessionMap() {
        return portaSessionMap;
    }

    public static PortaThreadPool getPortaThreadPool() {
        return portaThreadPool;
    }

    public static ManagementServer getManagementServer() {
        return managementServer;
    }

    public static ResourceManager getResourceManager() {
        return resourceManager;
    }
}
