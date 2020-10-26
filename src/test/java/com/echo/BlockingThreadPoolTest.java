package com.echo;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author yucheng
 * @description
 * @create 2020-10-23 3:46 下午
 */

public class BlockingThreadPoolTest {
	private static Logger logger = LoggerFactory.getLogger(BlockingThreadPoolTest.class);

	private static BlockingThreadPool blockingThreadPool;
	private static AtomicLong counter = new AtomicLong(0);

	@BeforeClass
	public static void beforeClass() {
		blockingThreadPool = new BlockingThreadPool("blocking-thread-pool-", 5, 1, TimeUnit.MINUTES);
	}

	@Test
	public void testExecute() {
		for (int i = 0; i < 1000; ++i) {
			logger.info("Producer msg: {}", i);
			blockingThreadPool.execute(new BlockingThread(String.format("%s", i)));
		}
		logger.info("Thread finished");
	}

	public static class BlockingThread implements Runnable {
		private final String msg;

		public BlockingThread(String msg) {
			this.msg = msg;
		}

		@Override
		public void run() {
			logger.info("Execute msg: {}", msg);
			try {
				TimeUnit.SECONDS.sleep(new Random().nextInt(5));
			} catch (InterruptedException e) {
				logger.warn("Sleep failure", e);
			}
			logger.info("Execute msg: {} finish", msg);
		}

		@Override
		public String toString() {
			return "BlockingThread{" +
					"msg='" + msg + '\'' +
					'}';
		}
	}

}
