package me.ihaq.iClient.command.commands;

import me.ihaq.iClient.Envy;
import me.ihaq.iClient.command.Command;
import me.ihaq.iClient.command.CommandManager;

public class Help extends Command {
	public Help() {
		super("Help", "");
	}

	@Override
	public void run(final String message) {
		if (message.split(" ").length == 1) {
			Envy.tellPlayer("Here are the list of Commands:");
			for(Command cmd : CommandManager.getCommands()){
				if(cmd.getCommand().equals("Help")){
					continue;
				}
				Envy.tellPlayer(cmd.getCommand()+" "+cmd.getArguments());
			}
		} else {
			Envy.tellPlayer("Incorrect Syntax! Try -help.");
		}
	}
}