package org.chaostocosmos.porta;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import org.chaostocosmos.porta.properties.Configs;
import org.chaostocosmos.porta.properties.PropertiesHelper;
import org.chaostocosmos.porta.properties.SessionMappingConfigs;

public class ResourceManager implements IResourceUsage, ISessionStatus{

    private class ProbeTask extends TimerTask {
        @Override
        public void run() {
            try {
            } catch (Exception e) {
                Logger.getInstance().throwable(e);
            }
        } 
    }

    PortaApp portaApp; 
    Context context;
    Timer probeTimer;
    long statisticsProbeMillis;
    
    /**
     * ResourceManager 
     * @param portaApp
     */
    public ResourceManager(PortaApp portaApp, Context context) {
        this.portaApp = portaApp;
        this.context = context;
        this.statisticsProbeMillis = PropertiesHelper.getInstance().getConfigs().getAppConfigs().getStatisticsProbeMillis();
        this.probeTimer = new Timer();
        this.probeTimer.schedule(new ProbeTask(), this.statisticsProbeMillis, this.statisticsProbeMillis);
    }

    /**
     * Get system resource map
     * @param type
     * @param unit
     * @return
     * @throws Exception
     */
    public Map<Object, Object> getResourceUsage(RESOURCE type, UNIT unit, Object... params) throws Exception {
        switch(type) {
            case CPU:
            return getCpuUsage(unit);
            case MEMORY:
            return getMemoryUsage(unit);
            case THREAD:
            return getThreadPoolUsage(unit);
            case SESSION_INFO:
            return getSessionInfo(params[0]+"");
            case SESSIONS_INFO:
            return getSessionsInfo();
            case SESSION_SIMPLE:
            return getSessionSimple();
            case SESSION_USAGE:
            return getSessionUsage(params[0]+"");
            case SESSIONS_USAGE:
            return getSessionsUsage();
            case SESSION_THROUGHPUT:
            return getSessionThroughput(params[0]+"");
            case SESSIONS_THROUGHPUT:
            return getSessionsThroughput();
            default:
            throw new PortaException("ERRCODE008", new Object[]{type});
        }       
    }    

    @Override
    public Map<Object, Object> getSessionInfo(String sessionName) throws Exception {
        SessionMappingConfigs sessionMapping = this.portaApp.getContext().getConfigs().getSessionMappingConfigs(sessionName);        
        return sessionMapping.getSessionMappingMap();
    }

