package me.glor;

import me.glor.BeaconNavigation.Beacon;
import me.glor.Matrix.Position;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by glor on 9/27/16.
 */
public class TransmissionHandler {
	private DataInputStream dis;
	private DataOutputStream dos;

	private TransmissionHandler() {
		throw new UnsupportedOperationException();
	}

	public TransmissionHandler(Socket socket) throws IOException {
		dis = new DataInputStream(socket.getInputStream());
		dos = new DataOutputStream(socket.getOutputStream());
	}


	public synchronized byte getModus() throws IOException {
		return dis.readByte();
	}

	/**
	 * Reading in Modus MODUS_BEACON_BROADCAST
	 * @return
	 * @throws IOException
	 */
	public synchronized Collection<Beacon> readBeacons() throws IOException {
		// stdout in as unsigned	// 2 bytes
		int beaconCount = dis.readShort();
		long time = dis.readLong();
		ArrayList<Beacon> beacons = new ArrayList<Beacon>(beaconCount);

		for (int i = 0; i < beaconCount; i++) {
			long uuid1 = dis.readLong();
			long uuid2 = dis.readLong();
			short major = dis.readShort();
			short minor = dis.readShort();
			// stdout in as unsigned
			float distance = dis.readFloat();
			beacons.add(new Beacon(uuid1,uuid2,major,minor,distance,time));
		}
		return beacons;
	}

	public synchronized void writeBeacons(Collection<Beacon> beacons) throws IOException {
		dos.writeShort((short) beacons.size());
		for (Beacon beacon : beacons) {
			dos.writeLong(beacon.uuid1);
			dos.writeLong(beacon.uuid2);
			dos.writeLong(beacon.major);
			dos.writeLong(beacon.minor);
			dos.writeFloat(beacon.distance);
		}
	}

	public synchronized void sendPositions(Position x, Position[] beacons, int[] identifiers) throws IOException {
		dos.writeByte(Run.MODUS_BEACON_BROADCAST);
		dos.writeFloat((float) x.x());
		dos.writeFloat((float) x.y());
		dos.writeByte(beacons.length);
		for (int i = 0; i < beacons.length; i++) {
			dos.writeFloat((float)beacons[i].x());
			dos.writeFloat((float)beacons[i].y());
			dos.writeInt(identifiers[i]);
		}
	}
	public synchronized float[][] receiveCalibrate() throws IOException {
		int length = dis.readByte();
		float[] distance = new float[length];
		float[] rssi = new float[length];
		for (int i = 0; i < length; i++) {
			distance[i] = dis.readFloat();
			rssi[i] = dis.readFloat();
		}
		return new float[][]{distance,rssi};
	}

	public void writeCalibrate(double a, double b, double c) throws IOException {
		dos.writeByte(Run.MODUS_BEACON_CALIBRATE);
		dos.writeFloat((float)a);
		dos.writeFloat((float)b);
		dos.writeFloat((float)c);
	}
}
