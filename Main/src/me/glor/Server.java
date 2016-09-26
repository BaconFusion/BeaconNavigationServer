package me.glor;

import me.glor.BeaconNavigation.Beacon;
import me.glor.BeaconNavigation.BeaconCallee;
import me.glor.BeaconNavigation.Logger;
import me.glor.me.glor.SmartphoneSensorNavigation.SmartphoneSensorCallee;
import me.glor.me.glor.SmartphoneSensorNavigation.SmartphoneSensors;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Collection;

/**
 * Created by glor on 9/14/16.
 */
public class Server {
	Socket connectionSocket = null;
	DataInputStream dis;
	Logger log = Logger.getLogFile();
	Table<Beacon> beaconTable;
	Table<SmartphoneSensors> sensorTable;
	CallbackHandler beaconHandler;
	CallbackHandler sensorHandler;

	private Server() {
		throw new RuntimeException();
	}

	public Server(Socket socket) throws IOException {
		this.connectionSocket = socket;
		dis = new DataInputStream(connectionSocket.getInputStream());
		beaconTable = new Table();
		beaconHandler = new CallbackHandler(beaconTable, new BeaconCallee(connectionSocket.getOutputStream()));

		sensorTable = new Table();
		sensorHandler = new CallbackHandler(beaconTable, new SmartphoneSensorCallee(connectionSocket.getOutputStream()));
	}

	public void runBeacon() {
		log.println(Beacon.getCSVHeader());
		try {

			Collection<Beacon> data = Beacon.fill(dis);
			beaconTable.update(data);
			for (Beacon dataContainer : data) {
				//System.out.println(dataContainer);
				log.println(dataContainer.toCSVLine() + ", " + beaconTable.getKalman(dataContainer));
				log.flush();
			}
			//System.out.println(beaconTable.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void runSmartphoneSensors() {
		log.println(SmartphoneSensors.getCSVHeader());
		try {
			Collection<SmartphoneSensors> data = SmartphoneSensors.fill(dis);
			sensorTable.update(data);
			for (SmartphoneSensors dataContainer : data) {
				log.println(dataContainer.toCSVLine());
				log.flush();
			}
			//System.out.println(beaconTable.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
