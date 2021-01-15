// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod.Collection.Cmds;

import cf.euphoria.euphorical.Euphoria;
import cf.euphoria.euphorical.Mod.Cmd;
import cf.euphoria.euphorical.Mod.Cmd.Info;
import cf.euphoria.euphorical.Utils.ChatUtils;

@Info(name = "friend", syntax = { "add <name>", "del <name>, list" }, help = "Manages your friends")
public class FriendCmd extends Cmd
{
    @Override
    public void execute(final String[] args) throws Error {
        if (args.length > 2) {
            ChatUtils.sendMessageToPlayer("Args: add <name>, del <name>, list");
        }
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add")) {
                Euphoria.getEuphoria().friendUtils.addFriend(args[1]);
            }
            else if (args[0].equalsIgnoreCase("del")) {
                Euphoria.getEuphoria().friendUtils.delFriend(args[1]);
            }
            else {
                ChatUtils.sendMessageToPlayer("Args: add <name>, del <name>, list");
            }
        }
        else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("list")) {
                ChatUtils.sendMessageToPlayer("Friends:");
                int i = 0;
                for (final String friend : Euphoria.getEuphoria().friendUtils.getFriends()) {
                    ++i;
                    ChatUtils.sendMessageToPlayer(String.valueOf(i) + ". " + friend);
                }
            }
            else {
                ChatUtils.sendMessageToPlayer("Args: add <name>, del <name>, list");
            }
        }
        else {
            ChatUtils.sendMessageToPlayer("Args: add <name>, del <name>, list");
        }
    }
}
