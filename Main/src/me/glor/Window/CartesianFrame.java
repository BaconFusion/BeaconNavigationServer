package me.glor.Window;

import me.glor.Matrix.Position;
import me.glor.TransmissionHandler;

import javax.swing.*;

/**
 * Created by glor on 9/30/16.
 */
public class CartesianFrame extends JFrame {
	public static CartesianPanel panel;

	public static void updateData(Position ownPos, Position[] beacons, int[] identifiers) {
		panel.updateData(ownPos, beacons, identifiers);
	}

	public CartesianFrame() {
		panel = new CartesianPanel();
		System.out.println("set CartesianPanel");
		TransmissionHandler.setPositionNotifier(panel);
		add(panel);
		//panel.updateData(new Position, null, null);
	}

	public void showUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Cartesian");
		setSize(700, 700);
		setVisible(true);
	}
}