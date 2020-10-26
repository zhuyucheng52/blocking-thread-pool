package com.echo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 消费者线程池
 * @author yucheng
 * @description
 * @create 2020-10-23 3:05 下午
 */

public class BlockingThreadPool implements ExecutorService {
	private static final Logger logger = LoggerFactory.getLogger(BlockingThreadPool.class);
	/**
	 * 内置JDK线程池
	 */
	private ThreadPoolExecutor executor;

	/**
	 * 线程池阻塞队列
	 */
	private BlockingQueue<Runnable> blockingQueue;

	public BlockingThreadPool(String threadNamePrefix, int corePoolSize, long keepAliveTime, TimeUnit unit) {
		this.blockingQueue = new ArrayBlockingQueue<Runnable>(corePoolSize);
		this.executor = new ThreadPoolExecutor(corePoolSize,
				corePoolSize,
				keepAliveTime,
				unit,
				blockingQueue,
				new BlockingThreadThreadFactory(threadNamePrefix));
	}

	@Override
	public void execute(Runnable command) {
		try {
			blockingQueue.put(command);
			executor.prestartCoreThread();
			logger.debug("Thread: {} pushed into thread pool", command);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void shutdown() {
		executor.shutdown();
	}

	@Override
	public List<Runnable> shutdownNow() {
		return executor.shutdownNow();
	}

	@Override
	public boolean isShutdown() {
		return executor.isShutdown();
	}

	@Override
	public boolean isTerminated() {
		return executor.isTerminated();
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		return executor.awaitTermination(timeout, unit);
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return executor.submit(task);
	}

	@Override
	public <T> Future<T> submit(Runnable task, T result) {
		return executor.submit(task, result);
	}

	@Override
	public Future<?> submit(Runnable task) {
		return executor.submit(task);
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
		return executor.invokeAll(tasks);
	}

	@Override
	public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
		return executor.invokeAll(tasks, timeout, unit);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
		return executor.invokeAny(tasks);
	}

	@Override
	public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return executor.invokeAny(tasks, timeout, unit);
	}

	public void setCorePoolSize(int corePoolSize) {
		executor.setCorePoolSize(corePoolSize);
	}

	public void setKeepAliveTime(long keepAliveTime, TimeUnit unit) {
		executor.setKeepAliveTime(keepAliveTime, unit);
	}

}
