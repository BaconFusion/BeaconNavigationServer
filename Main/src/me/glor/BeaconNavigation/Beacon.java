package me.glor.BeaconNavigation;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Collection;

/**
 * Container Class for Beacons which contains the identifiers, the approximate distance and a timestamp
 */
public class Beacon implements Comparable<Beacon> {

	public final long uuid1, uuid2;    // 8+8 = 16 byte
	public final short major, minor;    // 2 byte each
	public final float distance;
	public final long time;

	private Beacon() {
		throw new UnsupportedOperationException();
	}

	public Beacon(long uuid1, long uuid2, short major, short minor, float distance, long time) {
		this.uuid1 = uuid1;
		this.uuid2 = uuid2;
		this.major = major;
		this.minor = minor;
		this.distance = distance;
		this.time = time;
	}

	public static String getCSVHeader() {
		return "time, UUID1, UUID2, Major, Minor, distance";
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
