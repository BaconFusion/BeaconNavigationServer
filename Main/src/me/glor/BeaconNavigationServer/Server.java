package me.glor.BeaconNavigationServer;

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
		try {
			Collection<Beacon> beacons = Beacon.readBeacons(dis);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
