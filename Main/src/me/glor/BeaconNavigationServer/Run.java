package me.glor.BeaconNavigationServer;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by glor on 9/14/16.
 */
public class Run {
	public static final int PORT = 6789;

	public static void main(String[] args) {
		String clientSentence;
		String capitalizedSentence;
		ServerSocket welcomeSocket = null;
		try {
			welcomeSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			System.out.println("Could not create Socket on Port" + PORT);
		}
		System.out.println("Starting Server on port " + PORT);
		while (true) {
			Socket connectionSocket = null;
			try {
				connectionSocket = welcomeSocket.accept();
			} catch (IOException e) {
				System.out.println("Connection establishment failed.");
				continue;
			}
			BufferedReader inFromClient;
			DataOutputStream outToClient;
			try {
				inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
				outToClient = new DataOutputStream(connectionSocket.getOutputStream());
				clientSentence = inFromClient.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
			System.out.println("Received: " + clientSentence);
			capitalizedSentence = clientSentence.toUpperCase() + '\n';
			try {
				outToClient.writeBytes(capitalizedSentence);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}
	}
}
