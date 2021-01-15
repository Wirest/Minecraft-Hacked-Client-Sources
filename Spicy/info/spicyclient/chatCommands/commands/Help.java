package info.spicyclient.chatCommands.commands;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class Help extends Command {

	public Help() {
		super("help", "help", 0);
	}
	
	@Override
	public void commandAction(String message) {
		
		sendPrivateChatMessage("-----Help Menu-----");
		
		for (Command c : SpicyClient.commandManager.commands) {
			sendPrivateChatMessage(SpicyClient.commandManager.prefix + c.helpText);
		}
		
		sendPrivateChatMessage("-------------------");
		
	}
	
}
