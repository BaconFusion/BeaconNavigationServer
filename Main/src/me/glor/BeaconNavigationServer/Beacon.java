package me.glor.BeaconNavigationServer;

/**
 * Created by glor on 9/14/16.
 */
public class Beacon {
	public long uuid1, uuid2;    // 8+8 = 16 byte
	public short major, minor;    // 2 byte each
	public short rssi = 0;

	private Beacon() {
	}

	public Beacon(long uuid1, long uuid2, short major, short minor, short rssi) {
		this.uuid1 = uuid1;
		this.uuid2 = uuid2;
		this.major = major;
		this.minor = minor;
		this.rssi = rssi;
	}

	public String toString() {
		return "[" + Long.toHexString(uuid1) + "-" + Long.toHexString(uuid2) + "," + Integer.toHexString(major) + "," + Integer.toHexString(minor) + "," + rssi + "]";
	}
}
