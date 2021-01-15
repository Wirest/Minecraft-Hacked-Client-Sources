package me.xatzdevelopments.xatz.client.commands;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.client.modules.NameProtect;

public class CommandNameprotect extends Command {

	@Override
	public void run(String[] commands) {
		if (commands.length < 3) {
			Xatz.chatMessage("§cEnter two names!");
			return;
		}
		Xatz.chatMessage(commands[1] + " = " + commands[2]);
		((NameProtect) Xatz.getModuleByName("NameProtect")).replacements.put(commands[1], commands[2]);
		return;
	}

	@Override
	public String getActivator() {
		return ".nameprotect";
	}

	@Override
	public String getSyntax() {
		return ".nameprotect <name> <replacement>";
	}

	@Override
	public String getDesc() {
		return "Adds a player as a fakehacker for the mod \"fakehackers\"";
	}
}
