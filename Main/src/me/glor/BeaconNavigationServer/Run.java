package me.glor.BeaconNavigationServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by glor on 9/14/16.
 */
public class Run {
	public static final int PORT = 6788;

	public static void main(String[] args) {

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
