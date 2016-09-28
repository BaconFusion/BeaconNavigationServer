package me.glor.Matrix;

/**
 * Created by glor on 9/18/16.
 */
public class Position {
	private double[] content = new double[2];
	public Position() {
	}

	public Position(double x, double y) {
		content[0] = x;
		content[1] = y;
	}

	public static Position add(Position a, Position b) {
		return new Position(a.x() + b.x(), a.y() + b.y());
	}

	public static Position sub(Position a, Position b) {
		return new Position(a.x() - b.x(), a.y() - b.y());
	}

	public static Position fromTo(Position b, Position a) {
		return new Position(a.x() - b.x(), a.y() - b.y());
	}

	public static Position subAbs(Position a, Position b) {
		return abs(sub(a, b));
	}

	public static Position mean(Position... vectors) {
		Position sum = new Position(0, 0);
		for (Position vector : vectors) {
			sum.content[0] += vector.x();
			sum.content[1] += vector.y();
		}
		sum.content[0] /= vectors.length;
		sum.content[1] /= vectors.length;
		return sum;
	}

	public static Position meanWithout(Position without, Position... vectors) {
		Position sum = new Position(0, 0);
		for (Position vector : vectors) {
			sum.content[0] += vector.x();
			sum.content[1] += vector.y();
		}
		sum.content[0] /= vectors.length;
		sum.content[1] /= vectors.length;
		return sum;
	}

	public static Position mean(Position a, Position b, Position c) {
		return new Position((a.x() + b.x() + c.x()) / 3, (a.y() + b.y() + c.y()) / 3);
	}

	public static Position scaleTo(double d, Position a) {

		return scale(d, norm(a));
		//double len = a.length();
		//return new Position(a.x()/len*d, a.y()/len*d);
	}

	public static Position norm(Position a) {
		double len = a.length();
		//System.out.println("norm " + a +  " to " + new Position(a.x()/len, a.y()/len));
		return new Position(a.x() / len, a.y() / len);
	}

	public double length() {
		return Math.sqrt(x()*x()+y()*y());
	}

	public static Position scale(double len, Position a) {
		//System.out.println("scale " + a + " by " + len +  " to " + new Position(a.x()*len, a.y()*len));
		return new Position(a.x() * len, a.y() * len);
	}

	public static Position abs(Position a) {
		return new Position(Math.abs(a.x()), Math.abs(a.y()));
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
