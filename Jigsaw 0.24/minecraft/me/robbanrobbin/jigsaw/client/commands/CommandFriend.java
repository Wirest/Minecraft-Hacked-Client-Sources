package me.robbanrobbin.jigsaw.client.commands;

import me.robbanrobbin.jigsaw.client.main.Jigsaw;
import me.robbanrobbin.jigsaw.gui.Level;
import me.robbanrobbin.jigsaw.gui.Notification;

public class CommandFriend extends Command {

	@Override
	public void run(final String[] commands) {
		if (commands.length < 2) {
			Jigsaw.chatMessage("§cEnter a player name!");
			return;
		}
		if (Jigsaw.getFriendsMananger().isFriend(commands[1])) {
			Jigsaw.getNotificationManager().addNotification(new Notification(Level.INFO, "Added " + commands[1] + " as a friend!"));
			Jigsaw.getFriendsMananger().removeFriend(commands[1]);
		} else {
			Jigsaw.getNotificationManager().addNotification(new Notification(Level.INFO, "Removed friend: " + commands[1] + "!"));
			Jigsaw.getFriendsMananger().getFriends().add(commands[1]);
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
