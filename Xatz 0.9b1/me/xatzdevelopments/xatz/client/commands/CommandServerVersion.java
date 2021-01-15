package me.xatzdevelopments.xatz.client.commands;

import me.xatzdevelopments.xatz.client.main.Xatz;

public class CommandServerVersion extends Command {

	@Override
	public void run(String[] commands) {
		if(mc.getCurrentServerData() == null) {
			Xatz.chatMessage("§cYou are not on a server!");
			return;
		}
		Xatz.chatMessage(mc.getCurrentServerData().serverName
				+ " : " + mc.getCurrentServerData().gameVersion);
	}

	@Override
	public String getActivator() {
		return ".version";
	}

	@Override
	public String getSyntax() {
		return ".version";
	}

	@Override
	public String getDesc() {
		return "Prints the server version";
	}
}
