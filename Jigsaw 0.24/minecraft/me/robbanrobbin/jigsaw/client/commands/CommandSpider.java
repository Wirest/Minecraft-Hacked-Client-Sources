package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.client.modules.HighJump;

public class CommandSpider extends Command {

	@Override
	public void run(String[] commands) {
		if (commands.length < 2) {
			Jigsaw.chatMessage("Enter two values!");
			return;
		}
		int height = 0;
		try {
			height = Integer.parseInt(commands[1]);
		} catch (NumberFormatException e) {
			Jigsaw.chatMessage("§cPlease enter a valid number!");
			return;
		}
		HighJump.height = height;
		HighJump.delay = 0;
		// Jigsaw.chatMessage(delay);

		Jigsaw.getModuleByName("HighJump").setToggled(true, true);
	}

	@Override
	public String getActivator() {
		return ".jump";
	}

	@Override
	public String getSyntax() {
		return ".jump <jumps>";
	}

	@Override
	public String getDesc() {
		return "This only works for a small amount of people RIP!";
	}
}
