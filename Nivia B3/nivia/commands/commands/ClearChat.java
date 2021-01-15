package nivia.commands.commands;

import nivia.commands.Command;

public class ClearChat extends Command {
	 public ClearChat() {
		 	super("ClearChat", "Clears the chat.", null, false, "cc", "clearc", "cchat");
	    } 
	 
	 @Override
		public void execute(String commandName, String[] arguments) {
		 mc.ingameGUI.getChatGUI().clearChatMessages();
	 }
	 
}
