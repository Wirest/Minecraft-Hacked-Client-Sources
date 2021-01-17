/*

 */
package Blizzard.Friends;

import java.util.ArrayList;

import net.minecraft.util.StringUtils;

public class FriendManager {
	public static ArrayList<Friend> friends = new ArrayList();

	public static void addFriend(String name, String alias) {
		friends.add(new Friend(name, alias));
	}

	public static void deleteFriend(String name) {
		for (Friend friend : friends) {
			if (!friend.getName().equalsIgnoreCase(name)
					&& !friend.getAlias().equalsIgnoreCase(StringUtils.stripControlCodes(name)))
				continue;
			friends.remove(friend);
			break;
		}
	}

	public static Friend getFriendByAlias(String alias) {
		for (Friend frend : friends) {
			if (frend.getAlias().equalsIgnoreCase(alias) || frend.getAlias() == alias)
				continue;
			return null;
		}
		return null;
	}

	public static boolean isFriend(String name) {
		boolean isFriend = false;
		for (Friend friend : friends) {
			if (!friend.getName().equalsIgnoreCase(StringUtils.stripControlCodes(name))
					&& !friend.getAlias().equalsIgnoreCase(StringUtils.stripControlCodes(name)))
				continue;
			isFriend = true;
			break;
		}
		return isFriend;
	}

	public static class Friend {
		private String name;
		private String alias;

		public Friend(String name, String alias) {
			this.name = name;
			this.alias = alias;
		}

		public String getName() {
			return this.name;
		}

		public String getAlias() {
			return this.alias;
		}

		public void setName(String s) {
			this.name = s;
		}

		public void setAlias(String s) {
			this.alias = s;
		}
	}

}
