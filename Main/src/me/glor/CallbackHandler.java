package me.glor;


import java.util.Collection;

import static java.lang.Thread.currentThread;

/**
 * Created by glor on 9/18/16.
 */
public class CallbackHandler<T extends Comparable<T>> implements Runnable {
	private final Table<T> table;
	private final Callee<T> callee;
	public boolean restart = true;

	private CallbackHandler() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Constructs a new Positioner which connects to a Table.
	 *
	 * @param table
	 */
	public CallbackHandler(Table<T> table, Callee<T> callee) {
		this.table = table;
		this.callee = callee;

		table.addCallback(new Thread(this));
	}

	@Override
	public void run() {
		while (restart) {
			Collection<T> collection = table.toCollection();
			callee.calcPosition(collection);
			try {
				synchronized (currentThread()) {
					currentThread().wait();
				}
			} catch (InterruptedException e) {
				throw new RuntimeException();
			}
		}
	}
}
