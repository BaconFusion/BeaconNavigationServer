/*
run --> cartesian.start();

Transhandler --> callback blabalbla, senddata

BeaconCallee --> position senden

*/


package me.glor.Window;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import me.glor.Matrix.Position;
import me.glor.TransmissionHandler;

//import me.Matrix.Position;

public class Cartesian {
	static CartesianFrame frame;

	public static void updateData(Position ownPos, Position[] beacons, int[] identifiers) {
		frame.updateData(ownPos, beacons, identifiers);
	}

	/*public static float x, y;
	public static float[] b_x, b_y;
	public static int[] b_i;
	public static int len = 0;*/

	//public static Position ownPos;

	// public static void start(Position ownPos, Position[] beacons, int[] identifiers) {
//	public static void main(String args[]){
	public static void start() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				frame = new CartesianFrame();
				System.out.println("set CartesianFrame");
				frame.showUI();
			}
		});
	}

}



