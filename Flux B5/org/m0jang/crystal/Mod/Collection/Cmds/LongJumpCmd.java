package org.m0jang.crystal.Mod.Collection.Cmds;

import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Mod.Command;
import org.m0jang.crystal.Mod.Collection.Movement.LongJump;
import org.m0jang.crystal.Utils.ChatUtils;

@Command.Info(
   name = "longjump",
   syntax = {"<OldNCP/Hypixel>"},
   help = "Change the mode of Longjump."
)
public class LongJumpCmd extends Command {
   public void execute(String[] args) throws Command.Error {
      if (args.length > 1) {
         this.syntaxError();
      } else if (args.length == 1) {
         if (Crystal.INSTANCE.getMods().get(LongJump.class).setSubModule(args[0])) {
            ChatUtils.sendMessageToPlayer("LongJump mode changed to: \247a" + args[0]);
         } else {
            ChatUtils.sendMessageToPlayer("SubModule Not Found.");
         }
      } else {
         this.syntaxError();
      }

   }
}
