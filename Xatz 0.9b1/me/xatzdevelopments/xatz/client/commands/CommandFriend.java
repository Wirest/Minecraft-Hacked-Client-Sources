package me.xatzdevelopments.xatz.client.commands;

import me.xatzdevelopments.xatz.client.main.Xatz;
import me.xatzdevelopments.xatz.gui.Level;
import me.xatzdevelopments.xatz.gui.Notification;

public class CommandFriend extends Command {

	@Override
	public void run(final String[] commands) {
		if (commands.length < 2) {
			Xatz.chatMessage("§cEnter a player name!");
			return;
		}
		if (Xatz.getFriendsMananger().isFriend(commands[1])) {
			Xatz.getNotificationManager().addNotification(new Notification(Level.INFO, "Added " + commands[1] + " as a friend!"));
			Xatz.getFriendsMananger().removeFriend(commands[1]);
		} else {
			Xatz.getNotificationManager().addNotification(new Notification(Level.INFO, "Removed friend: " + commands[1] + "!"));
			Xatz.getFriendsMananger().getFriends().add(commands[1]);
		}
	}

	@Override
	public String getActivator() {
		return ".friend";
	}

	@Override
	public String getSyntax() {
		return ".friend <player>";
	}

	@Override
	public String getDesc() {
		return "Adds a player as a friend";
	}
}
