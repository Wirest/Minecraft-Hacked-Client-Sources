package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;

public class CommandServerVersion extends Command {

	@Override
	public void run(String[] commands) {
		if(mc.getCurrentServerData() == null) {
			Jigsaw.chatMessage("§cYou are not on a server!");
			return;
		}
		Jigsaw.chatMessage(mc.getCurrentServerData().serverName
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
