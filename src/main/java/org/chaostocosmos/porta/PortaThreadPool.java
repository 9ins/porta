package org.chaostocosmos.porta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.chaostocosmos.porta.PortaSession.ChannelWorker;

/**
 * PortaThreadPool class
 */
public class PortaThreadPool extends ThreadPoolExecutor implements RejectedExecutionHandler {	

    Logger logger = Logger.getInstance();
	List<PortaThreadPoolExceptionHandler> exceptionHandlers = new ArrayList<>();
	List<PortaThreadPoolResultHandler> resultHandlers = new ArrayList<>();

	/**
	 * Constructor
	 * @param coreSize
	 * @param maxSize
	 * @param idleSecond
	 * @param queueSize
	 */
    public PortaThreadPool(int coreSize, int maxSize, int idleSecond, int queueSize) {
        super(coreSize, maxSize, idleSecond, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(queueSize));
    } 

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		logger.fatal("[" + r.toString() + "] Session Task is rejected. Please check ThreadPool configuration. ThreadPool active count: " + executor.getActiveCount() + "  Task count: " + executor.getTaskCount() + "  ThreadPool Maximun: " + executor.getMaximumPoolSize() + "  Queue size : " + executor.getQueue().size());
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		if(t == null) {
			ChannelWorker worker = (ChannelWorker)r;		
			for(PortaThreadPoolResultHandler resultHandler : this.resultHandlers) {
				resultHandler.handleResult(worker);
			}
		} else {
			for(PortaThreadPoolExceptionHandler exceptionHandler : this.exceptionHandlers) {				
				exceptionHandler.handleException(t);
			}	
		}
	}

	/**
	 * Add result handler
	 * @param resultHandler
	 */
	public void addResultHandler(PortaThreadPoolResultHandler resultHandler) {
		this.resultHandlers.add(resultHandler);
	}

	/**
	 * Add exception handler
	 * @param exceptionHandler
	 */
	public void addExceptionHandler(PortaThreadPoolExceptionHandler exceptionHandler) {
		this.exceptionHandlers.add(exceptionHandler);
	}
}
