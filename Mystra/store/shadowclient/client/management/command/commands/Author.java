package store.shadowclient.client.management.command.commands;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.management.command.Command;

public class Author extends Command {

	public Author(String[] names, String description) {
		super(names, description);
	}

	@Override
	public String getAlias() {
		return "author";
	}

	@Override
	public String getDescription() {
		return "Shows author / creator";
	}

	@Override
	public String getSyntax() {
		return ".author";
	}

	@Override
	public String executeCommand(String line, String[] args) {
		if(args[0].equalsIgnoreCase("")) {
			Shadow.instance.addChatMessage("Shadow coded / made by Crystal");
		}
		return line;
			
	}

}
