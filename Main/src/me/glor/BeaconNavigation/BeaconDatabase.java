package me.glor.BeaconNavigation;

import me.glor.Matrix.Position;

import java.util.TreeMap;

/**
 * Created by glor on 9/18/16.
 */
public class BeaconDatabase {
	private static TreeMap<Beacon, Position> content = new TreeMap<>();

	static {
		//58837
		Beacon a = new Beacon(0xf0018b9b75094c31L, 0xa9051a27d39c003cL, (short) 0xe56, 		(short) 0xffff92d4	, 0.0f, 0);
		content.put(a, new Position(0,0));
		//55298
		Beacon b = new Beacon(0xf0018b9b75094c31L, 0xa9051a27d39c003cL, (short) 0xffffa960, (short) 0x2aca 		, 0.0f, 0);
		content.put(b, new Position(0,2));
		//58865
		Beacon c = new Beacon(0xf0018b9b75094c31L, 0xa9051a27d39c003cL, (short) 0xffffbbd0, (short) 0xffff8ca8	, 0.0f, 0);
		content.put(c, new Position(0,4));

		// Na0q
		Beacon d = new Beacon(0xf7826da64fa24e98L, 0x8024bc5b71e0893eL, (short) 0x48b7, 	(short) 0xffffa8c5	, 0.0f, 0);
		content.put(d, new Position(5,0));
		// WCc5
		Beacon e = new Beacon(0xf7826da64fa24e98L, 0x8024bc5b71e0893eL, (short) 0x7163, 	(short) 0xffffa67b	, 0.0f, 0);
		content.put(e, new Position(0,0));
		// A51f
		Beacon f = new Beacon(0xf7826da64fa24e98L, 0x8024bc5b71e0893eL, (short) 0xffffbcd7, (short) 0x3d5		, 0.0f, 0);
		content.put(f, new Position(0,5));
	}

	public static Position getPosition(Beacon beacon) {
		return content.get(beacon);
	}

	public static boolean contains(Beacon beacon) {
		return content.containsKey(beacon);
	}
}
