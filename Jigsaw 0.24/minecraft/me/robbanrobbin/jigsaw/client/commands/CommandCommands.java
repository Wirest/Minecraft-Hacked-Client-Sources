package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;

public class CommandCommands extends Command {

	@Override
	public void run(String[] commands) {
		Jigsaw.chatMessage("§6These are all available commands:");
		for (Command cmd : Jigsaw.getCommandManager().commands) {
			Jigsaw.chatMessage("§b§l" + cmd.getActivator());
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
