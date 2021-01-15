package me.robbanrobbin.jigsaw.client.commands;

import java.net.URI;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.tools.IPGetter;

public class CommandGeoPlayer extends Command {

	@Override
	public void run(String[] commands) {
		try {
			if (commands.length < 2) {
				Jigsaw.chatMessage("§6Enter a player!");
				return;
			}
			String ip = IPGetter.getIpForPlayer(commands[1]);
			if (ip.indexOf("No IPs found") != -1) {
				Jigsaw.chatMessage("§9[IpLookup]§7",
						"§7No IPs found related to " + "§c" + commands[1] + "§7. Unable to geolocate!");
				return;
			}
			try {
				Class<?> oclass = Class.forName("java.awt.Desktop");
				Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
				oclass.getMethod("browse", new Class[] { URI.class }).invoke(object,
						new Object[] { new URI("https://geoiptool.com/en/?ip=" + ip) });
			} catch (Exception e) {
				Jigsaw.chatMessage("§cCouldn't open link!");
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getActivator() {
		return ".geoplayer";
	}

	@Override
	public String getSyntax() {
		return ".geoplayer <player>";
	}

	@Override
	public String getDesc() {
		return "Looks up the player's ip on a geo ip website (if it finds any)";
	}
}
