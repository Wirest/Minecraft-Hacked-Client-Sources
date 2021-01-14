package moonx.ohare.client.friend;

import java.io.File;
import java.util.ArrayList;

public class FriendManager {
    private ArrayList<Friend> friends = new ArrayList<Friend>();
    private FriendSaving friendSaving;

    public FriendManager(File dir) {
        friendSaving = new FriendSaving(dir);
        friendSaving.setup();
    }

    public FriendSaving getFriendSaving() {
        return this.friendSaving;
    }

    public ArrayList<Friend> getFriends() {
        return this.friends;
    }

    public void addFriend(String name) {
        friends.add(new Friend(name));
    }

    public void addFriendWithAlias(String name, String alias) {
        friends.add(new Friend(name, alias));
    }

    public Friend getFriend(String ign) {
        for (Friend friend : friends) {
            if (friend.getName().equalsIgnoreCase(ign)) {
                return friend;
            }
        }
        return null;
    }

    public void setFriends(ArrayList<Friend> friends) {
        this.friends = friends;
    }

    public boolean isFriend(String ign) {
        return getFriend(ign) != null;
    }

    public void clearFriends() {
        friends.clear();
    }

    public void removeFriend(String name) {
        Friend f = getFriend(name);
        if (f != null) {
            friends.remove(f);
        }
    }
}
