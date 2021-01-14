package de.iotacb.client.manager;

import java.util.ArrayList;
import java.util.List;

import de.iotacb.client.friends.Friend;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;

public class FriendManager {

	private final List<Friend> friends = new ArrayList<Friend>();
	
	public List<Friend> getFriends() {
		return friends;
	}
	
	public void addFriend(final Entity entity) {
		getFriends().add(new Friend(entity.getName()));
	}
	
	public void addFriend(final Entity entity, String alias) {
		getFriends().add(new Friend(entity.getName(), alias));
	}
	
	public void addFriend(final String playerName) {
		getFriends().add(new Friend(playerName));
	}
	
	public void addFriend(final String playerName, String alias) {
		getFriends().add(new Friend(playerName, alias));
	}
	
	public void removeFriend(final Entity entity) {
		getFriends().removeIf(friend -> friend.getPlayerName().equals(entity.getName()));
	}
	
	public void removeFriend(final String playerName) {
		getFriends().removeIf(friend -> friend.getPlayerName().equals(playerName));
	}
	
	public boolean isFriend(final String playerName) {
		return getFriends().stream().filter(friend -> friend.getPlayerName().equals(playerName)).findFirst().orElse(null) != null;
	}
	
	public boolean isFriend(final Entity entity) {
		return isFriend(entity.getName());
	}
}
