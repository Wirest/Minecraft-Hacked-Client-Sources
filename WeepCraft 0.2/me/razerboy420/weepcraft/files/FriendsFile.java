/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.files;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import me.razerboy420.weepcraft.Weepcraft;
import me.razerboy420.weepcraft.friend.Friend;
import me.razerboy420.weepcraft.friend.FriendManager;
import me.razerboy420.weepcraft.util.FileUtils;

public class FriendsFile {
    public static void load() {
        List<String> file = FileUtils.readFile(Weepcraft.configDir + File.separator + "YouHaveNoFriends.weep");
        for (String s : file) {
            try {
                if (s.startsWith("#")) continue;
                String name = s.split(":")[0];
                String alias = s.split(":")[1];
                FriendManager.addFriend(name, alias);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void save() {
        List<String> file = FileUtils.readFile(Weepcraft.configDir + File.separator + "YouHaveNoFriends.weep");
        ArrayList<String> newfile = new ArrayList<String>();
        newfile.add(FileUtils.getDateString());
        for (Friend f : FriendManager.friends) {
            newfile.add(String.valueOf(String.valueOf(f.name)) + ":" + f.alias);
        }
        FileUtils.writeFile(Weepcraft.configDir + File.separator + "YouHaveNoFriends.weep", newfile);
    }
}

