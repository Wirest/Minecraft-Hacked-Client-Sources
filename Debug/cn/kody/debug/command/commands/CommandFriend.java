package cn.kody.debug.command.commands;

import cn.kody.debug.Client;
import cn.kody.debug.command.Command;
import cn.kody.debug.friend.FriendsManager;
import cn.kody.debug.ui.Notification;
import cn.kody.debug.utils.Friend;
import cn.kody.debug.utils.Type;

public class CommandFriend extends Command
{
    
    public CommandFriend(final String[] array) {
        super(array);
        this.setArgs(".friend <add/remove> <name>");
    }
    
    @Override
    public void onCmd(final String[] array) {
        if (array.length < 3) {
            Notification.tellPlayer(this.getArgs(), Type.INFO);
            return;
        }
        final String s = array[1];
        final String p_i737_1_ = array[2];
        final Friend friend = FriendsManager.getFriend(p_i737_1_);
        if (s.equalsIgnoreCase("add")) {
            if (friend == null) {
                final Friend class189 = new Friend(p_i737_1_);
                Notification.tellPlayer("Added friend " + p_i737_1_, Type.INFO);
                FriendsManager.getFriends().add(class189);
            }
        }
        else if (s.equalsIgnoreCase("remove")) {
            if (friend != null) {
                FriendsManager.getFriends().remove(friend);
                Notification.tellPlayer("Removed friend", Type.ERROR);
            }
        }else {
            Notification.tellPlayer(this.getArgs(), Type.INFO);
        }
        Client.instance.fileMgr.saveFriends();
    }
}
