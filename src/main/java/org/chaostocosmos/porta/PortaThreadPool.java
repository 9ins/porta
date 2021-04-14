package org.chaostocosmos.porta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class PortaThreadPool extends ThreadPoolExecutor implements RejectedExecutionHandler {	

    Logger logger = Logger.getInstance();

	List<ThreadExceptionListener> exceptionListeners = new ArrayList<>();

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

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		if(t != null) {
			for(ThreadExceptionListener threadExceptionListener : this.exceptionListeners) {
				threadExceptionListener.receiveException(r, t);
			}
		}
	}

	public void addExceptionListener(ThreadExceptionListener exceptionListener) {
		this.exceptionListeners.add(exceptionListener);
	}

	interface ThreadExceptionListener {
		public void receiveException(Runnable r, Throwable t);
	}
}
