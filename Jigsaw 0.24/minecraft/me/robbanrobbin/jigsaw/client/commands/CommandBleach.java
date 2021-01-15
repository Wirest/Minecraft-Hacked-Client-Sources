package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;

public class CommandBleach extends Command {

	@Override
	public void run(String[] commands) {
		Jigsaw.getModuleByName("Bleach").setToggled(true, true);
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
