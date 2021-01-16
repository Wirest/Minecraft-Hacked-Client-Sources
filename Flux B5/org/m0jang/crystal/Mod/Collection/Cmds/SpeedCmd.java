package org.m0jang.crystal.Mod.Collection.Cmds;

import org.m0jang.crystal.Crystal;
import org.m0jang.crystal.Mod.Command;
import org.m0jang.crystal.Mod.Collection.Movement.Speed;
import org.m0jang.crystal.Utils.ChatUtils;

@Command.Info(
   name = "speed",
   syntax = {"<SlowHop/NCPHop/Latest/Swift/Frames>"},
   help = "Change the mode of Speed."
)
public class SpeedCmd extends Command {
   public void execute(String[] args) throws Command.Error {
      if (args.length > 1) {
         this.syntaxError();
      } else if (args.length == 1) {
         if (Crystal.INSTANCE.getMods().get(Speed.class).setSubModule(args[0])) {
            ChatUtils.sendMessageToPlayer("Speed's SubModule changed to: \247a" + args[0]);
         } else {
            ChatUtils.sendMessageToPlayer("SubModule Not Found.");
         }
      } else {
         this.syntaxError();
      }

   }
}
