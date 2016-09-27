package me.glor.BeaconNavigation;

import me.glor.Callee;
import me.glor.Matrix.Vector2D;
import me.glor.Run;
import me.glor.RunExternal;
import me.glor.Table;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

import static me.glor.Matrix.Vector2D.*;

/**
 * Created by glor on 9/18/16.
 */
public class BeaconCallee implements Callee<Beacon> {
	public static final double epsilon = 10e-5;
	private static RunExternal re = null;
	public Table<Beacon> t = null;
	DataOutputStream dos;
	public double[] sum = new double[2];
	public double cnt = 0;

	private BeaconCallee() {
		throw new RuntimeException();
	}

	public BeaconCallee(OutputStream outputStream) {
		dos = new DataOutputStream(outputStream);
		try {
			re = new RunExternal(RunExternal.getCommand(RunExternal.searchProgram("octave"), "-i", "-q", "--no-window-system"));
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Failed to start process.");
		}
		re.println("silent_functions(1);\n" +
				"function calcMin (fn)\n" +
				"\ty = fminsearch (fn, [0;0]);\n" +
				"\tprintf(\"%.5f %.5f\\n\", y(1), y(2));\n" +
				"endfunction");
		try {
			for (int i = 0; i < 6; i++) {
				re.p.getInputStream().read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String masterFormel(Vector2D vector, double length) {
		return "abs(" + length + "-sqrt( (" + vector.x() + "-x(1)" + ")^2 + (" + vector.y() + " - x(2))^2 ))";
	}

	public static String fullMasterFormel(int len, Vector2D[] vectors, double[] lengths) {
		String octaveCommand = "calcMin( @(x) " + masterFormel(vectors[0], lengths[0]);
		for (int i = 1; i < len; i++) {
			octaveCommand += " + " + masterFormel(vectors[i], lengths[i]);
		}

		octaveCommand += ");";
		return octaveCommand;
	}

	public static Vector2D calcPositionOctave(int len, Vector2D[] vectors, double[] lengths) {
		//System.out.println(fullMasterFormel(len, vectors, lengths));
		re.println(fullMasterFormel(len, vectors, lengths));

		String[] line = re.stdout.nextLine().split(" ");
		float f1 = Float.parseFloat(line[1]);
		float f2 = Float.parseFloat(line[2]);
		return new Vector2D(f1, f2);
	}

	/**
	 * WARNING: THIS IS A BAD HEURISTIC
	 *
	 * @param vectors Positions of the Beacons
	 * @param lengths Signal strength of the Beacons
	 * @return Your approximate Position
	 */
	public static Vector2D calc2DPosition(int len, Vector2D[] vectors, double[] lengths) {
		if (len < 2)
			throw new RuntimeException("Cannot calc Position with less than 2 Beacons.");
		Vector2D[] rot = new Vector2D[len];
		Vector2D m = mean(vectors);
		for (int i = 0; i < len; i++) {
			rot[i] = new Vector2D(m.x(), m.y());
		}
		// init first two ones
		double maxDiff = Double.MAX_EXPONENT;
		Vector2D tmp;
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

	public static void main(String[] args) throws IOException {
		Vector2D v1 = new Vector2D(0, 1);
		Vector2D v2 = new Vector2D(2, 3);
		Vector2D v3 = new Vector2D(4, 5);
		Vector2D[] v = new Vector2D[]{v1, v2, v3};
		System.out.println(fullMasterFormel(3, v, new double[]{1.5, 2.3, 4.1}));
		double d = System.currentTimeMillis();
		for (int i = 0; i < 100; i++) {
			System.out.println(calcPositionOctave(3, v, new double[]{1.5, 2.3, 4.1}));
		}
		System.out.println(System.currentTimeMillis() - d);

	}

	@Override
	public void calcPosition(Collection<Beacon> collection) {
		Vector2D[] vectors = new Vector2D[collection.size()];
		double[] lengths = new double[collection.size()];
		int len = 0;
		for (Beacon beacon : collection) {
			Vector2D v = BeaconDatabase.getPosition(beacon);
			if (v != null) {
				vectors[len] = v;
				lengths[len] = beacon.distance;
				len++;
			}
		}

		if (len < 3) {
			System.err.println("Cannot trilaterate with less than 3 known beacons." + len);
			return;
		}
		Vector2D result = calcPositionOctave(len, vectors, lengths);
		sum[0] += result.x();
		sum[1] += result.y();
		cnt++;
		if (cnt == 5) {
			sum[0] /= cnt;
			sum[1] /= cnt;
			result = new Vector2D(sum[0],sum[1]);
			//Vector2D alternative = calc2DPosition(len, vectors, lengths);
			System.out.println(result);
			//System.out.println(alternative);
			System.out.println();
			try {
				dos.writeByte(Run.MODUS_BEACON_BROADCAST);
				dos.writeFloat((float) result.x());
				dos.writeFloat((float) result.y());
				dos.writeByte(0);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}
}
