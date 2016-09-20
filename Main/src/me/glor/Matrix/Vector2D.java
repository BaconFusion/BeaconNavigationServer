package me.glor.Matrix;

/**
 * Created by glor on 9/18/16.
 */
public class Vector2D extends Vector {
	public Vector2D() {
		super(2);
	}

	public Vector2D(double x, double y) {
		super(2);
		content[0] = x;
		content[1] = y;
	}

	public static Vector2D add(Vector2D a, Vector2D b) {
		return new Vector2D(a.x() + b.x(), a.y() + b.y());
	}

	public static Vector2D sub(Vector2D a, Vector2D b) {
		return new Vector2D(a.x() - b.x(), a.y() - b.y());
	}

	public static Vector2D fromTo(Vector2D b, Vector2D a) {
		return new Vector2D(a.x() - b.x(), a.y() - b.y());
	}

	public static Vector2D subAbs(Vector2D a, Vector2D b) {
		return abs(sub(a, b));
	}

	public static Vector2D mean(Vector2D... vectors) {
		Vector2D sum = new Vector2D(0, 0);
		for (Vector2D vector : vectors) {
			sum.content[0] += vector.x();
			sum.content[1] += vector.y();
		}
		sum.content[0] /= vectors.length;
		sum.content[1] /= vectors.length;
		return sum;
	}

	public static Vector2D meanWithout(Vector2D without, Vector2D... vectors) {
		Vector2D sum = new Vector2D(0, 0);
		for (Vector2D vector : vectors) {
			sum.content[0] += vector.x();
			sum.content[1] += vector.y();
		}
		sum.content[0] /= vectors.length;
		sum.content[1] /= vectors.length;
		return sum;
	}

	public static Vector2D mean(Vector2D a, Vector2D b, Vector2D c) {
		return new Vector2D((a.x() + b.x() + c.x()) / 3, (a.y() + b.y() + c.y()) / 3);
	}

	public static Vector2D scaleTo(double d, Vector2D a) {

		return scale(d, norm(a));
		//double len = a.length();
		//return new Vector2D(a.x()/len*d, a.y()/len*d);
	}

	public static Vector2D norm(Vector2D a) {
		double len = a.length();
		//System.out.println("norm " + a +  " to " + new Vector2D(a.x()/len, a.y()/len));
		return new Vector2D(a.x() / len, a.y() / len);
	}

	public static Vector2D scale(double len, Vector2D a) {
		//System.out.println("scale " + a + " by " + len +  " to " + new Vector2D(a.x()*len, a.y()*len));
		return new Vector2D(a.x() * len, a.y() * len);
	}

	public static Vector2D abs(Vector2D a) {
		return new Vector2D(Math.abs(a.x()), Math.abs(a.y()));
	}

	public double x() {
		return content[0];
	}

	public double y() {
		return content[1];
	}

	@Override
	public String toString() {
		return "(" + content[0] + "," + content[1] + ")";
	}
}
