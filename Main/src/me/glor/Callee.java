package me.glor;

import java.util.Collection;

/**
 * Created by glor on 9/18/16.
 */
public interface Callee<T> {
	public void calcPosition(Collection<T> collection);

	//remove
	void setTable(Table table);
}
