package cn.kody.debug.friend;

import java.util.Iterator;

import cn.kody.debug.utils.Friend;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;

public class FriendsManager
{
    private static ArrayList<Friend> friends;
    
    public FriendsManager() {
    }
    
    public static ArrayList getFriends() {
        return FriendsManager.friends;
    }
    
    public static boolean isFriend(final EntityPlayer entityPlayerMP) {
        final Iterator<Friend> iterator = FriendsManager.friends.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getName().equalsIgnoreCase(entityPlayerMP.getName())) {
                return true;
            }
           
        }
        return false;
    }
    
    public static boolean isFriend(final String s) {
        final Iterator<Friend> iterator = FriendsManager.friends.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getName().equalsIgnoreCase(s)) {
                return true;
            }
            
        }
        return false;
    }
    
    public static Friend getFriend(String s) {
        for (final Friend friend : FriendsManager.friends) {
            if (friend.getName().equalsIgnoreCase(s)) {
                return friend;
            }
            
        }
        return null;
    }
    
    static {
        FriendsManager.friends = new ArrayList();
    }
}
