/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.commands.cmds;

import me.razerboy420.weepcraft.commands.Command;
import me.razerboy420.weepcraft.files.FriendsFile;
import me.razerboy420.weepcraft.friend.FriendManager;
import me.razerboy420.weepcraft.util.Wrapper;

public class CommandFriend
extends Command {
    public CommandFriend() {
        super(new String[]{"friend"}, "Add or remove friends.", ".friend add/del <name> <alias>");
    }

    @Override
    public boolean runCommand(String command, String[] args) {
        String name = args[2];
        if (args[1].equalsIgnoreCase("add")) {
            String alias = args[3];
            if (FriendManager.isFriend(name)) {
                Wrapper.tellPlayer("That person is already friended!");
            } else {
                FriendManager.addFriend(name, alias);
                Wrapper.tellPlayer(String.valueOf(String.valueOf(name)) + " added as " + alias);
                FriendsFile.save();
                return true;
            }
        }
        if (args[1].equalsIgnoreCase("del")) {
            if (!FriendManager.isFriend(name)) {
                Wrapper.tellPlayer("That person is not friended!");
            } else {
                FriendManager.removeFriend(name);
                Wrapper.tellPlayer(String.valueOf(String.valueOf(name)) + " removed from friends list");
                FriendsFile.save();
                return true;
            }
        }
        return false;
    }
}

