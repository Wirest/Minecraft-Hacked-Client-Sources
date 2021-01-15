// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.friend;

import java.util.List;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StringUtils;
import me.aristhena.utils.FileUtils;
import java.util.ArrayList;
import java.io.File;

public class FriendManager
{
    private static final File FRIEND_DIR;
    public static ArrayList<Friend> friendsList;
    
    static {
        FRIEND_DIR = FileUtils.getConfigFile("Friends");
        FriendManager.friendsList = new ArrayList<Friend>();
    }
    
    public static void start() {
        load();
        save();
    }
    
    public static void addFriend(final String name, final String alias) {
        FriendManager.friendsList.add(new Friend(name, alias));
        save();
    }
    
    public static String getAliasName(final String name) {
        String alias = "";
        for (final Friend friend : FriendManager.friendsList) {
            if (friend.name.equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
                alias = friend.alias;
                break;
            }
        }
        if (Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().thePlayer.getGameProfile().getName() == name) {
            return name;
        }
        return alias;
    }
    
    public static void removeFriend(final String name) {
        for (final Friend friend : FriendManager.friendsList) {
            if (friend.name.equalsIgnoreCase(name)) {
                FriendManager.friendsList.remove(friend);
                break;
            }
        }
        save();
    }
    
    public static String replaceText(String text) {
        for (final Friend friend : FriendManager.friendsList) {
            if (text.contains(friend.name)) {
                text = friend.alias;
            }
        }
        return text;
    }
    
    public static boolean isFriend(final String name) {
        boolean isFriend = false;
        for (final Friend friend : FriendManager.friendsList) {
            if (friend.name.equalsIgnoreCase(StringUtils.stripControlCodes(name))) {
                isFriend = true;
                break;
            }
        }
        if (Minecraft.getMinecraft().thePlayer.getGameProfile().getName() == name) {
            isFriend = true;
        }
        return isFriend;
    }
    
    public static void load() {
        FriendManager.friendsList.clear();
        final List<String> fileContent = FileUtils.read(FriendManager.FRIEND_DIR);
        for (final String line : fileContent) {
            try {
                final String[] split = line.split(":");
                final String name = split[0];
                final String alias = split[1];
                addFriend(name, alias);
            }
            catch (Exception ex) {}
        }
    }
    
    public static void save() {
        final List<String> fileContent = new ArrayList<String>();
        for (final Friend friend : FriendManager.friendsList) {
            final String alias = getAliasName(friend.name);
            fileContent.add(String.format("%s:%s", friend.name, alias));
        }
        FileUtils.write(FriendManager.FRIEND_DIR, fileContent, true);
    }
}
