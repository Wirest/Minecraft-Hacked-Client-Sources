package com.etb.client.command.impl;

import com.etb.client.command.Command;
import com.etb.client.Client;
import com.etb.client.command.Command;
import com.etb.client.gui.notification.Notification;
import com.etb.client.utils.Printer;

public class Friend extends Command {

	public Friend() {
		super("Friend",new String[]{"friends","friend","f"},"Friends people");
	}

	@Override
	public void onRun(final String[] args) {
		switch (args[1]) {
			case "add":
			case "a":
			case "Add":
			case "Ad":
			case "ad":
				if (args.length > 1) {
					if (Client.INSTANCE.getFriendManager().isFriend(args[2])) {
						Printer.print(args[2] + " is already your friend.");
						return;
					}
					if (args.length < 4) {
                        Client.INSTANCE.getNotificationManager().sendClientMessage("Added " + args[2] + " to your friends list without an alias.", Notification.Type.SUCCESS);

                        Printer.print("Added " + args[2] + " to your friends list without an alias.");
						Client.INSTANCE.getFriendManager().addFriend(args[2]);
					} else {
                        Client.INSTANCE.getNotificationManager().sendClientMessage("Added " + args[2] + " to your friends list.", Notification.Type.SUCCESS);

                        Printer.print("Added " + args[2] + " to your friends list with the alias " + args[3] + ".");
						Client.INSTANCE.getFriendManager().addFriendWithAlias(args[2], args[3]);
					}
				}
				break;
			case "del":
			case "delete":
			case "d":
			case "rem":
			case "remove":
			case "r":
				if (args.length > 1) {
					if (!Client.INSTANCE.getFriendManager().isFriend(args[2])) {
						Printer.print(args[2] + " is not your friend.");
						return;
					}
					if (Client.INSTANCE.getFriendManager().isFriend(args[2])) {
						Printer.print("Removed " + args[2] + " from your friends list.");
						Client.INSTANCE.getFriendManager().removeFriend(args[2]);
					}
				}
				break;
			case "c":
			case "clear":
				if (Client.INSTANCE.getFriendManager().getFriends().isEmpty()) {
					Printer.print("Your friends list is already empty.");
					return;
				}
				Printer.print("Your have cleared your friends list. Friends removed: " + Client.INSTANCE.getFriendManager().getFriends().size());
				Client.INSTANCE.getFriendManager().clearFriends();
				break;
			case "list":
			case "l":
				if (Client.INSTANCE.getFriendManager().getFriends().isEmpty()) {
					Printer.print("Your friends list is empty.");
					return;
				}
				Printer.print("Your current friends are: ");
				Client.INSTANCE.getFriendManager().getFriends().forEach(friend -> {
					Printer.print("Username: " + friend.getName() + (friend.getAlias() != null ? (" - Alias: " + friend.getAlias()) : ""));
				});
				break;
		}
	}
}
