package me.slowly.client.util.command;

import java.util.ArrayList;
import me.slowly.client.util.command.cmds.CommandBind;
import me.slowly.client.util.command.cmds.CommandBlock;
import me.slowly.client.util.command.cmds.CommandCrasher;
import me.slowly.client.util.command.cmds.CommandFriend;
import me.slowly.client.util.command.cmds.CommandHelp;
import me.slowly.client.util.command.cmds.CommandIRC;
import me.slowly.client.util.command.cmds.CommandIngameName;
import me.slowly.client.util.command.cmds.CommandNameProtect;
import me.slowly.client.util.command.cmds.CommandSay;
import me.slowly.client.util.command.cmds.CommandSpammer;
import me.slowly.client.util.command.cmds.CommandToggle;
import me.slowly.client.util.friendmanager.FriendManager;

public class CommandManager {
   private static ArrayList commands = new ArrayList();

   public CommandManager() {
      this.isCmd();
      commands.add(new CommandBind(new String[]{"bind"}));
      commands.add(new CommandSpammer(new String[]{"spammer"}));
      commands.add(new CommandHelp(new String[]{"help"}));
      commands.add(new CommandFriend(new String[]{"friend"}));
      commands.add(new CommandToggle(new String[]{"toggle", "t"}));
      commands.add(new CommandNameProtect(new String[]{"nameprotect"}));
      commands.add(new CommandIngameName(new String[]{"copyign", "ign", "ingamename", "copyingamename"}));
      commands.add(new CommandBlock(new String[]{"block", "blockesp", "blocklist"}));
      commands.add(new CommandSay(new String[]{"say"}));
      commands.add(new CommandIRC(new String[]{"irc"}));
      commands.add(new CommandCrasher(new String[]{"c"}));
   }

   public static ArrayList getCommands() {
      return commands;
   }

   private void isCmd() {
      FriendManager.isValid();
   }
}
