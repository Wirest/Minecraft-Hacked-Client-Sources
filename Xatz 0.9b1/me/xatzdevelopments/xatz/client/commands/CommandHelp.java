package me.xatzdevelopments.xatz.client.commands;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.module.Module;

public class CommandHelp extends Command {

	@Override
	public void run(String[] commands) {
		boolean allMods = commands.length == 1;
		if (allMods) {
			Xatz.chatMessage("These are all commands:");
			for (Command cmd : Xatz.getCommandManager().commands) {
				Xatz.chatMessage("§e" + cmd.getName() + "§6 §7" + cmd.getSyntax());
			}
		} else {
			if (!Xatz.isCommand(commands[1]) && !Xatz.isModuleName(commands[1])) {
				Xatz.chatMessage("§cCould not find command or hack: " + commands[1]);
				return;
			}
			if(Xatz.isCommand(commands[1])) {
				Command cmd = Xatz.getCommandByName(commands[1]);
				Xatz.chatMessage("§eName: §7" + cmd.getName());
				Xatz.chatMessage("§eUsage: §7" + cmd.getSyntax());
				Xatz.chatMessage("§eDescription: §7" + cmd.getDesc());
			}
			if(Xatz.isModuleName(commands[1])) {
				Module mod = Xatz.getModuleByName(commands[1]);
				String modes = "";
				for(String s : mod.getModes()) {
					modes += s + ", ";
				}
				if(mod.getModes().length == 1) {
					modes = "None, ";
				}
				Xatz.chatMessage("§eName: §7" + mod.getName());
				Xatz.chatMessage("§eModes: §7" + modes);
				Xatz.chatMessage("§eDesc: §7" + mod.description);
			}
		}
	}

	@Override
	public String getActivator() {
		return ".help";
	}

	@Override
	public String getSyntax() {
		return ".help, .help <command>, .help <hack>";
	}

	@Override
	public String getDesc() {
		return "Gets help for all commands or for a specific command or hack";
	}
}
