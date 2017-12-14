package com.higherli.asyn_syn_demo;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 多线程队列处理器<BR>
 * 特点：按数据接收顺序处理，但对于同一K值的V数据处理，既保证顺序，也是线程安全的<BR>
 * <B>要求：一个V对象唯一对应一个键值</B><BR>
 * 功能类似扩展线程的处理，实现参考引擎的ServerWriter
 */
public class MultiThreadQueueWorker<K, V> implements IControl {
	private final LinkedBlockingQueue<K> keyQueue = new LinkedBlockingQueue<K>();
	private final ConcurrentHashMap<K, ValueQueueWrap<V>> key_ValueQueueWrap = new ConcurrentHashMap<K, ValueQueueWrap<V>>();
	protected final String name;
	private final Thread[] workers;
	private volatile boolean running;
	private final int warnSize;
	private final IProcessor<K, V> processor;
	// IO监控线程
	private final boolean enableIOMonitor;
	private final ScheduledExecutorService monitor;
	private AtomicLong inputCount;
	private AtomicLong ouputCount;

	public MultiThreadQueueWorker(String name, int threadNum, int warnSize, boolean enableIOMonitor, double warnIORate, long printIOPeriod, IProcessor<K, V> processor) {
		this.name = name;
		workers = new Thread[threadNum];
		running = false;
		this.warnSize = warnSize;
		this.processor = processor;
		for (int i = 0; i < workers.length; i++) {
			String threadName = String.format("%s-Pool%d-Th%d", name, threadNum, i);
			workers[i] = new Thread(new WorkRunnable(), threadName);
			workers[i].setDaemon(true);
		}
		// IO监控
		this.enableIOMonitor = enableIOMonitor;
		if (enableIOMonitor) {
			monitor = Executors.newSingleThreadScheduledExecutor();
			monitor.scheduleAtFixedRate(new PrintIORunnable(warnIORate), 100L, printIOPeriod, TimeUnit.MILLISECONDS);
			inputCount = new AtomicLong(0);
			ouputCount = new AtomicLong(0);
		} else {
			monitor = null;
			inputCount = ouputCount = null;
		}
	}

	public MultiThreadQueueWorker(String name, int threadNum, int warnSize, double warnIORate, IProcessor<K, V> processor) {
		this(name, threadNum, warnSize, true, warnIORate, 10000L, processor);
	}

	public MultiThreadQueueWorker(String name, int threadNum, int warnSize, IProcessor<K, V> processor) {
		this(name, threadNum, warnSize, false, 0, 0, processor);
	}

	/**
	 * 只能开启一次，关闭后再开会抛异常
	 */
	public synchronized void start() {
		if (!running) {
			running = true;
			for (int i = 0; i < workers.length; i++) {
				workers[i].start();
			}
		}
	}

	public synchronized void shutdown() {
		if (running) {
			running = false;
			for (int i = 0; i < workers.length; i++) {
				workers[i].interrupt();
			}
			if (enableIOMonitor && !monitor.isShutdown()) {
				monitor.shutdown();
			}
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void accept(K key, V value) {
		ValueQueueWrap<V> valueQueueWrap = key_ValueQueueWrap.get(key);
		if (valueQueueWrap == null) {
			valueQueueWrap = new ValueQueueWrap<V>();
			ValueQueueWrap<V> tmp = key_ValueQueueWrap.putIfAbsent(key, valueQueueWrap);
			if (tmp != null) {
				valueQueueWrap = tmp;
			}
		}
		boolean add = true;
		synchronized (valueQueueWrap) {
			int size = valueQueueWrap.valueQueue.size();
			if (size == 0 && !valueQueueWrap.hasBeenAppendedToKeyQueue) {
				if (keyQueue.offer(key)) {
					valueQueueWrap.valueQueue.addLast(value);
					valueQueueWrap.hasBeenAppendedToKeyQueue = true;
				} else {
					add = false;
					System.err.println(String.format("<MultiThreadQueueWorker> Workers[%s] offer fail while accept. key:%s", name, key));
				}
			} else {
				if (size > 0 && size % warnSize == 0) {
					System.err.println(String.format("<MultiThreadQueueWorker> Workers[%s] queue exceed warn size. key:%s size:%d", name, key, size));
				}
				valueQueueWrap.valueQueue.addLast(value);
			}
		}
		if (add && enableIOMonitor) {
			inputCount.incrementAndGet();
		}
	}

	public void remove(K key) {
		key_ValueQueueWrap.remove(key);
	}

	private void process() {
		while (running) {
			try {
				K key = (K) keyQueue.take();
				ValueQueueWrap<V> valueQueueWrap = key_ValueQueueWrap.get(key);
				if (valueQueueWrap != null) {
					boolean dec = false;
					V value = null;
					synchronized (valueQueueWrap) {
						value = valueQueueWrap.valueQueue.poll();
					}
					if (value != null) {
						dec = true;
						processor.process(key, value);
					}
					synchronized (valueQueueWrap) {
						// 这里才重置hasAddedToKeyQueue，保证对K的处理是单线程的
						valueQueueWrap.hasBeenAppendedToKeyQueue = false;
						if (valueQueueWrap.valueQueue.size() > 0) {
							if (keyQueue.offer(key)) {
								valueQueueWrap.hasBeenAppendedToKeyQueue = true;
							} else {
								System.err.println(String.format("<MultiThreadQueueWorker> Workers[%s] offer fail after process.", name));
							}
						}
					}
					if (dec && enableIOMonitor) {
						ouputCount.incrementAndGet();
					}
				}
			} catch (Throwable t) {
				if (running) {
					System.err.println(String.format("<MultiThreadQueueWorker> Workers[%s] work process error.", name));
				}
			}
		}
	}

	private class WorkRunnable implements Runnable {
		public void run() {
			process();
		}
	}

	public interface IProcessor<K, V> {
		public void process(K key, V value);
	}

	private static class ValueQueueWrap<V> {
		public final LinkedList<V> valueQueue;
		public volatile boolean hasBeenAppendedToKeyQueue;

		public ValueQueueWrap() {
			this.valueQueue = new LinkedList<V>();
			this.hasBeenAppendedToKeyQueue = false;
		}
	}

	private class PrintIORunnable implements Runnable {

		private final double warnIORate;

		public PrintIORunnable(double warnIORate) {
			this.warnIORate = warnIORate;
		}

		public void run() {
			try {
				long _inputCount = inputCount.get();
				long _outputCount = ouputCount.get();
				double ioRate = _outputCount == 0 ? 0 : (double) _inputCount * 100 / _outputCount;
				String warnMsg = String.format("<MultiThreadQueueWorker> Workers[%s] io:%d/%d/%g%%", name, _inputCount, _outputCount, ioRate);
				System.out.println(warnMsg);
				if (ioRate > warnIORate) {
					System.err.println(warnMsg);
				}
			} catch (Throwable t) {
				System.err.println(String.format("<MultiThreadQueueWorker> Workers[%s] print io error.", name));
			}
		}

	}

}
