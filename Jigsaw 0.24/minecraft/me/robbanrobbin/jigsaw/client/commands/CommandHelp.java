package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.module.Module;

public class CommandHelp extends Command {

	@Override
	public void run(String[] commands) {
		boolean allMods = commands.length == 1;
		if (allMods) {
			Jigsaw.chatMessage("These are all commands:");
			for (Command cmd : Jigsaw.getCommandManager().commands) {
				Jigsaw.chatMessage("§e" + cmd.getName() + "§6 §7" + cmd.getSyntax());
			}
		} else {
			if (!Jigsaw.isCommand(commands[1]) && !Jigsaw.isModuleName(commands[1])) {
				Jigsaw.chatMessage("§cCould not find command or hack: " + commands[1]);
				return;
			}
			if(Jigsaw.isCommand(commands[1])) {
				Command cmd = Jigsaw.getCommandByName(commands[1]);
				Jigsaw.chatMessage("§eName: §7" + cmd.getName());
				Jigsaw.chatMessage("§eUsage: §7" + cmd.getSyntax());
				Jigsaw.chatMessage("§eDescription: §7" + cmd.getDesc());
			}
			if(Jigsaw.isModuleName(commands[1])) {
				Module mod = Jigsaw.getModuleByName(commands[1]);
				String modes = "";
				for(String s : mod.getModes()) {
					modes += s + ", ";
				}
				if(mod.getModes().length == 1) {
					modes = "None, ";
				}
				Jigsaw.chatMessage("§eName: §7" + mod.getName());
				Jigsaw.chatMessage("§eModes: §7" + modes);
				Jigsaw.chatMessage("§eDesc: §7" + mod.description);
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
