package me.robbanrobbin.jigsaw.friends;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class FriendsMananger {

	private Minecraft mc = Minecraft.getMinecraft();

	private ArrayList<String> friends;

	public FriendsMananger() {
		friends = new ArrayList<String>();
	}

	public boolean isFriend(EntityPlayer en) {
		for (String name : friends) {
			if (mc.theWorld.getPlayerEntityByName(name) == null) {
				continue;
			}
			if (en.isEntityEqual(mc.theWorld.getPlayerEntityByName(name))) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<String> getFriends() {
		return friends;
	}

	public void removeFriend(EntityPlayer en) {
		Iterator<String> iter = friends.iterator();

		while (iter.hasNext()) {
			String name = iter.next();
			if (mc.theWorld.getPlayerEntityByName(name) == null) {
				continue;
			}
			if (en.isEntityEqual(mc.theWorld.getPlayerEntityByName(name))) {
				iter.remove();
			}
		}
	}

	public void removeFriend(String en) {
		Iterator<String> iter = friends.iterator();

		while (iter.hasNext()) {
			if (iter.next().equals(en)) {
				iter.remove();
			}
		}
	}

	public boolean isFriend(String string) {
		return friends.contains(string);
	}
}
