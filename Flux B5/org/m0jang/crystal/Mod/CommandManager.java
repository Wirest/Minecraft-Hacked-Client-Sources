package org.m0jang.crystal.Mod;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.EventTarget;
import java.util.Collection;
import java.util.TreeMap;

import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Events.EventChatSend;
import org.m0jang.crystal.Mod.Collection.Cmds.AnalyzeBot;
import org.m0jang.crystal.Mod.Collection.Cmds.BindCmd;
import org.m0jang.crystal.Mod.Collection.Cmds.FlyCmd;
import org.m0jang.crystal.Mod.Collection.Cmds.FriendCmd;
import org.m0jang.crystal.Mod.Collection.Cmds.HClipCmd;
import org.m0jang.crystal.Mod.Collection.Cmds.HelpCmd;
import org.m0jang.crystal.Mod.Collection.Cmds.LongJumpCmd;
import org.m0jang.crystal.Mod.Collection.Cmds.NoSlowCmd;
import org.m0jang.crystal.Mod.Collection.Cmds.PDCmd;
import org.m0jang.crystal.Mod.Collection.Cmds.ServerInfoCmd;
import org.m0jang.crystal.Mod.Collection.Cmds.SpeedCmd;
import org.m0jang.crystal.Mod.Collection.Cmds.ToggleCmd;
import org.m0jang.crystal.Mod.Collection.Cmds.TracersCmd;
import org.m0jang.crystal.Mod.Collection.Cmds.VClipCmd;
import org.m0jang.crystal.Mod.Collection.Misc.Commands;
import org.m0jang.crystal.Utils.ChatUtils;

public class CommandManager {
   private final TreeMap cmds = new TreeMap();

   public CommandManager() {
      this.add(new BindCmd());
      this.add(new FriendCmd());
      this.add(new HelpCmd());
      this.add(new PDCmd());
      this.add(new ServerInfoCmd());
      this.add(new SpeedCmd());
      this.add(new ToggleCmd());
      this.add(new TracersCmd());
      this.add(new VClipCmd());
      this.add(new FlyCmd());
      this.add(new NoSlowCmd());
      this.add(new HClipCmd());
      this.add(new LongJumpCmd());
      this.add(new AnalyzeBot());
      EventManager.register(this);
   }

   private void add(Command cmd) {
      this.cmds.put(cmd.getCmdName(), cmd);
   }

   @EventTarget
   public void onChatSend(EventChatSend event) {
      if (Crystal.INSTANCE.getMods().get(Commands.class).isEnabled()) {
         String message = event.message;
         if (message.startsWith(".")) {
            event.setCancelled(true);
            String input = message.substring(1);
            String commandName = input.split(" ")[0];
            String[] args;
            if (input.contains(" ")) {
               args = input.substring(input.indexOf(" ") + 1).split(" ");
            } else {
               args = new String[0];
            }

            Command cmd = this.getCommandByName(commandName);
            if (cmd != null) {
               try {
                  cmd.execute(args);
               } catch (Command.SyntaxError var8) {
                  if (var8.getMessage() != null) {
                     ChatUtils.sendMessageToPlayer("\2474Syntax error:\247r " + var8.getMessage());
                  } else {
                     ChatUtils.sendMessageToPlayer("\2474Syntax error!\247r");
                  }

                  cmd.printSyntax();
               } catch (Command.Error var9) {
                  ChatUtils.sendMessageToPlayer(var9.getMessage());
               } catch (Exception var10) {
                  ;
               }
            } else {
               ChatUtils.sendMessageToPlayer("\"." + commandName + "\" is not a valid command.");
            }
         }
      }

   }

   public Command getCommandByName(String name) {
      return (Command)this.cmds.get(name);
   }

   public Collection getCmds() {
      return this.cmds.values();
   }

   public int countCommands() {
      return this.cmds.size();
   }
}
