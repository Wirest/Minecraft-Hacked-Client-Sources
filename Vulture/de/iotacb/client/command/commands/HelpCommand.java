package de.iotacb.client.command.commands;

import de.iotacb.client.Client;
import de.iotacb.client.command.Command;
import de.iotacb.client.command.CommandInfo;

@CommandInfo(names = {"Help", "H"}, description = "Prints a list of all commands", usage = "#help")
public class HelpCommand extends Command {

	@Override
	public void fireCommand(String[] args) {
		Client.INSTANCE.getCommandManager().getCommands().forEach(command -> command.sendHelp());
	}

}
