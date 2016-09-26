package me.glor.BeaconNavigation;


import me.glor.RunExternal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class BeaconCalibration {
	public static void calibrate(Socket connectionSocket) throws IOException {
		DataInputStream dis = new DataInputStream(connectionSocket.getInputStream());
		int length = dis.readByte();
		float[] distance = new float[length];
		float[] rssi = new float[length];
		for (int i = 0; i < length; i++) {
			distance[i] = dis.readFloat();
			rssi[i] = dis.readFloat();
		}
		float[] params = calcParameters(distance, rssi);
		DataOutputStream dos = new DataOutputStream(connectionSocket.getOutputStream());

		//do not use more than 4 params
		for (int i = 0; i < 4; i++) {
			float f;
			if (i < params.length)
				f = params[i];
			else
				f = 0;
			dos.writeFloat(f);
		}
	}


	public static String formData(float[] distance, float[] rssi) throws IOException {

		if (distance.length != rssi.length) {
			throw new IOException("ArrayLists keys and values differ in size!");
		}

		String octaveX = "x = [";
		String octaveY = "y = [";

		for (int i = 0; i < distance.length; i++) {
			octaveX += distance[i] + " ";
			octaveY += rssi[i] + " ";
		}
		return octaveX + "];\n" + octaveY + "];";
	}

	public static String formulaToString(float[] ddat, float[] xdat) {
		String s1 = "xdat = [" + xdat[0];
		for (int i = 1; i < xdat.length; i++) {
			s1 += "," + xdat[i];
		}
		s1 += "];\n";

		String s2 = "ddat = [" + ddat[0];
		for (int i = 1; i < ddat.length; i++) {
			s2 += "," + ddat[i];
		}
		s2 += "];\n";

		String s3 = "[xmin,fval] = fminsearch(@(x) sum((ddat-(x(1)*(xdat).^x(2)+x(3))).^2),[0;0;0]);\ndisp(xmin(1));\ndisp(xmin(2));\ndisp(xmin(3));\n";
		return s1 + s2 + s3;
	}


	public static float[] calcParameters(float[] x, float[] y) throws IOException {
		// create octave commands
		String command = formulaToString(x, y);


		//a*Math.pow(rssi/oneMeterReferenceRSSI,b)+c
		RunExternal re = new RunExternal(RunExternal.getCommand(RunExternal.searchProgram("octave"), "-q", "--no-gui", "--no-window-system", "--persist"));
		re.println(command);

		float[] params = new float[3];
		for (int j = 0; j < 3; j++) {
			if (!re.stdout.hasNextDouble())
				throw new RuntimeException();
			params[j] = re.stdout.nextFloat();
		}
		re.stdout.close();
		return params;
	}

	public static void main(String[] args) {
		float[] y = {-80, -75, -65, -50, -40, -20};
		float[] x = {1, 2, 5, 10, 20, 50};
		float[] erg = null;
		try {
			erg = calcParameters(x, y);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (float f : erg) {
			System.out.println(f);
		}
	}
}
