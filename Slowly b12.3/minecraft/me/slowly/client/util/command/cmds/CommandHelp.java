package me.slowly.client.util.command.cmds;

import java.util.Iterator;
import me.slowly.client.util.command.Command;
import me.slowly.client.util.command.CommandManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class CommandHelp extends Command {
   public CommandHelp(String[] commands) {
      super(commands);
      this.setArgs("Shows all commands");
   }

   public void onCmd(String[] args) {
      Iterator var3 = CommandManager.getCommands().iterator();

      while(var3.hasNext()) {
         Command c = (Command)var3.next();
         String aliases = c.getCommands()[0];

         for(int i = 1; i < c.getCommands().length; ++i) {
            aliases = aliases + (i != c.getCommands().length ? ", " : "") + c.getCommands()[i];
         }

         Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("\u00a7e" + aliases + "\u00a7f - " + c.getArgs()));
      }

   }
}
