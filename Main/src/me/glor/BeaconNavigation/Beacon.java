package me.glor.BeaconNavigation;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by glor on 9/14/16.
 */
public class Beacon implements Comparable<Beacon> {

	public final long uuid1, uuid2;    // 8+8 = 16 byte
	public final short major, minor;    // 2 byte each
	public final double distance;
	public final long time;
	private Beacon() {
		throw new UnsupportedOperationException();
	}

	public Beacon(DataInputStream dis) throws IOException {
		time = System.currentTimeMillis();
		uuid1 = dis.readLong();
		uuid2 = dis.readLong();
		major = dis.readShort();
		minor = dis.readShort();
		// stdout in as unsigned
		distance = dis.readDouble();
	}

	public Beacon(long uuid1, long uuid2, short major, short minor, double distance) {
		this.uuid1 = uuid1;
		this.uuid2 = uuid2;
		this.major = major;
		this.minor = minor;
		this.distance = distance;
		time = System.currentTimeMillis();
	}

	public static void writeBeacons(Collection<Beacon> beacons, DataOutputStream dos) throws IOException {
		dos.writeShort((short) beacons.size());
		for (Beacon beacon : beacons) {
			beacon.writeToDataOutputStream(dos);
		}
	}

	public static Collection<Beacon> readBeacons(DataInputStream dis) throws IOException {
		int beaconCount = 0;    // 2 bytes
		// stdout in as unsigned
		beaconCount |= dis.readShort();

		ArrayList<Beacon> beacons = new ArrayList<Beacon>(beaconCount);

		for (int i = 0; i < beaconCount; i++) {
			beacons.add(new Beacon(dis));
		}
		return beacons;
	}

	public void writeToDataOutputStream(DataOutputStream dos) throws IOException {
		dos.writeLong(uuid1);
		dos.writeLong(uuid2);
		dos.writeLong(major);
		dos.writeLong(minor);
		dos.writeDouble(distance);
	}

	public String toString() {
		return time + "[" + Long.toHexString(uuid1) + "-" + Long.toHexString(uuid2) + "," + Integer.toHexString(major) + "," + Integer.toHexString(minor) + "," + distance + "]";
	}

	public String toCSVLine() {
		return time + ", " + Long.toHexString(uuid1) + ", " + Long.toHexString(uuid2) + ", " + Integer.toHexString(major) + ", " + Integer.toHexString(minor) + ", " + distance;
	}

	@Override
	public int compareTo(Beacon beacon) {
		long tmp = 0;
		tmp = uuid1 - beacon.uuid1;
		if (tmp != 0)
			return (int) tmp;
		tmp = uuid2 - beacon.uuid2;
		if (tmp != 0)
			return (int) tmp;
		tmp = major - beacon.major;
		if (tmp != 0)
			return (int) tmp;
		tmp = minor - beacon.minor;
		if (tmp != 0)
			return (int) tmp;
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Beacon) {
			Beacon beacon = (Beacon) o;
			return compareTo(beacon) == 0;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (int) ((uuid1 >> 32)
				^ (uuid1 & 0xFFFFFFFF)
				^ (uuid2 >> 32)
				^ (uuid2 & 0xFFFFFFFF)
				^ ((int) major) << 16 ^ minor);
	}
}
