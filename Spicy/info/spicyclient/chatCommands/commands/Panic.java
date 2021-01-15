package info.spicyclient.chatCommands.commands;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;
import info.spicyclient.modules.Module;

public class Panic extends Command {

	public Panic() {
		super("panic", "panic", 0);
	}
	
	@Override
	public void commandAction(String message) {
		
		for (Module m : SpicyClient.modules) {
			
			if (m.isEnabled()) {
				m.toggle();
			}
			
		}
		
	}
	
}
