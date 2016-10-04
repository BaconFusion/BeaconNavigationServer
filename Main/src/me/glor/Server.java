package me.glor;

import me.glor.BeaconNavigation.Beacon;
import me.glor.BeaconNavigation.BeaconCallee;
import me.glor.SmartphoneSensorNavigation.SmartphoneSensors;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by glor on 9/14/16.
 */
public class Server {
	TransmissionHandler th = null;
	Logger log = Logger.getLogFile();
	Table<Beacon> beaconTable;
	Table<SmartphoneSensors> sensorTable;
	CallbackHandler beaconHandler;
	CallbackHandler sensorHandler;

	private Server() {
		throw new RuntimeException();
	}

	public Server(TransmissionHandler th) throws IOException {
		this.th = th;
		beaconTable = new Table();
		beaconHandler = new CallbackHandler(beaconTable, new BeaconCallee(th));

		//sensorTable = new Table();
		//sensorHandler = new CallbackHandler(beaconTable, new SmartphoneSensorCallee(th));
	}

	public void runBeacon() {
		log.println(Beacon.getCSVHeader());
		try {
			Collection<Beacon> data = th.readBeacons();
			beaconTable.update(data);
			/*System.out.println();
			for (Beacon beacon : data) {
				log.println(beacon.toCSVLine());
				System.out.println(beacon.distance);
			}*/
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void runSmartphoneSensors() {
		throw new RuntimeException();
		/*
		log.println(SmartphoneSensors.getCSVHeader());
		try {
			Collection<SmartphoneSensors> data = SmartphoneSensors.readBeacons(dis);
			sensorTable.update(data);
			for (SmartphoneSensors dataContainer : data) {
				log.println(dataContainer.toCSVLine());
			}
			//System.out.println(beaconTable.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
}
