package me.glor;

import me.glor.BeaconNavigation.Beacon;
import me.glor.BeaconNavigation.BeaconCallee;
import me.glor.BeaconNavigation.Logger;

import java.io.DataInputStream;
import java.io.IOException;
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
		Logger log = Logger.getLogFile();

		Table<Beacon> table = new Table<>();
		CallbackHandler callbackHandler = new CallbackHandler(table, new BeaconCallee());

		log.println("time, UUID1, UUID2, Major, Minor, distance");
		while (true) {
			try {
				Collection<Beacon> beacons = Beacon.readBeacons(dis);
				for (Beacon beacon : beacons) {
					//System.out.println("printing " + beacon.toCSVLine());
					log.println(beacon.toCSVLine());
					log.flush();
				}
				table.update(beacons);
				System.out.println(table.toString());
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			System.out.println();
		}
	}
}
