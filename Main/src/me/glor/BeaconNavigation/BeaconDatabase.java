package me.glor.BeaconNavigation;

import me.glor.Matrix.Vector2D;

import java.util.TreeMap;

/**
 * Created by glor on 9/18/16.
 */
public class BeaconDatabase {
	private static TreeMap<Beacon, Vector2D> content = new TreeMap<>();

	static {
		Beacon a = new Beacon(0xf0018b9b75094c31L, 0xa9051a27d39c003cL, (short) 0xffffbbd0, (short) 0xffff8ca8, 0.0);
		content.put(a, new Vector2D(0, 0));
	}

	public static Vector2D getPosition(Beacon beacon) {
		return content.get(beacon);
	}
}
