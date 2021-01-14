package de.iotacb.client.utilities.misc;

import java.io.IOException;

import de.iotacb.client.Client;

public class FritzBoxUtil {

	public final void reconnect() {
		try {
			Client.INSTANCE.getNotificationManager().addNotification("Ip Changer", "Trying to change your IP!");
			System.out.println("Trying to run the IP change script...");
			Runtime.getRuntime().exec("wscript.exe ".concat(Client.INSTANCE.getFileManager().getVultureFolder().concat("/ip.vbs")));
			System.out.println("Your IP should change in a sec!");
		} catch (IOException e) {
			System.out.println("Failed to run the IP change script...");
			e.printStackTrace();
		}
	}
	
}
