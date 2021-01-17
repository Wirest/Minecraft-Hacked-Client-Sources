package me.slowly.client.util.command.cmds;

import me.slowly.client.Client;
import me.slowly.client.ui.notifiactions.ClientNotification;
import me.slowly.client.util.ClientUtil;
import me.slowly.client.util.FileUtil;
import me.slowly.client.util.command.Command;
import me.slowly.client.util.friendmanager.Friend;
import me.slowly.client.util.friendmanager.FriendManager;

public class CommandFriend
extends Command {
    public CommandFriend(String[] commands) {
        super(commands);
        this.setArgs("Args: <add/a/remove/r> <name> <alias>");
    }

    @Override
    public void onCmd(String[] args) {
        if (args.length < 3) {
            ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.INFO);
            return;
        }
        String option = args[1];
        String name = args[2];
        String alias = args.length > 3 ? args[3] : name;
        Friend friend = FriendManager.getFriend(name);
        if (option.equalsIgnoreCase("a") || option.equalsIgnoreCase("add")) {
            if (friend == null) {
                Friend newFriend = new Friend(name, alias);
                ClientUtil.sendClientMessage("Added friend " + name + " as " + alias, ClientNotification.Type.SUCCESS);
                FriendManager.getFriends().add(newFriend);
            } else {
                friend.setAlias(alias);
                ClientUtil.sendClientMessage("Changed alias to " + alias, ClientNotification.Type.INFO);
            }
        } else if (option.equalsIgnoreCase("r") || option.equalsIgnoreCase("remove")) {
            if (friend != null) {
                FriendManager.getFriends().remove(friend);
                ClientUtil.sendClientMessage("Removed friend", ClientNotification.Type.ERROR);
            }
        } else {
            ClientUtil.sendClientMessage(this.getArgs(), ClientNotification.Type.INFO);
        }
        Client.getInstance().getFileUtil().saveFriends();
    }
}

