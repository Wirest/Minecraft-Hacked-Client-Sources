// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.command.commands;

import me.aristhena.client.friend.FriendManager;
import me.aristhena.utils.ClientUtils;
import me.aristhena.client.command.Com;
import me.aristhena.client.command.Command;

@Com(names = { "friend", "f" })
public class Friend extends Command
{
    @Override
    public void runCommand(final String[] args) {
        if (args.length < 3) {
            ClientUtils.sendMessage(this.getHelp());
            return;
        }
        if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("a")) {
            String alias = args[2];
            if (args.length > 3) {
                alias = args[3];
                if (alias.startsWith("\"") && args[args.length - 1].endsWith("\"")) {
                    alias = alias.substring(1, alias.length());
                    for (int i = 4; i < args.length; ++i) {
                        alias = String.valueOf(alias) + " " + args[i].replace("\"", "");
                    }
                }
            }
            if (FriendManager.isFriend(args[2]) && args.length < 3) {
                ClientUtils.sendMessage(String.valueOf(args[2]) + " is already your friend.");
                return;
            }
            FriendManager.removeFriend(args[2]);
            FriendManager.addFriend(args[2], alias);
            ClientUtils.sendMessage("Added " + args[2] + ((args.length > 3) ? (" as " + alias) : ""));
        }
        else if (args[1].equalsIgnoreCase("del") || args[1].equalsIgnoreCase("d")) {
            if (FriendManager.isFriend(args[2])) {
                FriendManager.removeFriend(args[2]);
                ClientUtils.sendMessage("Removed friend: " + args[2]);
            }
            else {
                ClientUtils.sendMessage(String.valueOf(args[2]) + " is not your friend.");
            }
        }
        else {
            ClientUtils.sendMessage(this.getHelp());
        }
    }
    
    @Override
    public String getHelp() {
        return "Friend - friend <f>  (add <a> | del <d>) (name) [alias | \"alias w/ spaces\"].";
    }
}
