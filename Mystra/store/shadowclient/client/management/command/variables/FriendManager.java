package store.shadowclient.client.management.command.variables;

import joptsimple.internal.Strings;
import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class FriendManager {
    public final static List<Friend> friends = new ArrayList<>();

    public void add(String name) {
        add(name, name);
    }

    public void remove(String name) {
        for (final Friend friend : friends) {
            if (friend.getUsername().equalsIgnoreCase(name)) {
                friends.remove(friend);
                break;
            }
        }
    }

    public void add(String name, String alias) {
        friends.add(new Friend(name, alias));
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public static String getFriendsName() {
        ArrayList<String> list = new ArrayList<>();
        for (Friend friend : friends) {
            list.add(friend.getUsername());
        }
        return Strings.join(list.toArray(new String[0]), "§f, §a");
    }

    public static boolean isFriend(Entity entity) {
        for (Friend friend : friends) {
            if (friend.getUsername().equals(entity))
                return true;
        }
        return false;
    }
    
    public static boolean isFriend(String name) {
        for (Friend friend : friends) {
            if (friend.getUsername().equals(name))
                return true;
        }
        return false;
    }
}