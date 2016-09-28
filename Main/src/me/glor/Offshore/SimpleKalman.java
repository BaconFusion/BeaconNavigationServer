package me.glor.Offshore;

/**
 * Created by glor on 9/24/16.
 */
public class SimpleKalman {
	public static double q = 1; //process noise covariance
	public static double r = 1; //measurement noise covariance
	public static double p = 1; //estimation error covariance
	double x; //value
	double k; //kalman gain

	boolean initialized = false;

	public SimpleKalman() {
	}

	public void kalman_update(double measurement) {

		if (!initialized) {
			x = measurement;
			initialized = true;
			return;
		}
		//prediction update
		p = p + q;
		//measurement update
		k = p / (p + r);
		x = x + k * (measurement - x);
		p = (1 - k) * p;
	}

	public double getX() {
		return x;
	}
}
