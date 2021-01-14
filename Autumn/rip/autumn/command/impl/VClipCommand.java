package rip.autumn.command.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import rip.autumn.command.AbstractCommand;
import rip.autumn.utils.Logger;

public final class VClipCommand extends AbstractCommand {
   public VClipCommand() {
      super("VClip", "Teleport vertically.", "vclip <value>", "vclip");
   }

   public void execute(String... arguments) {
      if (arguments.length == 2) {
         try {
            double distance = Double.parseDouble(arguments[1]);
            EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
            player.setPosition(player.posX, player.posY + distance, player.posZ);
            Logger.log("Successfully VClipped " + distance + " blocks.");
         } catch (NumberFormatException var5) {
            Logger.log("'" + arguments[1] + "' is not a number.");
         }
      } else {
         this.usage();
      }

   }
}
