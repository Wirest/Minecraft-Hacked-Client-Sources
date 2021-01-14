package store.shadowclient.client.management.command;

import java.util.ArrayList;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.management.command.commands.*;

public class CommandManager {

	private static ArrayList<Command> commands;
	
	public CommandManager() {
		commands = new ArrayList();
		addCommand(new Bind(new String[] {"bind"}, "Allows you to bind a module."));
		addCommand(new Toggle(new String[] {"toggle"}, "Allows you to toggle a module."));
		addCommand(new Help(new String[] {"help"}, "Shows you this."));
		addCommand(new Friend(new String[] {"friend"}, "Allows you to add friends."));
		addCommand(new PluginFinder(new String[] {"plugins"}, "Shows you the server plugins."));
		addCommand(new ClientName(new String[] {"clientname"}, "Change text displayed on watermark."));
		addCommand(new Settings(new String[] {"settings"}, "Auto setting modules."));
		addCommand(new Settings(new String[] {"ping"}, "Shows your ping."));
		addCommand(new Insult(new String[] {"insult"}, "Insults a player."));
		addCommand(new Author(new String[] {"author"}, "Shows author / creator."));
	}
	
	public static void addCommand(Command c) {
		commands.add(c);
	}
	
	public static ArrayList<Command> getCommands(){
		return commands;
	}
	
	public void callCommand(String input) {
		String[] split = input.split(" ");
		String command = split[0];
		String args = input.substring(command.length()).trim();
		for(Command c: getCommands()) {
			if(c.getAlias().equalsIgnoreCase(command)) {
				try {
					c.executeCommand(args, args.split(" "));
				}catch(Exception e) {
					Shadow.addChatMessage("Invalid Command Usage");
					Shadow.addChatMessage(c.getSyntax());
				}
				return;
			}
		}
		Shadow.addChatMessage("Command not found");
	}
}
