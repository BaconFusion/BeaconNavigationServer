package me.glor.BeaconNavigation;

import me.glor.Callee;
import me.glor.Matrix.Vector2D;
import me.glor.RunExternal;

import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

import static me.glor.Matrix.Vector2D.*;

/**
 * Created by glor on 9/18/16.
 */
public class BeaconCallee implements Callee<Beacon> {
	public static final double epsilon = 10e-5;

	public static String masterFormel(Vector2D vector, double length) {
		return "(" + length + " - sqrt( (" + vector.x() + ")";
	}

	public static Vector2D calcPositionOctave(Vector2D[] vectors, double[] lengths) {
		RunExternal re = null;
		try {
			re = new RunExternal(RunExternal.octave);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		// vector can be null
		if (vectors.length < 3) {
			System.out.println("Cannot trilaterate with less than 3 datapoints.");
		}
		String octaveCommand = "calcMin( @(x) " + masterFormel(vectors[0], lengths[0]);
		for (int i = 1; i < vectors.length; i++) {
			octaveCommand += " + " + masterFormel(vectors[i], lengths[i]);
		}

		octaveCommand += ", " + vectors.length + ")";

		re.stdin.println("5");
		re.stdin.close();

		float f1 = re.stdout.nextFloat();
		float f2 = re.stdout.nextFloat();

		System.out.println(f1);
		System.out.println(f2);
		return null;
	}

	/**
	 * WARNING: THIS IS A BAD HEURISTIC
	 *
	 * @param vectors Positions of the Beacons
	 * @param lengths Signal strength of the Beacons
	 * @return Your approximate Position
	 */
	public static Vector2D calc2DPosition(Vector2D[] vectors, double[] lengths) {
		if (vectors.length != lengths.length || vectors.length == 1)
			throw new RuntimeException("Array Length too small or not matching.");
		Vector2D[] rot = new Vector2D[vectors.length];
		Vector2D m = mean(vectors);
		for (int i = 0; i < rot.length; i++) {
			rot[i] = new Vector2D(m.x(), m.y());
		}
		// init first two ones
		double maxDiff = Double.MAX_EXPONENT;
		Vector2D tmp;
		int j = 0;
		while (maxDiff > epsilon) {
			maxDiff = 0;
			//System.out.println();
			for (int i = 0; i < vectors.length; i++) {
				tmp = rot[i];
				rot[i] = add(vectors[i],
						scaleTo(lengths[i],
								fromTo(vectors[i],
										meanWithout(rot[i], rot))));
				//System.out.println("rot["+i+"]" + rot[i] + " diff " + sub(tmp, rot[i]).length());
				maxDiff = Math.max(maxDiff, sub(tmp, rot[i]).length());
			}
			j++;
		}
		//System.out.println("Took " + j + " turns.");
		return mean(rot);
	}

	public static void main(String[] args) throws IOException {
		Vector2D a = new Vector2D(0, 2);
		double alen = 0.5;
		Vector2D b = new Vector2D(0, 0);
		double blen = 0.5;
		Vector2D c = new Vector2D(1, 0);
		double clen = 0.5;
		Scanner in = new Scanner(System.in);
		while (true) {
			double ax, ay, al, bx, by, bl, cx, cy, cl;
			System.err.println("a: x, y, length");
			ax = in.nextDouble();
			ay = in.nextDouble();
			al = in.nextDouble();
			System.err.println("b: x, y, length");
			bx = in.nextDouble();
			by = in.nextDouble();
			bl = in.nextDouble();
			System.err.println("c: x, y, length");
			cx = in.nextDouble();
			cy = in.nextDouble();
			cl = in.nextDouble();
			Vector2D[] vectors = new Vector2D[]{new Vector2D(ax, ay)};//, new Vector2D(bx,by), new Vector2D(cx,cy)};
			Vector2D out = calc2DPosition(vectors, new double[]{al});//,bl,cl
			//Vector2D out = calc2DPosition(a, alen, b, blen, c, clen);
			//System.out.println("plot(x=c(" + a.x() + "," + b.x() + "," + c.x() + "," + out.x() + "),y=c(" + a.y() + "," + b.y() + "," + c.y() + "," + out.y() + "))");
			//System.out.println("text(x=c(" + a.x() + "," + b.x() + "," + c.x() + "," + out.x() + "),y=c(" + a.y() + "," + b.y() + "," + c.y() + "," + out.y() + "),labels=c('x1','x2','x3','out'), cex=0.7, pos=3)");
			System.err.println(out);
			System.err.flush();
			System.out.println("plot(x=c(" + ax + "," + bx + "," + cx + "," + out.x() + "),y=c(" + ay + "," + by + "," + cy + "," + out.y() + "))");
			System.out.println("text(x=c(" + ax + "," + bx + "," + cx + "," + out.x() + "),y=c(" + ay + "," + by + "," + cy + "," + out.y() + "),labels=c('x1','x2','x3','out'), cex=0.7, pos=3)");

		}
	}

	@Override
	public void calcPosition(Collection<Beacon> collection) {
		if (collection.size() < 2) {
			System.out.println("Cannot calculate Position with just 1 Vector.");
			return;
		}
		Vector2D[] vectors = new Vector2D[collection.size()];
		double[] lengths = new double[collection.size()];
		int i = 0;
		for (Beacon beacon : collection) {
			lengths[i] = beacon.distance;
			vectors[i++] = BeaconDatabase.getPosition(beacon);
			System.out.println(BeaconDatabase.getPosition(beacon));
		}


		System.out.println(calc2DPosition(vectors, lengths));
	}
}
