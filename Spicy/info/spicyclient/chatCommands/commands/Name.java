package info.spicyclient.chatCommands.commands;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;

public class Name extends Command {

	public Name() {
		super("name", "name <clientName/reset>", 1);
	}
	
	@Override
	public void commandAction(String message) {
		
		String[] splitMessage = message.split(" ");
		String clientName = "";
		
		for (String s : splitMessage) {
			clientName += s + " ";
		}
		
		clientName = clientName.replaceFirst(".name ", "");
		
		if (clientName.toLowerCase().contains("reset")) {
			info.spicyclient.files.Config temp = new info.spicyclient.files.Config("temp");
			SpicyClient.config.clientName = temp.clientName;
			SpicyClient.config.clientVersion = SpicyClient.config.version;
			sendPrivateChatMessage("Reset the client name");
			return;
		}
		
		SpicyClient.config.clientName = clientName.substring(0, clientName.length() - 1);
		SpicyClient.config.clientVersion = "GET_CHANGED_ON_STEAM";
		sendPrivateChatMessage("Set the client name to " + clientName);
		
	}
	
	@Override
	public void incorrectParameters() {
		sendPrivateChatMessage("Please use .name <clientName>");
	}
	
}
