package me.glor.BeaconNavigation;

import java.util.Scanner;

/**
 * Created by glor on 9/24/16.
 */
public class TestKalman implements Runnable {
	@Override
	public void run() {
		Scanner sc = new Scanner(System.in);
		while (true) {
			double r, p, q;
			r = sc.nextDouble();
			p = sc.nextDouble();
			q = sc.nextDouble();
			SimpleKalman.r = r;
			SimpleKalman.p = p;
			SimpleKalman.q = q;
			System.out.println("set");
		}
	}
}
