package me.glor.BeaconNavigationServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

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

		int beaconCount = 0;    // 2 bytes
		try {
			// read in as unsigned
			beaconCount |= dis.readShort();
		} catch (IOException e) {
			System.err.println("Error while reading data.");
			e.printStackTrace();
			return;
		}
		ArrayList<Beacon> beacons = new ArrayList<Beacon>(beaconCount);

		long uuid1, uuid2;    // 8+8 = 16 byte
		short major, minor;    // 2 byte each
		short rssi = 0;
		beaconCount = 1;
		for (int i = 0; i < beaconCount; i++) {
			try {
				uuid1 = dis.readLong();
				uuid2 = dis.readLong();
				major = dis.readShort();
				minor = dis.readShort();
				// read in as unsigned
				rssi |= dis.readByte();
			} catch (IOException e) {
				System.err.println("Error while reading data.");
				e.printStackTrace();
				return;
			}
			beacons.add(new Beacon(uuid1, uuid2, major, minor, rssi));
		}
		for (Beacon beacon : beacons) {
			System.out.println("Received: " + beacon);
		}


	}
}
