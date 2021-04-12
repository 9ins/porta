package org.chaostocosmos.porta.properties;

/**
 * ThreadPoolConfigs
 * 
 */
public class ThreadPoolConfigs {
    int threadPoolCoreSize;
    int threadPoolMaxSize;
    int threadPoolLimitSize;
    int threadPoolIdleSecond;
    int threadPoolQueueSize;    
    
    public ThreadPoolConfigs() {}

    public int getThreadPoolCoreSize() {
        return this.threadPoolCoreSize;
    }

    public void setThreadPoolCoreSize(int threadPoolCoreSize) {
        this.threadPoolCoreSize = threadPoolCoreSize;
    }

    public int getThreadPoolMaxSize() {
        return this.threadPoolMaxSize;
    }

    public int getThreadPoolLimitSize() {
        return this.threadPoolLimitSize;
    }

    public void setThreadPoolLimitSize(int threadPoolLimitSize) {
        this.threadPoolLimitSize = threadPoolLimitSize;
    }

    public void setThreadPoolMaxSize(int threadPoolMaxSize) {
        this.threadPoolMaxSize = threadPoolMaxSize;
    }

    public int getThreadPoolIdleSecond() {
        return this.threadPoolIdleSecond;
    }

    public void setThreadPoolIdleSecond(int threadPoolIdleSecond) {
        this.threadPoolIdleSecond = threadPoolIdleSecond;
    }

    public int getThreadPoolQueueSize() {
        return this.threadPoolQueueSize;
    }

    public void setThreadPoolQueueSize(int threadPoolQueueSize) {
        this.threadPoolQueueSize = threadPoolQueueSize;
    }

    @Override
    public String toString() {
        return "{" +
            " threadPoolCoreSize='" + threadPoolCoreSize + "'" +
            ", threadPoolMaxSize='" + threadPoolMaxSize + "'" +
            ", threadPoolLimitSize='" + threadPoolLimitSize + "'" +
            ", threadPoolIdleSecond='" + threadPoolIdleSecond + "'" +
            ", threadPoolQueueSize='" + threadPoolQueueSize + "'" +
            "}";
    }
}
