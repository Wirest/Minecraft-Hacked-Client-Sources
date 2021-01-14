package de.iotacb.client.irc;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class IRC {

	private String username;
	private IRCReceiver receiver;
	private boolean connected;
	
	public IRC(final String username, final String host, final int port) {
		this.username = username;
		
		try {
			final Socket socket = new Socket(host, port);
			final Scanner in = new Scanner(socket.getInputStream());
			final PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			final Scanner inp = new Scanner(System.in);
			
			receiver = new IRCReceiver(socket, in, out, inp, username);
			receiver.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getUsername() {
		return username;
	}
	
	public IRCReceiver getReceiver() {
		return receiver;
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public void setConnected(final boolean connected) {
		this.connected = connected;
	}
	
}
