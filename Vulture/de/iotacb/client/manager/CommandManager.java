package de.iotacb.client.manager;

import java.util.ArrayList;
import java.util.List;

import de.iotacb.client.command.Command;
import de.iotacb.client.command.commands.BindCommand;
import de.iotacb.client.command.commands.ClearCommand;
import de.iotacb.client.command.commands.ConfigCommand;
import de.iotacb.client.command.commands.FriendCommand;
import de.iotacb.client.command.commands.HelpCommand;
import de.iotacb.client.command.commands.IRCCommand;
import de.iotacb.client.command.commands.NameCommand;
import de.iotacb.client.command.commands.NameProtectCommand;
import de.iotacb.client.command.commands.SayCommand;
import de.iotacb.client.command.commands.ToggleCommand;

public class CommandManager {
	
	private final List<Command> commands;
	
	public CommandManager() {
		commands = new ArrayList<Command>();
		
		addCommands(BindCommand.class,
					ClearCommand.class,
					ConfigCommand.class,
					SayCommand.class,
					ToggleCommand.class,
					FriendCommand.class,
					NameCommand.class,
					NameProtectCommand.class,
					HelpCommand.class,
					IRCCommand.class
					);
	}
	
	public void addCommands(final Class <?extends Command>...classes) {
		for (final Class <?extends Command> command : classes) {
			try {
				commands.add(command.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void handleCommands(final String message) {
		final String[] args = message.split(" ");
		getCommands().forEach(command -> {
			if (command.equalsInvoke(args[0].substring(1))) {
				command.fireCommand(args);
			}
		});
	}
	
	public List<Command> getCommands() {
		return commands;
	}

}
