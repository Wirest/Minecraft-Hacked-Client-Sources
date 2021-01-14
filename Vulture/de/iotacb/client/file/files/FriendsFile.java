package de.iotacb.client.file.files;

import de.iotacb.client.Client;
import de.iotacb.client.file.ClientFile;
import de.iotacb.client.friends.Friend;

public class FriendsFile extends ClientFile {

	public FriendsFile(String path) {
		super(path);
	}
	
	public void saveFriends() {
		String content = "";
		for (Friend friend : Client.INSTANCE.getFriendManager().getFriends()) {
			content += friend.getPlayerName() + (friend.getAliasName().isEmpty() ? "":  ":" + friend.getAliasName()) + "\n";
		}
		saveFile(content);
	}
	
	public void readFriends() {
		final String content = loadFile();
		if (content.isEmpty()) return;
		final String[] lines = content.split("\n");
		for (String line : lines) {
			if (line.contains(":")) {
				Client.INSTANCE.getFriendManager().addFriend(line.split(":")[0], line.split(":")[1]);
			} else {
				Client.INSTANCE.getFriendManager().addFriend(line);
			}
		}
	}
}
