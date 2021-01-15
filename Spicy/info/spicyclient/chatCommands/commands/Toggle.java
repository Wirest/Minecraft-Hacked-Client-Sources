package info.spicyclient.chatCommands.commands;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;
import info.spicyclient.modules.Module;

public class Toggle extends Command{

	public Toggle() {
		super("toggle", "toggle <module>", 1);
	}
	
	@Override
	public void commandAction(String message) {
		
		String[] splitMessage = message.split(" ");
		String moduleName = "";
		
		for (String s : splitMessage) {
			moduleName += s + " ";
		}
		
		moduleName = moduleName.replaceFirst(".toggle ", "");
		
		for (Module m : SpicyClient.modules) {
			
			if ((m.name.replace(" ", "")).equalsIgnoreCase(moduleName.replace(" ", ""))) {
				sendPrivateChatMessage("Toggling the " + m.name.replace(" ", "") + " module...");
				m.toggle();
				return;
			}
			
		}
		
		sendPrivateChatMessage("Could not find the module");
		
	}
	
	@Override
	public void incorrectParameters() {
		sendPrivateChatMessage("Please use .toggle <module name>");
	}
	
}
