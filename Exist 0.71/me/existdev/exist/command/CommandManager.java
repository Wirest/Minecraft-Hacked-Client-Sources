package me.existdev.exist.command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import me.existdev.exist.command.Command;
import me.existdev.exist.command.commands.BindCommand;
import me.existdev.exist.command.commands.HelpCommand;
import me.existdev.exist.command.commands.ToggleCommand;
import me.existdev.exist.utils.ChatUtils;

public class CommandManager {
   // $FF: synthetic field
   private HashMap commands = new HashMap();
   // $FF: synthetic field
   private String prefix = "-";

   // $FF: synthetic method
   public void loadCommands() {
      this.commands.put(new String[]{"help"}, new HelpCommand());
      this.commands.put(new String[]{"bind"}, new BindCommand());
      this.commands.put(new String[]{"toggle", "t"}, new ToggleCommand());
   }

   // $FF: synthetic method
   public boolean processCommand(String rawMessage) {
      if(!rawMessage.startsWith(this.prefix)) {
         return false;
      } else {
         boolean safe = rawMessage.split(this.prefix).length > 1;
         if(safe) {
            String beheaded = rawMessage.split(this.prefix)[1];
            String[] args = beheaded.split(" ");
            Command command = this.getCommand(args[0]);
            if(command != null) {
               if(!command.run(args)) {
                  ChatUtils.tellPlayer(command.usage());
               }
            } else {
               ChatUtils.tellPlayer("That command does not exist!");
            }
         } else {
            ChatUtils.tellPlayer("Try -help to get all commands!");
         }

         return true;
      }
   }

   // $FF: synthetic method
   private Command getCommand(String name) {
      Iterator var3 = this.commands.entrySet().iterator();

      while(var3.hasNext()) {
         Entry entry = (Entry)var3.next();
         String[] key = (String[])entry.getKey();
         String[] var8 = key;
         int var7 = key.length;

         for(int var6 = 0; var6 < var7; ++var6) {
            String s = var8[var6];
            if(s.equalsIgnoreCase(name)) {
               return (Command)entry.getValue();
            }
         }
      }

      return null;
   }

   // $FF: synthetic method
   public HashMap getCommands() {
      return this.commands;
   }
}
