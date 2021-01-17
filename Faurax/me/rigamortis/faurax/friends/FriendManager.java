package me.rigamortis.faurax.friends;

import java.util.*;
import net.minecraft.util.*;

public class FriendManager
{
    public static ArrayList<Friend> friends;
    
    static {
        FriendManager.friends = new ArrayList<Friend>();
    }
    
    public static void addFriend(final String name, final String alias) {
        FriendManager.friends.add(new Friend(name, alias));
    }
    
    public static void removeFriend(final String name) {
        for (final Friend friend : FriendManager.friends) {
            if (friend.getName().equalsIgnoreCase(name)) {
                FriendManager.friends.remove(friend);
                break;
            }
        }
    }
    
    public static boolean isFriend(final String name) {
        boolean isFriend = false;
        for (final Friend friend : FriendManager.friends) {
            if (friend.getName().equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
                isFriend = true;
                break;
            }
        }
        return isFriend;
    }
}
