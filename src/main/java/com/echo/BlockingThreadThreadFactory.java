package com.echo;

/**
 * @author yucheng
 * @description
 * @create 2020-10-26 5:56 下午
 */

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 内置线程池ThreadFactory
 */
public class BlockingThreadThreadFactory implements ThreadFactory {
	/**
	 * 线程池分组
	 */
	private ThreadGroup threadGroup;

	/**
	 * 线程前缀
	 */
	private String threadNamePrefix;

	/**
	 * 线程池线程总数量
	 */
	private AtomicLong threadCount = new AtomicLong(0L);

	/**
	 * 线程池线程优先级
	 */
	private int priority;

	/**
	 * 线程池线程是否为守护线程
	 */
	private boolean daemon;

	public BlockingThreadThreadFactory(String threadNamePrefix) {
		this(threadNamePrefix, "blocking-thread-group-name");
	}

	public BlockingThreadThreadFactory(String threadNamePrefix, String threadGroupName) {
		this(threadNamePrefix, threadGroupName, Thread.NORM_PRIORITY);
	}

	public BlockingThreadThreadFactory(String threadNamePrefix, String threadGroupName, int priority) {
		this(threadNamePrefix, threadGroupName, priority, false);
	}

	public BlockingThreadThreadFactory(String threadNamePrefix, String threadGroupName, int priority, boolean daemon) {
		this.threadNamePrefix = threadNamePrefix;
		this.priority = priority;
		this.daemon = daemon;
		this.threadGroup = new ThreadGroup(threadGroupName);
	}

	@Override
	public Thread newThread(Runnable runnable) {
		Thread thread = new Thread(threadGroup, runnable, nextThreadName());
		thread.setPriority(priority);
		thread.setDaemon(daemon);
		return thread;
	}

	private String nextThreadName() {
		return this.threadNamePrefix + this.threadCount.incrementAndGet();
	}
}
