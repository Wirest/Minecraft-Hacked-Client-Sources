package me.xatzdevelopments.xatz.client.commands;

import java.net.URI;

import me.xatzdevelopments.xatz.client.main.Xatz;

public class CommandGeoIp extends Command {

	@Override
	public void run(String[] commands) {
		try {
			if (commands.length < 2) {
				Xatz.chatMessage("§6Enter an IPv4 address!");
				return;
			}
			String ip = commands[1];
			try {
				Class<?> oclass = Class.forName("java.awt.Desktop");
				Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
				oclass.getMethod("browse", new Class[] { URI.class }).invoke(object,
						new Object[] { new URI("https://geoiptool.com/en/?ip=" + ip) });
			} catch (Exception e) {
				Xatz.chatMessage("§cCouldn't open link!");
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getActivator() {
		return ".geoip";
	}

	@Override
	public String getSyntax() {
		return ".geoip <ip>";
	}

	@Override
	public String getDesc() {
		return "Looks up the ip on a geo ip website";
	}
}
