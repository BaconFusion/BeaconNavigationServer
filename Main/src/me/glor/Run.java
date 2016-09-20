package me.glor;

import me.glor.BeaconNavigation.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by glor on 9/14/16.
 */
public class Run {
	public static final String logFilePrefix = "./data";
	public static final String logFileSuffix = ".log";
	public static int PORT = 6788;

	public static void main(String[] args) {
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
				throw new IllegalArgumentException("Unknown Argument: " + args[i]);
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
			new Thread(new Server(connectionSocket)).start();
		}
	}
}
