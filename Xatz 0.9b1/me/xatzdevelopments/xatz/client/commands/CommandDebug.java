package me.xatzdevelopments.xatz.client.commands;

import me.xatzdevelopments.xatz.client.main.Xatz;

public class CommandDebug extends Command {

	@Override
	public void run(String[] commands) {
		Xatz.debugMode = !Xatz.debugMode;
	}

	@Override
	public String getActivator() {
		return ".debug";
	}

	@Override
	public String getSyntax() {
		return ".debug";
	}

	@Override
	public String getDesc() {
		return "Turns debug mode on";
	}
}
