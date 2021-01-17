package me.rigamortis.faurax.commands;

import me.cupboard.command.*;
import me.cupboard.command.argument.*;
import me.rigamortis.faurax.friends.*;
import me.rigamortis.faurax.*;

public class CommandFriend extends Command
{
    public CommandFriend() {
        super("friend", new String[0]);
    }
    
    @Argument
    protected String friendHelp() {
        final String help = "Add §a<§fUsername§a>§f §a<§fAlias§a>§f, Remove §a<§fUsername§a>§f, Clear";
        return help;
    }
    
    @Argument(handles = { "add", "a" })
    protected String friend(final String name, final String alias) {
        if (FriendManager.isFriend(name)) {
            return "§b" + name + " §fis already on your friend list.";
        }
        FriendManager.addFriend(name, alias);
        Client.getConfig().saveFriends();
        return "Added §b" + name + " §fas§b " + alias + " §fto your friend list.§f";
    }
    
    @Argument(handles = { "remove", "r", "rem", "del", "delete" })
    protected String friend(final String name) {
        if (!FriendManager.isFriend(name)) {
            return "§b" + name + " §fis not on your friend list.";
        }
        FriendManager.removeFriend(name);
        Client.getConfig().saveFriends();
        return "Removed §b" + name + " §ffrom your friend list.§f";
    }
    
    @Argument(handles = { "clear", "c" })
    protected String friendClear(final String clear) {
        final int friends = FriendManager.friends.size();
        FriendManager.friends.clear();
        Client.getConfig().saveFriends();
        return "Removed §b" + friends + " §ffrom your friend list.§f";
    }
}
