package me.glor.BeaconNavigationServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Collection;

/**
 * Created by glor on 9/14/16.
 */
public class Server implements Runnable {
	Socket connectionSocket = null;

	private Server() {
	}

	public Server(Socket socket) {
		this.connectionSocket = socket;
	}

	public void run() {
		DataInputStream dis;
		try {
			dis = new DataInputStream(connectionSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		PrintWriter log = Run.getLogFile();
		log.println("time, UUID1, UUID2, Major, Minor, rssi");
		while (true) {
			try {
				Collection<Beacon> beacons = Beacon.readBeacons(dis);
				for (Beacon beacon : beacons) {
					System.out.println("printing " + beacon.toCSVLine());
					log.println(beacon.toCSVLine());
					log.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			System.out.println();
		}
		log.close();
	}
}
