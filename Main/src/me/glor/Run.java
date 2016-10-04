package me.glor;

import me.glor.BeaconNavigation.BeaconCalibration;
import me.glor.Matrix.Position;
import me.glor.Window.Cartesian;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by glor on 9/14/16.
 */
public class Run implements Runnable {
	public static final String logFilePrefix = "./data";
	public static final String logFileSuffix = ".log";
	public static final int MODUS_BEACON_BROADCAST = 0;
	public static final int MODUS_BEACON_CALIBRATE = 1;
	public static final int MODUS_SMARTPHONE_SENSORS = 2;
	private static int PORT = 6788;
	private Socket connectionSocket;
	private final TransmissionHandler th;

	private Run() {
		throw new UnsupportedOperationException();
	}

	public Run(Socket connectionSocket) throws IOException {
		this.connectionSocket = connectionSocket;
		th = new TransmissionHandler(connectionSocket);
	}

	public static void readParams(String[] args) {
		int i = 0;
		while (i < args.length) {
			if (args[i++].compareTo("-p") == 0) {
				if (i < args.length || !args[i].matches("[0-9]+"))
					throw new IllegalArgumentException("You need to specify a valid port after '-p'");
				int port = Integer.parseInt(args[i]);
				if (port < 0 || port > 65535)
					throw new IllegalArgumentException("Port numbers p does not match 0>=p<=65535");
			} else {
				throw new IllegalArgumentException();
			}
		}
	}

	public static void main(String[] args) {

		Cartesian.start();
		Logger log = Logger.getLogFile();
		log.println("System log file");

		readParams(args);

		ServerSocket welcomeSocket = null;
		try {
			welcomeSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			throw new RuntimeException("Could not create Socket on Port " + PORT);
		}
		System.out.println("Started Server on port " + PORT);
		log.println("Started Server on port " + PORT);

		while (true) {
			Socket connectionSocket;
			try {
				connectionSocket = welcomeSocket.accept();
			} catch (IOException e) {
				System.out.println("Connection establishment failed.");
				continue;
			}
			System.out.println("Connection established to " + connectionSocket.getInetAddress());
			log.println("Connection established to " + connectionSocket.getInetAddress());
			try {
				new Thread(new Run(connectionSocket)).start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		int modus;

		Server server = null;
		try {
			server = server = new Server(th);
		} catch (IOException e) {
			e.printStackTrace();
		}
		;
		while (connectionSocket.isConnected()) {
			try {
				modus = th.getModus();
				switch (modus) {
					case MODUS_BEACON_BROADCAST:
						//System.out.println("MODUS_BEACON_BROADCAST");
						server.runBeacon();
						break;
					case MODUS_BEACON_CALIBRATE:
						System.out.println("MODUS_BEACON_CALIBRATE");
						BeaconCalibration.calibrate(th);
						break;
					case MODUS_SMARTPHONE_SENSORS:
						System.out.println("MODUS_SMARTPHONE_SENSORS");
						server.runSmartphoneSensors();
						break;
					default:
						throw new RuntimeException("Wrong Modus " + modus);
				}
			} catch (IOException e) {
				try {
					connectionSocket.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return;
			}
		}
	}
}
