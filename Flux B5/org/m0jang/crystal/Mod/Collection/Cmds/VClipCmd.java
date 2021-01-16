package org.m0jang.crystal.Mod.Collection.Cmds;

import net.minecraft.client.Minecraft;
import org.m0jang.crystal.Mod.Command;
import org.m0jang.crystal.Utils.MathUtils;

@Command.Info(
   name = "vclip",
   syntax = {"<height>"},
   help = "TPs you up or down depending on <height>."
)
public class VClipCmd extends Command {
   public void execute(String[] args) throws Command.Error {
      if (args.length > 1) {
         this.syntaxError();
      } else if (args.length == 1) {
         if (MathUtils.isDouble(args[0])) {
            Minecraft.thePlayer.setPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + Double.parseDouble(args[0]), Minecraft.thePlayer.posZ);
         } else {
            this.syntaxError("<height> must be a valid integer!");
         }
      } else {
         this.syntaxError();
      }

   }
}
