package org.m0jang.crystal.Mod.Collection.Cmds;

import org.m0jang.crystal.Mod.Command;
import org.m0jang.crystal.Utils.ChatUtils;
import org.m0jang.crystal.Utils.ModeUtils;

@Command.Info(
   name = "tracers",
   syntax = {"<players/mobs/animals>"},
   help = "Change the settings of Tracers."
)
public class TracersCmd extends Command {
   public void execute(String[] args) throws Command.Error {
      if (args.length > 1) {
         this.syntaxError();
      } else if (args.length == 1) {
         if (args[0].equalsIgnoreCase("players")) {
            ModeUtils.tracerP = !ModeUtils.tracerP;
            ChatUtils.sendMessageToPlayer("Players set to: " + ModeUtils.tracerP);
         } else if (args[0].equalsIgnoreCase("mobs")) {
            ModeUtils.tracerM = !ModeUtils.tracerM;
            ChatUtils.sendMessageToPlayer("Mobs set to: " + ModeUtils.tracerM);
         } else if (args[0].equalsIgnoreCase("animals")) {
            ModeUtils.tracerA = !ModeUtils.tracerA;
            ChatUtils.sendMessageToPlayer("Animals set to: " + ModeUtils.tracerA);
         } else {
            this.syntaxError();
         }
      } else {
         this.syntaxError();
      }

   }
}
