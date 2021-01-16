package org.m0jang.crystal.Mod.Collection.Cmds;

import java.util.Iterator;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Mod.Command;
import org.m0jang.crystal.Utils.ChatUtils;

@Command.Info(
   name = "friend",
   syntax = {"add <name>", "del <name>, list"},
   help = "Manages your friends."
)
public class FriendCmd extends Command {
   public void execute(String[] args) throws Command.Error {
      if (args.length > 2) {
         ChatUtils.sendMessageToPlayer("Args: add <name>, del <name>, list");
      } else if (args.length == 2) {
         if (args[0].equalsIgnoreCase("add")) {
            Crystal.INSTANCE.friendManager.addFriend(args[1]);
         } else if (args[0].equalsIgnoreCase("del")) {
            Crystal.INSTANCE.friendManager.delFriend(args[1]);
         } else {
            ChatUtils.sendMessageToPlayer("Args: add <name>, del <name>, list");
         }
      } else if (args.length == 1) {
         if (args[0].equalsIgnoreCase("list")) {
            ChatUtils.sendMessageToPlayer("Friends:");
            int i = 0;
            Iterator var4 = Crystal.INSTANCE.friendManager.getFriends().iterator();

            while(var4.hasNext()) {
               String friend = (String)var4.next();
               ++i;
               ChatUtils.sendMessageToPlayer(i + ". " + friend);
            }
         } else {
            ChatUtils.sendMessageToPlayer("Args: add <name>, del <name>, list");
         }
      } else {
         ChatUtils.sendMessageToPlayer("Args: add <name>, del <name>, list");
      }

   }
}
