package me.xatzdevelopments.xatz.friends;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;

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
	
	public String replaceNames(String message, final boolean color) {
        if (!message.startsWith("§6[§lE§r§6]§f ")) {
            try {
                for (final String name : this.friends) {
                    message = message.replaceAll("(?i)" + name, Matcher.quoteReplacement(color ? ("§e§l§k|§r§e§l" + (String)this.friends.get(0) + "§e§l§k|§r§f") : ((String)this.friends.get(0))));
                }
            }
            catch (Exception ex) {}
        }
        return message;
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
