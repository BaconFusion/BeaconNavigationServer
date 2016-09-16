package me.glor.BeaconNavigationServer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by glor on 9/14/16.
 */
public class Run {
	public static final int PORT = 6788;
	public static final String logFilePrefix = "./data";
	public static final String logFileSuffix = ".log";

	public static int logFileCounter = 0;

	public static PrintWriter getLogFile() {
		logFileCounter++;
		FileWriter fw = null;
		try {
			fw = new FileWriter(logFilePrefix + logFileCounter + logFileSuffix);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		PrintWriter pw = new PrintWriter(fw);
		return pw;
	}

	public static void main(String[] args) {

		PrintWriter log = getLogFile();
		log.println("System log file");
		log.flush();


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
