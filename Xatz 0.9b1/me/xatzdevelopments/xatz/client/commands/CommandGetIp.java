package me.xatzdevelopments.xatz.client.commands;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.tools.IPGetter;

public class CommandGetIp extends Command {

	@Override
	public void run(final String[] commands) {
		try {
			if (commands.length < 2) {
				Xatz.chatMessage("§6Enter a player name!");
				return;
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					final String ip = IPGetter.getIpForPlayer(commands[1]);
					mc.addScheduledTask(new Runnable() {
						@Override
						public void run() {
							if (ip.indexOf("No IPs found") == -1) {
								Xatz.chatMessage("§9[IpLookup §7- §6" + commands[1] + "§9]§7",
										"Results: §6" + ip + " §7Do §b.geoip §b<ip>§7 for location lookup!");
							} else {
								Xatz.chatMessage("§9[IpLookup]§7", "Results: §6" + ip);
							}
						}
					});
				}
			}).start();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getActivator() {
		return ".ip";
	}

	@Override
	public String getSyntax() {
		return ".ip <player>";
	}

	@Override
	public String getDesc() {
		return "Looks up the player's name in mcresolver's database";
	}
}
