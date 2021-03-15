package org.chaostocosmos.porta;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PortaThreadPool extends ThreadPoolExecutor implements RejectedExecutionHandler {

    Logger logger = Logger.getInstance();

    private static PortaThreadPool proxyThreadPool = null;

    public PortaThreadPool(int coreSize, int maxSize, int idleSecond, int queueSize) {
        super(coreSize, maxSize, idleSecond, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));
        setRejectedExecutionHandler(this);
    } 

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		logger.fatal("[" + r.toString()
				+ "] Session Task is rejected. Please check ThreadPool configuration. ThreadPool active count: "
				+ executor.getActiveCount() + "  Task count: " + executor.getTaskCount() + "  ThreadPool Maximun: "
				+ executor.getMaximumPoolSize() + "  Queue size : " + executor.getQueue().size());
	}
}
