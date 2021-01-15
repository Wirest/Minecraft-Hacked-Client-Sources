package me.xatzdevelopments.xatz.client.commands;

import me.xatzdevelopments.xatz.client.main.Xatz;

public class CommandCommands extends Command {

	@Override
	public void run(String[] commands) {
		Xatz.chatMessage("§6These are all available commands:");
		for (Command cmd : Xatz.getCommandManager().commands) {
			Xatz.chatMessage("§b§l" + cmd.getActivator());
		}
	}

	@Override
	public String getActivator() {
		return ".commands";
	}

	@Override
	public String getSyntax() {
		return ".commands";
	}

	@Override
	public String getDesc() {
		return "Lists all commands";
	}
}
