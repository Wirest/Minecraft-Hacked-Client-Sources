package org.m0jang.crystal.Mod.Collection.Cmds;

import net.minecraft.client.Minecraft;
import org.m0jang.crystal.Mod.Command;
import org.m0jang.crystal.Utils.MathUtils;

@Command.Info(
   name = "hclip",
   syntax = {"<distance>"},
   help = "Teleport by Horizonal."
)
public class HClipCmd extends Command {
   public void execute(String[] args) throws Command.Error {
      if (args.length > 1) {
         this.syntaxError();
      } else if (args.length == 1) {
         if (MathUtils.isDouble(args[0])) {
            float multiplier = Float.parseFloat(args[0]);
            double mx = Math.cos(Math.toRadians((double)(Minecraft.thePlayer.rotationYaw + 90.0F)));
            double mz = Math.sin(Math.toRadians((double)(Minecraft.thePlayer.rotationYaw + 90.0F)));
            double x = (double)(1.0F * multiplier) * mx + (double)(0.0F * multiplier) * mz;
            double z = (double)(1.0F * multiplier) * mz - (double)(0.0F * multiplier) * mx;
            Minecraft.thePlayer.boundingBox.offsetAndUpdate(x, 0.0D, z);
         } else {
            this.syntaxError("<distance> must be a valid integer!");
         }
      } else {
         this.syntaxError();
      }

   }
}
