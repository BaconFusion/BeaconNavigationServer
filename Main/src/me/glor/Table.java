package me.glor;

import java.util.*;

/**
 * A Table that holds Object with a time to live.
 *
 * @param <T> Contained Object type must {@link Comparable} interface
 * @author glor
 */
public class Table<T extends Comparable<T>> implements Iterable<T> {
	// time in millis how long an object is to remain in the table
	public final long TTL;
	private LinkedList<Thread> callbacks = new LinkedList<>();
	private SortedMap<T, Long> container = new TreeMap<>();

	/**
	 * Default TTL (Time To Live) is 2000ms
	 */
	public Table() {
		TTL = 2000;
	}

	/**
	 * Custom TTL Constructor
	 *
	 * @param TTL custom Time To Live in ms
	 */
	public Table(int TTL) {
		this.TTL = TTL;
	}

	/**
	 * Update Objects in Table, ttl of missing objects will be decremented
	 *
	 * @param collection Collection of Objects that should be updated or added to the Table
	 */
	public void update(Collection<T> collection) {
		long time = System.currentTimeMillis();
		synchronized (container) {
			for (T t : collection) {
				container.remove(t);
				container.put(t, time);
			}
		}
		tick(time);
	}

	public long getTTL(T t) {
		return container.get(t);
	}

	/**
	 * Stringify
	 *
	 * @return Space separated concatenated results of contents {@code toString()} method
	 */
	public String toString() {
		String string = "Table contains: ";
		for (T t : container.keySet()) {
			string += t + " ";
		}
		return string;
	}

	public Collection<T> toCollection() {
		synchronized (container) {
			ArrayList<T> collection = new ArrayList<T>(container.size());
			collection.addAll(container.keySet());
			return collection;
		}
	}

	/**
	 * Iterate over copy of currently held Objects
	 *
	 * @return
	 */
	@Override
	public Iterator<T> iterator() {
		return toCollection().iterator();
	}

	/**
	 * Register a thread that will be started on every update.
	 * Still running treads won't be called.
	 *
	 * @param thread Callback Thread
	 */
	public void addCallback(Thread thread) {
		callbacks.add(thread);
	}

	/**
	 * Let Objects age and invoke callbacks
	 */
	private void tick(long time) {
		synchronized (container) {
			// foreach key let it age or remove it
			Iterator<T> iterator = container.keySet().iterator();
			while (iterator.hasNext()) {
				T t = iterator.next();
				long ttl = container.get(t);
				if (time-ttl > TTL) {
					iterator.remove();
				}
			}
		}
		for (Thread thread : callbacks) {
			if (thread.getState() == Thread.State.NEW) {
				thread.start();
			} else if (thread.getState() == Thread.State.WAITING) {
				//noinspection deprecation
				synchronized (thread) {
					thread.notify();
				}
			} else
				System.err.println("Thread not ready: " + thread.getState());
		}
	}
}
