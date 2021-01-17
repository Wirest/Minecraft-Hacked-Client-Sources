package me.ihaq.iClient.command.commands;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.command.Command;
import me.ihaq.iClient.modules.Module;

public class Toggle extends Command {
	public Toggle() {
		super("Toggle", "<Module>");
	}

	@Override
	public void run(final String message) {
		final Module m = Module.getModuleByString(message.split(" ")[1]);
		if (message.split(" ").length == 2) {
			if (message.split(" ")[1].equalsIgnoreCase(m.name)) {
				if (m.isToggled()) {
					m.setToggled(false);
				} else {
					m.setToggled(true);
				}
			}
		} else {
			Envy.tellPlayer("Incorrect Syntax! toggle <Module>");
		}
	}
}