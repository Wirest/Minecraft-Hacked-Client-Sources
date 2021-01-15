package info.spicyclient.chatCommands.commands;

import java.util.ArrayList;
import java.util.List;

import info.spicyclient.SpicyClient;
import info.spicyclient.chatCommands.Command;
import info.spicyclient.chatCommands.CommandManager;
import info.spicyclient.notifications.Color;
import info.spicyclient.notifications.NotificationManager;
import info.spicyclient.notifications.Type;
import info.spicyclient.settings.ModeSetting;

public class Nickname extends Command {

	public Nickname() {
		super("nick", "nick <name>", 1);
	}
	
	@Override
	public void commandAction(String message) {
		
		String[] splitMessage = message.split(" ");
		String nickname = "";
		
		for (String s : splitMessage) {
			nickname += s + " ";
		}
		
		nickname = nickname.replaceFirst(SpicyClient.commandManager.prefix + "nick ", "");
		nickname = nickname.substring(0, nickname.length() - 1);
		
		if (!SpicyClient.config.hideName.mode.modes.contains(nickname))
			SpicyClient.config.hideName.mode.modes.add(nickname);
		
		SpicyClient.config.hideName.mode.index = SpicyClient.config.hideName.mode.modes.indexOf(nickname);
		
		if (!SpicyClient.config.hideName.isEnabled())
			SpicyClient.config.hideName.toggle();
		
		NotificationManager.getNotificationManager().createNotification("Nickname changed", "Changed nickname to " + nickname, true, 3000, Type.INFO, Color.PINK);
		
	}
	
}
