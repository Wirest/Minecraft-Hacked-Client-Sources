/*
 * Decompiled with CFR 0.150.
 */
package delta.friends;

import java.util.ArrayList;
import java.util.List;

public class FriendsManager {
    private List<String> friends = new ArrayList<String>();

    public List<String> getFriends() {
        return this.friends;
    }

    public void addFriend(String string) {
        if (this.contains(string)) {
            return;
        }
        this.friends.add(string);
    }

    public void removeFriend(String string) {
        if (this.contains(string)) {
            this.friends.remove(string);
        }
    }

    public boolean contains(String string) {
        return this.friends.contains(string);
    }
}

