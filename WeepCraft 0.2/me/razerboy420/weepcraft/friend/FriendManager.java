/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.friend;

import java.util.ArrayList;
import me.razerboy420.weepcraft.friend.Friend;

public class FriendManager {
    public static ArrayList<Friend> friends = new ArrayList();

    public static void addFriend(String name, String alias) {
        friends.add(new Friend(name, alias));
    }

    public static void removeFriend(String name) {
        if (FriendManager.getFriend(name) != null) {
            friends.remove(FriendManager.getFriend(name));
        }
    }

    public static boolean isFriend(String name) {
        if (FriendManager.getFriend(name) != null) {
            return true;
        }
        return false;
    }

    public static Friend getFriend(String name) {
        for (Friend f : friends) {
            if (!f.name.equalsIgnoreCase(name)) continue;
            return f;
        }
        return null;
    }
}

