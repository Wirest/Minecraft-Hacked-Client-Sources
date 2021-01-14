package store.shadowclient.client.management.file.files;

import store.shadowclient.client.Shadow;
import store.shadowclient.client.management.file.ClientFile;
import store.shadowclient.client.management.command.variables.Friend;

public class FriendsFile extends ClientFile {

	public FriendsFile(String path) {
		super(path);
	}
	
	public void saveFriends() {
		String content = "";
		for (Friend friend : Shadow.instance.friendManager.getFriends()) {
			content += friend.getUsername() + (friend.getAlias().isEmpty() ? "":  ":" + friend.getAlias()) + "\n";
		}
		saveFile(content);
	}
	
	public void readFriends() {
		final String content = loadFile();
		if (content.isEmpty()) return;
		final String[] lines = content.split("\n");
		for (String line : lines) {
			if (line.contains(":")) {
				Shadow.instance.friendManager.add(line.split(":")[0], line.split(":")[1]);
			} else {
				Shadow.instance.friendManager.add(line);
			}
		}
	}
}
