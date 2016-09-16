package me.glor.BeaconNavigationServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by glor on 9/14/16.
 */
public class Beacon {

	public long uuid1, uuid2;    // 8+8 = 16 byte
	public short major, minor;    // 2 byte each
	public short rssi = 0;
	public long time;
	private Beacon() {
	}

	public Beacon(DataInputStream dis) throws IOException {
		time = System.currentTimeMillis();
		uuid1 = dis.readLong();
		uuid2 = dis.readLong();
		major = dis.readShort();
		minor = dis.readShort();
		// read in as unsigned
		rssi |= dis.readByte();
	}

	public Beacon(long uuid1, long uuid2, short major, short minor, short rssi) {
		this.uuid1 = uuid1;
		this.uuid2 = uuid2;
		this.major = major;
		this.minor = minor;
		this.rssi = rssi;
	}

	public static void writeBeacons(Collection<Beacon> beacons, DataOutputStream dos) throws IOException {
		dos.writeShort((short) beacons.size());
		for (Beacon beacon : beacons) {
			beacon.writeToDataOutputStream(dos);
		}
	}

	public static Collection<Beacon> readBeacons(DataInputStream dis) throws IOException {
		int beaconCount = 0;    // 2 bytes
		// read in as unsigned
		beaconCount |= dis.readShort();

		ArrayList<Beacon> beacons = new ArrayList<Beacon>(beaconCount);

		for (int i = 0; i < beaconCount; i++) {
			beacons.add(new Beacon(dis));
		}
		for (Beacon beacon : beacons) {
			System.out.println("Received: " + beacon);
		}
		return beacons;
	}

	public void writeToDataOutputStream(DataOutputStream dos) throws IOException {
		dos.writeLong(uuid1);
		dos.writeLong(uuid2);
		dos.writeLong(major);
		dos.writeLong(minor);
		dos.writeLong(rssi);
	}

	public String toString() {
		return time + "[" + Long.toHexString(uuid1) + "-" + Long.toHexString(uuid2) + "," + Integer.toHexString(major) + "," + Integer.toHexString(minor) + "," + rssi + "]";
	}

	public String toCSVLine() {
		return time + ", " + Long.toHexString(uuid1) + ", " + Long.toHexString(uuid2) + ", " + Integer.toHexString(major) + ", " + Integer.toHexString(minor) + ", " + rssi;
	}
}
