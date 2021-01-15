package me.xatzdevelopments.xatz.client.commands;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.modules.HighJump;

public class CommandSpider extends Command {

	@Override
	public void run(String[] commands) {
		if (commands.length < 2) {
			Xatz.chatMessage("Enter two values!");
			return;
		}
		int height = 0;
		try {
			height = Integer.parseInt(commands[1]);
		} catch (NumberFormatException e) {
			Xatz.chatMessage("§cPlease enter a valid number!");
			return;
		}
		HighJump.height = height;
		HighJump.delay = 0;
		//Xatz/.chatMessage(delay);

		Xatz.getModuleByName("HighJump").setToggled(true, true);
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
