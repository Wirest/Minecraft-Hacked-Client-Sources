package org.m0jang.crystal.Mod.Collection.Cmds;

import org.m0jang.crystal.Mod.Command;
import org.m0jang.crystal.Utils.ChatUtils;
import org.m0jang.crystal.Utils.ModeUtils;

@Command.Info(
   name = "noslow",
   syntax = {"<Sneak>"},
   help = "Change the mode of NoSlow."
)
public class NoSlowCmd extends Command {
   public void execute(String[] args) throws Command.Error {
      if (args.length > 1) {
         this.syntaxError();
      } else if (args.length == 1) {
         if (args[0].equalsIgnoreCase("Sneak")) {
            ModeUtils.noslowSneak = !ModeUtils.noslowSneak;
            ChatUtils.sendMessageToPlayer("[NoSlow] Sneak : " + ModeUtils.noslowSneak);
         } else {
            this.syntaxError();
         }
      } else {
         this.syntaxError();
      }

   }
}
