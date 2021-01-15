package me.robbanrobbin.jigsaw.client.commands;

import java.util.Collection;
import java.util.Iterator;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.tools.IPGetter;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;

public class CommandGetIpAll extends Command {

	@Override
	public void run(final String[] commands) {
		try {
			Collection<NetworkPlayerInfo> itr = mc.getNetHandler().getPlayerInfoMap();
			for (final NetworkPlayerInfo info : itr) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						String name = StringUtils.stripControlCodes(info.getGameProfile().getName());
						if(name == null) {
							return;
						}
						final String ip = IPGetter.getIpForPlayer(name);
						mc.addScheduledTask(new Runnable() {
							@Override
							public void run() {
								if (ip.indexOf("No IPs found") == -1) {
									Jigsaw.chatMessage("§9[IpLookup §7- §6" + commands[1] + "§9]§7",
											"Results: §6" + ip + " §7Do §b.geoip §b<ip>§7 for location lookup!");
								} else {
									Jigsaw.chatMessage("§9[IpLookup]§7", "Results: §6" + ip);
								}
							}
						});
					}
				}).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getActivator() {
		return ".ipall";
	}

	@Override
	public String getSyntax() {
		return ".ipall";
	}

	@Override
	public String getDesc() {
		return "Looks up all the loaded players' IPs";
	}
}
