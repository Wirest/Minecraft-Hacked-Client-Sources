package de.iotacb.client.irc;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class IRCSender extends Thread {
	Socket socket;
	Scanner in;
	PrintWriter out;
	Scanner inp;

	public IRCSender(final Socket socket, final Scanner in, final PrintWriter out, final Scanner inp) {
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.inp = inp;
	}

	public void run() {
		while (true) {}
	}
	
	public void addMessage(final String message) {
		out.println(message);
	}
}