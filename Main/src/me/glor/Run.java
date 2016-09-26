package me.glor;

import me.glor.BeaconNavigation.BeaconCalibration;
import me.glor.BeaconNavigation.Logger;
import me.glor.BeaconNavigation.TestKalman;

import java.io.DataInputStream;
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
	public static int PORT = 6788;
	Socket connectionSocket;

	private Run() {
		throw new UnsupportedOperationException();
	}

	public Run(Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
	}

	public static void main(String[] args) {
		//remove
		new Thread(new TestKalman()).start();

		Logger log = Logger.getLogFile();
		log.println("System log file");
		log.flush();

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

		ServerSocket welcomeSocket = null;
		try {
			welcomeSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println("Could not create Socket on Port " + PORT);
			System.exit(1);
		}
		System.out.println("Started Server on port " + PORT);

		while (true) {
			Socket connectionSocket;
			try {
				connectionSocket = welcomeSocket.accept();
			} catch (IOException e) {
				System.out.println("Connection establishment failed.");
				continue;
			}
			System.out.println("Connection established.");
			new Thread(new Run(connectionSocket)).start();
		}
	}

	@Override
	public void run() {
		int modus;
		Server server = null;
		while (connectionSocket.isConnected()) {
			System.out.flush();
			try {
				modus = new DataInputStream(connectionSocket.getInputStream()).readByte();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					connectionSocket.close();
				} catch (IOException e1) {
				}
				return;
			}
			System.out.flush();
			switch (modus) {
				case MODUS_BEACON_BROADCAST:
					if (server == null)
						try {
							server = new Server(connectionSocket);
						} catch (IOException e) {
							e.printStackTrace();
							return;
						}
					server.runBeacon();
					break;
				case MODUS_BEACON_CALIBRATE:
					try {
						BeaconCalibration.calibrate(connectionSocket);
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}
					break;
				case MODUS_SMARTPHONE_SENSORS:
					if (server == null)
						try {
							server = new Server(connectionSocket);
						} catch (IOException e) {
							e.printStackTrace();
							return;
						}

					break;
				default:
					System.err.println("Wrong Modus " + modus);
					continue;
			}
		}
	}
}
