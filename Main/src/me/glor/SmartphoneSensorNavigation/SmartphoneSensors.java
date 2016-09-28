package me.glor.SmartphoneSensorNavigation;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Collection;

/**
 *
 */
public class SmartphoneSensors implements Comparable<SmartphoneSensors> {


	public static Collection<SmartphoneSensors> fill(DataInputStream dis) throws IOException {
		return null;
	}

	public static String getCSVHeader() {
		return null;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SmartphoneSensors) {
			SmartphoneSensors ss = (SmartphoneSensors) o;
			return compareTo(ss) == 0;
		}
		return false;
	}

	public String toCSVLine() {
		return null;
	}

	/**
	 * Compare SmartphoneSensor type only
	 *
	 * @param o The {@code SmartphoneSensors} to be compared with
	 * @return {@code true} if type of data is the same, value is NOT relevant
	 */
	@Override
	public int compareTo(SmartphoneSensors o) {
		return 0;
	}
}
