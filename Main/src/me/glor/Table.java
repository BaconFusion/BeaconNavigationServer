package me.glor;

import java.util.*;

/**
 * A Table that holds Object with a time to live.
 * Time is based on ticks. Updating the table takes one tick.
 *
 * @param <T> Contained Object type must {@link Comparable} interface
 * @author glor
 */
public class Table<T extends Comparable<T>> implements Iterable<T> {
	public final int TTL;
	private LinkedList<Thread> callbacks = new LinkedList<>();
	private TreeMap<T, Integer> container = new TreeMap<>();

	/**
	 * Default TTL (Time To Live) is 3
	 */
	public Table() {
		TTL = 3;
	}

	/**
	 * Custom TTL Constructor
	 *
	 * @param TTL custom Time To Live
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
		synchronized (container) {
			for (T t : collection) {
				container.put(t, TTL);
			}
		}
		tick();
	}

	public int getTTL(T t) {
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
	private void tick() {
		synchronized (container) {
			// foreach key let it age or remove it
			Iterator<T> iterator = container.keySet().iterator();
			while (iterator.hasNext()) {
				T t = iterator.next();
				int ttl = container.get(t);
				if (ttl > 0) {
					container.put(t, ttl - 1);
				} else {
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
				System.err.println("Thread not ready");
		}
	}
}
