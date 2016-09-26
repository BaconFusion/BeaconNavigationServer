package me.glor.BeaconNavigation;

import me.glor.Matrix.Vector2D;

import java.util.TreeMap;

/**
 * Created by glor on 9/18/16.
 */
public class BeaconDatabase {
	private static TreeMap<Beacon, Vector2D> content = new TreeMap<>();

	static {
		Beacon a = new Beacon(0xf0018b9b75094c31L, 0xa9051a27d39c003cL, (short) 0xffffbbd0, (short) 0xffff8ca8, 0.0f, 0);    //L
		Beacon b = new Beacon(0xf0018b9b75094c31L, 0xa9051a27d39c003cL, (short) 0xffffa960, (short) 0x2aca, 0.0f, 0);        //S
		Beacon c = new Beacon(0xf7826da64fa24e98L, 0x8024bc5b71e0893eL, (short) 0xffffbcd7, (short) 0x3d5, 0.0f, 0);        //F/K
		content.put(a, new Vector2D(1.15, 0));
		content.put(b, new Vector2D(0, 0));
		content.put(c, new Vector2D(0, 1.15));
	}

	public static Vector2D getPosition(Beacon beacon) {
		return content.get(beacon);
	}

	public static boolean contains(Beacon beacon) {
		return content.containsKey(beacon);
	}
}
