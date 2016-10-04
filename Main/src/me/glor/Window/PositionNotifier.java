package me.glor.Window;

import me.glor.Matrix.*;

public interface PositionNotifier{

	public void updateData(Position ownPos, Position[] beacons, int[] identifiers);

}
