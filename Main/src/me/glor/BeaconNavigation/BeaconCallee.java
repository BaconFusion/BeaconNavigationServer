package me.glor.BeaconNavigation;

import me.glor.*;
import me.glor.Matrix.Position;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by glor on 9/18/16.
 */
public class BeaconCallee implements Callee<Beacon> {

	private static RunExternal re = null;
	TransmissionHandler th;
	public double[] sum = new double[2];
	public double cnt = 0;
	public static final String octaveInitCommand = "silent_functions(1);\n" +
			"function calcMin (fn)\n" +
			"\ty = fminsearch (fn, [0;0]);\n" +
			"\tprintf(\"%.5f %.5f\\n\", y(1), y(2));\n" +
			"endfunction";

	private BeaconCallee() {
		throw new RuntimeException();
	}

	public BeaconCallee(TransmissionHandler th) {
		this.th = th;
		try {
			re = new RunExternal(RunExternal.getCommand(RunExternal.searchProgram("octave"), "-i", "-q", "--no-window-system"));
			re.println(octaveInitCommand);

			for (int i = 0; i < 6; i++) {
				re.p.getInputStream().read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Position calcPositionOctave(int len, Position[] vectors, double[] lengths) {
		String octaveCommand = "calcMin( @(x) ";
		for (int i = 0; i < len; i++) {
			octaveCommand += "abs(" + lengths[i] + "-sqrt( (" + vectors[i].x() + "-x(1)" + ")^2 + (" + vectors[i].y() + " - x(2))^2 )) + ";
		}
		octaveCommand += "0);";
		re.println(octaveCommand);

		String[] line = re.stdout.nextLine().split(" ");
		float x1 = Float.parseFloat(line[1]);
		float x2 = Float.parseFloat(line[2]);
		return new Position(x1, x2);
	}
	/*public static int xi = 0;
	public static int yi = 0;
	public nextI(double x, double y) {

	}*/
	@Override
	public void calcPosition(Collection<Beacon> collection) {
		Position[] vectors = new Position[collection.size()];
		double[] lengths = new double[collection.size()];
		int len = 0;
		for (Beacon beacon : collection) {
			Position v = BeaconDatabase.getPosition(beacon);
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
		Position result = calcPositionOctave(len, vectors, lengths);
		System.out.println(result);

		try {
			th.sendPositions(result,null,null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.flush();
		/*
		sum[0] += result.x();
		sum[1] += result.y();
		cnt++;
		if (cnt == 5) {
			System.out.println("sending...");
			System.out.flush();
			sum[0] /= cnt;
			sum[1] /= cnt;
			result = new Position(sum[0], sum[1]);
			try {
				th.sendPositions(result, new Position[0], new int[0]);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
			sum[0] = 0;
			sum[1] = 0;
			cnt = 0;
		}*/
	}
}
