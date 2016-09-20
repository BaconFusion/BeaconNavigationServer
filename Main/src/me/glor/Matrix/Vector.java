package me.glor.Matrix;

import java.util.Iterator;

/**
 * Created by glor on 9/18/16.
 */
public class Vector implements Iterable<Double> {
	public double[] content;

	private Vector() {
		throw new UnsupportedOperationException();
	}

	public Vector(int dim) {
		content = new double[dim];
	}

	public double length() {
		double tmp = 0;
		for (double d :
				content) {
			tmp += d * d;
		}
		return Math.sqrt(tmp);
	}

	@Override
	public Iterator<Double> iterator() {
		return new Iterator<Double>() {
			int index = 0;

			@Override
			public boolean hasNext() {
				return index < content.length;
			}

			@Override
			public Double next() {
				return content[index++];
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
}
