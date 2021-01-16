package org.m0jang.crystal.Mod.Collection.Cmds;

import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Mod.Command;
import org.m0jang.crystal.Mod.Module;
import org.m0jang.crystal.Utils.ChatUtils;

@Command.Info(
   name = "t",
   syntax = {"<mod>"},
   help = "Toggles mods."
)
public class ToggleCmd extends Command {
   public void execute(String[] args) throws Command.Error {
      if (args.length < 1) {
         this.syntaxError();
      } else {
         Module mod = Crystal.INSTANCE.getMods().get(args[0]);
         if (mod != null) {
            mod.toggle();
         } else {
            ChatUtils.sendMessageToPlayer("Module not found! (Hint: Don't include spaces or '.')");
         }
      }

   }
}
