package me.glor.Matrix;

import javax.naming.OperationNotSupportedException;

/**
 * Created by glor on 9/15/16.
 */
public class Matrix {
	private final double[][] content;
	private int x;
	private int y;


	private Matrix() {
		content = null;
		throw new UnsupportedOperationException();
	}

	public Matrix(int x, int y) {
		this.x = x;
		this.y = y;
		content = new double[x][y];
	}

	public Matrix(Matrix matrix) {
		x = matrix.x;
		y = matrix.y;
		content = matrix.content;
	}

	public static Matrix mul(Matrix a, Matrix b) {
		if (a.x != b.y || a.y != b.x)
			throw new NumberFormatException("Wrong Matrix Dimensions");
		Matrix c = new Matrix(a.x, b.y);
		// iterate over matrix elements
		for (int i = 0; i < c.x; i++) {
			for (int j = 0; j < c.y; j++) {
				//iterate over a.cols and b.rows
				for (int k = 0; k < a.y; k++) {
					c.content[i][j] += a.content[i][k] * b.content[k][j];
				}
			}
		}
		return c;
	}

	public static Matrix add(Matrix a, Matrix b) throws OperationNotSupportedException {
		throw new OperationNotSupportedException();
	}

	public static Matrix transpose(Matrix m) {
		Matrix m2 = new Matrix(m.y, m.x);
		for (int i = 0; i < m.x; i++) {
			for (int j = 0; j < m.y; j++) {
				m2.content[j][i] = m.content[i][j];
			}
		}
		return m2;
	}

	public Matrix solve() throws OperationNotSupportedException {
		throw new OperationNotSupportedException();
	}
}
