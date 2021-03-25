package org.chaostocosmos.porta;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

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

    PortaMain portaMain;
    Timer probeTimer;
    long statisticsProbeMillis;
    
    Map<String, Object> resourceMap;

    public ResourceManager(PortaMain portaMain) {
        this.portaMain = portaMain;
        this.resourceMap = new LinkedHashMap<>();        
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
        SessionMappingConfigs sessionMapping = this.portaMain.getContext().getConfigs().getSessionMappingConfigs(sessionName);        
        return null;
    }

    @Override
    public Map<Object, Object> getSessionsInfo() throws Exception {
        this.portaMain.getContext().getConfigs().getSessionMapping();
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
    public Map<Object, Object> getCpuUsage(UNIT unit) throws Exception {
        Map<Object, Object> map = new LinkedHashMap<>();
        map.put("type", RESOURCE.CPU);
        map.put("unit", unit);
        map.put("CpuLoad", SystemMonitor.getProcessCpuLoad(unit));
        map.put("CpuTime", SystemMonitor.getProcessCpuTime(unit));
        map.put("SystemCpuLoad", SystemMonitor.getSystemCpuLoad(unit));    
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
        map.put("SystemTotal", totalPhysicalMemory);
        map.put("SystemFree", freePhysicalMemory);
        map.put("SystemUsed", usedPhysicalMemory);
        map.put("HeapMax", maxHeapMemory);
        map.put("HeapUsed", usedHeapMemory);
        map.put("HeapFree", freeHeapMemory);
        map.put("MemoryUsed", usedMemory);                
        return map;
    }

    @Override
    public Map<Object, Object> getThreadPoolUsage(UNIT unit) throws Exception {
        Map<Object, Object> map = new LinkedHashMap<>();
        int activeCount = this.portaMain.getPortaThreadPool().getActiveCount();
        int corePoolSize = this.portaMain.getPortaThreadPool().getCorePoolSize();
        int largestPoolSize = this.portaMain.getPortaThreadPool().getLargestPoolSize();
        int MaxinumPoolSize = this.portaMain.getPortaThreadPool().getMaximumPoolSize();
        long completedTaskCount = this.portaMain.getPortaThreadPool().getCompletedTaskCount();
        long taskCount = this.portaMain.getPortaThreadPool().getTaskCount();
        int queueSize = this.portaMain.getPortaThreadPool().getQueue().size();
        map.put("activeCount", activeCount);
        map.put("corePoolSize", corePoolSize);
        map.put("largestPoolSize", largestPoolSize);
        map.put("MaxinumPoolSize", MaxinumPoolSize);
        map.put("completedTaskCount", completedTaskCount);
        map.put("taskCount", taskCount);
        map.put("queueSize", queueSize);            
        System.out.println(map.toString());
        return map;
    }
}
