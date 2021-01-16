package org.m0jang.crystal.Mod.Collection.Cmds;

import java.util.Iterator;
import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Mod.Command;
import org.m0jang.crystal.Utils.ChatUtils;

@Command.Info(
   name = "help",
   syntax = {""},
   help = "Displays commands and info about them."
)
public class HelpCmd extends Command {
   public void execute(String[] args) throws Command.Error {
      Iterator var3 = Crystal.INSTANCE.commandManager.getCmds().iterator();

      while(var3.hasNext()) {
         Command cmd = (Command)var3.next();
         String output = "\247o." + cmd.getCmdName() + "\247r";
         if (cmd.getSyntax().length != 0) {
            output = output + " " + cmd.getSyntax()[0];

            for(int i = 1; i < this.getSyntax().length; ++i) {
               output = output + "\n    " + cmd.getSyntax()[i];
            }
         }

         String[] split;
         int length = (split = output.split("\n")).length;

         for(int j = 0; j < length; ++j) {
            String line = split[j];
            ChatUtils.sendMessageToPlayer(line + " - " + cmd.getHelp());
         }
      }

   }
}