    @Override
    public Map<Object, Object> getSessionsInfo() throws Exception {
        Map<String, SessionMappingConfigs> sessionMap = this.portaApp.getContext().getConfigs().getSessionMapping();
        return sessionMap.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), v -> v.getValue()));        
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
    public Map<Object, Object> getCpuUsage(UNIT unit) throws Exception {
        Map<Object, Object> map = new LinkedHashMap<>();
        map.put("type", RESOURCE.CPU);
        map.put("unit", unit);
        map.put("cpuLoad", SystemMonitor.getProcessCpuLoad(unit));
        map.put("cpuTime", SystemMonitor.getProcessCpuTime(unit));
        map.put("systemCpuLoad", SystemMonitor.getSystemCpuLoad(unit));    
        return map;
    }

    @Override
    public Map<Object, Object> getMemoryUsage(UNIT unit) throws Exception {
        Map<Object, Object> map = new LinkedHashMap<>();
        float totalPhysicalMemory = SystemMonitor.getTotalPhysicalMemorySize(unit);
        float freePhysicalMemory = SystemMonitor.getFreePhysicalMemorySize(unit);
        float usedPhysicalMemory = totalPhysicalMemory - freePhysicalMemory;
        float maxHeapMemory = SystemMonitor.getProcessHeapMax(unit);
        float usedHeapMemory = SystemMonitor.getProcessHeapUsed(unit);
        float freeHeapMemory = maxHeapMemory - usedHeapMemory;
        float usedMemory = SystemMonitor.getProcessMemoryUsed(unit);
        map.put("systemTotal", totalPhysicalMemory);
        map.put("systemFree", freePhysicalMemory);
        map.put("systemUsed", usedPhysicalMemory);
        map.put("heapMax", maxHeapMemory);
        map.put("heapUsed", usedHeapMemory);
        map.put("heapFree", freeHeapMemory);
        map.put("memoryUsed", usedMemory);                
        return map;
    }

    @Override
    public Map<Object, Object> getThreadPoolUsage(UNIT unit) throws Exception {
        Map<Object, Object> map = new LinkedHashMap<>();
        int activeCount = this.portaApp.getPortaThreadPool().getActiveCount();
        int corePoolSize = this.portaApp.getPortaThreadPool().getCorePoolSize();
        int largestPoolSize = this.portaApp.getPortaThreadPool().getLargestPoolSize();
        int maxinumPoolSize = this.portaApp.getPortaThreadPool().getMaximumPoolSize();
        int threadPoolLimitSize = PropertiesHelper.getInstance().getConfigs().getThreadPoolConfigs().getThreadPoolLimitSize();
        long completedTaskCount = this.portaApp.getPortaThreadPool().getCompletedTaskCount();
        long taskCount = this.portaApp.getPortaThreadPool().getTaskCount();
        int queueSize = this.portaApp.getPortaThreadPool().getQueue().size();
        map.put("activeCount", activeCount);
        map.put("corePoolSize", corePoolSize);
        map.put("largestPoolSize", largestPoolSize);
        map.put("maxinumPoolSize", maxinumPoolSize);
        map.put("limitPoolSize", threadPoolLimitSize);
        map.put("completedTaskCount", completedTaskCount);
        map.put("taskCount", taskCount);
        map.put("queueSize", queueSize);
        return map;
    }

    @Override
    public void setCorePoolSize(int size) throws Exception {
        this.portaApp.getPortaThreadPool().setCorePoolSize(size);
    }

    @Override
    public void setMaximumPoolSize(int size) throws Exception {
        this.portaApp.getPortaThreadPool().setMaximumPoolSize(size);
    }

    @Override    
    public Map<Object, Object> getSessionSimple() throws Exception {
        Configs config = PropertiesHelper.getInstance().getConfigs();
        Map<String, SessionMappingConfigs> sessionMap = config.getSessionMapping();
        Map<Object, Object> map = sessionMap.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), v -> v.getValue()));        
        return map;
    }

    @Override
    public void applySessionInfo(String sessionName, Map<Object, Object> sessionInfoMap) throws Exception {
        PropertiesHelper propertiesHelper = PropertiesHelper.getInstance();
        SessionMappingConfigs sessionMappingConfig = propertiesHelper.getConfigs().getSessionMappingConfigs(sessionName);
        sessionMappingConfig.setSessionMappingMap(sessionInfoMap);
        PortaSessionHandler sessionHandler = this.portaApp.getPortaSessionHandler();
        sessionHandler.closeSession(sessionName);
        PortaSession portaSession = sessionHandler.createProxySession(sessionName, sessionMappingConfig);
        portaSession.start();
        saveSessionInfo(sessionName, sessionInfoMap);
    }

    @Override
    public void saveSessionInfo(String sessionName, Map<Object, Object> sessionInfoMap) throws Exception {
        PropertiesHelper propertiesHelper = PropertiesHelper.getInstance();
        propertiesHelper.getConfigs().getSessionMappingConfigs(sessionName).setSessionMappingMap(sessionInfoMap);
        propertiesHelper.dumpConfigs();
    }

    @Override
    public void applySessionInfos(Map<String, Map<Object, Object>> sessionsInfoMap) throws Exception {
        for(Map.Entry<String, Map<Object, Object>> entry : sessionsInfoMap.entrySet()) {
            String sessionName = entry.getKey();
            SessionMappingConfigs sessionMappingConfig = PropertiesHelper.getInstance().getConfigs().getSessionMappingConfigs(sessionName);
            sessionMappingConfig.setSessionMappingMap(entry.getValue());                
            PortaSessionHandler sessionHandler = this.portaApp.getPortaSessionHandler();
            sessionHandler.closeSession(sessionName);
            PortaSession portaSession = sessionHandler.createProxySession(sessionName, sessionMappingConfig);
            portaSession.start();
        }
        saveSessionInfos(sessionsInfoMap);
    }

    @Override
    public void saveSessionInfos(Map<String, Map<Object, Object>> sessionsInfoMap) throws Exception {
        PropertiesHelper propertiesHelper = PropertiesHelper.getInstance();
        for(Map.Entry<String, Map<Object, Object>> entry : sessionsInfoMap.entrySet()) {
            propertiesHelper.getConfigs().getSessionMappingConfigs(entry.getKey()).setSessionMappingMap(entry.getValue());
        }
        propertiesHelper.dumpConfigs();
    }
}
