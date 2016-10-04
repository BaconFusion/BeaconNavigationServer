package me.glor.BeaconNavigation;

import me.glor.Run;
import me.glor.RunExternal;
import me.glor.TransmissionHandler;

import java.awt.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BeaconCalibration {
	public static String formulaToString(float[] ddat, float[] xdat) {
		String s1 = "xdat = [" + xdat[0];
		String s2 = "ddat = [" + ddat[0];
		for (int i = 1; i < xdat.length; i++) {
			s1 += "," + xdat[i];
			s2 += "," + ddat[i];
		}
		s1 += "];\n";
		s2 += "];\n";

		String s3 = "[xmin,fval] = fminsearch(@(x) sum((ddat-(x(1)*(xdat).^x(2)+x(3))).^2),[0;0;0]);\ndisp(xmin(1));\ndisp(xmin(2));\ndisp(xmin(3));\n";
		return s1 + s2 + s3;
	}

	public static void calibrate(TransmissionHandler th) throws IOException {
		float[][] tmp = th.receiveCalibrate();

		float[] x = tmp[0];
		float[] y = tmp[1];
		RunExternal re = new RunExternal(RunExternal.getCommand(RunExternal.searchProgram("octave"), "-q", "--no-window-system"));
		re.println(formulaToString(x, y));
		re.stdin.close();

		float a = re.stdout.nextFloat();
		float b = re.stdout.nextFloat();
		float c = re.stdout.nextFloat();

		re.stdout.close();
		re.stop();

		//System.out.println("NEXUS 5 MODE");
		//th.writeCalibrate(0.42093,6.9476,0.5992);

		th.writeCalibrate(a, b, c);
	}
}
