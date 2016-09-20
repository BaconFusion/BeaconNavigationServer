package me.glor.me.glor.SmartphoneSensorNavigation;

/**
 *
 */
public class SmartphoneSensors implements Comparable<SmartphoneSensors> {

	/**
	 * Compare SmartphoneSensor type only
	 *
	 * @param ss The {@code SmartphoneSensors} to be compared with
	 * @return {@code true} if type of data is the same, value is NOT relevant
	 */
	@Override
	public int compareTo(SmartphoneSensors ss) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SmartphoneSensors) {
			SmartphoneSensors ss = (SmartphoneSensors) o;
			return compareTo(ss) == 0;
		}
		return false;
	}
}
