package me.ihaq.iClient.command;

import java.util.ArrayList;


import me.ihaq.iClient.command.commands.Bind;
import me.ihaq.iClient.command.commands.Color;
import me.ihaq.iClient.command.commands.Help;
import me.ihaq.iClient.command.commands.Toggle;
public class CommandManager {

	public static ArrayList<Command> commands = new ArrayList<Command>();
	public static String prefix = "-";

	public static void add(Command cmd) {
		CommandManager.commands.add(cmd);
	}

	public Command getCommandByClass(Class command) {
		for (Command command2 : commands)
			if (command2.getClass().equals(command))
				return command2;
		return null;
	}

	public void loadCommands() {
		try {
			commands.add(new Help());
			commands.add(new Bind());
			commands.add(new Toggle());
			commands.add(new Color());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void setPrefix(String prefix) {
		CommandManager.prefix = prefix;
	}

	public static ArrayList<Command> getCommands() {
		return commands;
	}

}