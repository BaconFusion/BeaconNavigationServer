package me.glor.Offshore;

import me.glor.BeaconNavigation.BeaconCallee;
import me.glor.Matrix.Position;

import static me.glor.Matrix.Position.*;

/**
 * Created by glor on 9/27/16.
 */
public class BadPositioning {
	public static final double epsilon = 10e-5;
	/**
	 * WARNING: THIS IS A BAD HEURISTIC
	 *
	 * @param vectors Positions of the Beacons
	 * @param lengths Signal strength of the Beacons
	 * @return Your approximate Position
	 */
	public static Position calc2DPosition(int len, Position[] vectors, double[] lengths) {
		if (len < 2)
			throw new RuntimeException("Cannot calc Position with less than 2 Beacons.");
		Position[] rot = new Position[len];
		Position m = mean(vectors);
		for (int i = 0; i < len; i++) {
			rot[i] = new Position(m.x(), m.y());
		}
		// init first two ones
		double maxDiff = Double.MAX_EXPONENT;
		Position tmp;
		int j = 0;
		while (maxDiff > epsilon) {
			maxDiff = 0;
			//System.out.println();
			for (int i = 0; i < len; i++) {
				tmp = rot[i];
				rot[i] = add(vectors[i],
						scaleTo(lengths[i],
								fromTo(vectors[i],
										meanWithout(rot[i], rot))));
				maxDiff = Math.max(maxDiff, sub(tmp, rot[i]).length());
			}
			j++;
		}
		//System.out.println("Took " + j + " turns.");
		return mean(rot);
	}

}
