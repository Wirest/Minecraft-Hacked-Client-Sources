package de.iotacb.client.command.commands;

import de.iotacb.client.Client;
import de.iotacb.client.command.Command;
import de.iotacb.client.command.CommandInfo;
import de.iotacb.client.file.files.FriendsFile;
import de.iotacb.client.module.ModuleInfo;
import de.iotacb.client.utilities.misc.Printer;

@CommandInfo(names = {"Friend", "F"}, description = "Add or remove client friends", usage = "#friend PLAYERNAME [ALIAS]")
public class FriendCommand extends Command {

	@Override
	public void fireCommand(String[] args) {
		if (args.length == 2) {
			if (Client.INSTANCE.getFriendManager().isFriend(args[1].trim())) {
				Client.INSTANCE.getFriendManager().removeFriend(args[1]);
				Client.PRINTER.printMessage("Removed player '" + Client.INSTANCE.getClientColorCode() + args[1].trim() + "§f' from the friend list.");
			} else {
				Client.INSTANCE.getFriendManager().addFriend(args[1].trim());
				Client.PRINTER.printMessage("Added player '" + Client.INSTANCE.getClientColorCode() + args[1].trim() + "§f' to the friend list.");
			}
		} else if (args.length == 3) {
			if (Client.INSTANCE.getFriendManager().isFriend(args[1].trim())) {
				Client.INSTANCE.getFriendManager().removeFriend(args[1].trim());
				Client.PRINTER.printMessage("Removed player '" + Client.INSTANCE.getClientColorCode() + args[1].trim() + "§f' from the friend list.");
			} else {
				Client.INSTANCE.getFriendManager().addFriend(args[1].trim(), args[2].trim());
				Client.PRINTER.printMessage("Added player '" + Client.INSTANCE.getClientColorCode() + args[1].trim() + "§f' with the alias '§5" + args[2].trim() + "§f' to the friend list.");
			}
		} else {
			sendHelp();
		}
		save();
	}
	
	private void save() {
		((FriendsFile) Client.INSTANCE.getFileManager().getFileByClass(FriendsFile.class)).saveFriends();
	}

}
