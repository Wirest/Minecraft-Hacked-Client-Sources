package me.onlyeli.ice.utils;

import java.util.ArrayList;

public class FriendUtils {
	private ArrayList<String> friends;

	public FriendUtils() {
		this.friends = new ArrayList<String>();
	}

	public void addFriend(String name) {
		if (!this.getFriends().contains(name.toLowerCase())) {
			this.getFriends().add(name.toLowerCase());
			ChatUtils.sendMessageToPlayer("Username added to friends list!");
		} else {
			ChatUtils.sendMessageToPlayer("Username already found in friends list!");
		}
	}

	public void delFriend(String name) {
		if (this.getFriends().contains(name.toLowerCase())) {
			this.getFriends().remove(name.toLowerCase());
			ChatUtils.sendMessageToPlayer("Username removed from friends list!");
		} else {
			ChatUtils.sendMessageToPlayer("Username not found in friends list!");
		}
	}

	public ArrayList<String> getFriends() {
		return this.friends;
	}

	public boolean isFriend(String name) {
		for (final String friend : this.getFriends()) {
			if (friend.contains(name.toLowerCase())) {
				return true;
			}
		}
		return false;
	}
}
