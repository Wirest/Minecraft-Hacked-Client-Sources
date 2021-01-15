package me.xatzdevelopments.xatz.client.commands;

import me.xatzdevelopments.xatz.client.main.Xatz;

public class CommandBleach extends Command {

	@Override
	public void run(String[] commands) {
		Xatz.getModuleByName("Bleach").setToggled(true, true);
	}

	@Override
	public String getActivator() {
		return ".bleach";
	}

	@Override
	public String getSyntax() {
		return ".bleach";
	}

	@Override
	public String getDesc() {
		return "Kills you.";
	}
}
