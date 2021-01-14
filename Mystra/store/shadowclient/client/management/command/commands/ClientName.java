package store.shadowclient.client.management.command.commands;

import java.util.Arrays;
import java.util.List;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.management.command.Command;

public class ClientName extends Command {

	private final List chars = Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'k', 'm', 'o', 'l', 'n', 'r');
	
	public ClientName(String[] names, String description) {
		super(names, description);
	}

	@Override
	public String getAlias() {
		return "clientname";
	}

	@Override
	public String getDescription() {
		return "Change text displayed on watermark.";
	}

	@Override
	public String getSyntax() {
		return "Clientname set <name>";
	}

	@Override
	public String executeCommand(String line, String[] args) {
		if(args[0].equalsIgnoreCase("")) {
			Shadow.addChatMessage(getSyntax());
		}
		if(args[1].equalsIgnoreCase("set")) {
			Shadow.addChatMessage(getSyntax());
		}
		if (args.length >= 2) {
	         StringBuilder string = new StringBuilder();

	         for(int i = 1; i < args.length; ++i) {
	            String tempString = args[i];
	            tempString = tempString.replace('&', '§');
	            string.append(tempString).append(" ");
	         }

	         Shadow.addChatMessage(String.format("Changed client name to '%s§7'.", string.toString().trim(), Shadow.instance.uiname));
	         Shadow.instance.uiname = string.toString().trim();
	      } else {
	         this.getSyntax();
	      }
		return line;
	}

}
