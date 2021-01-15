package saint.comandstuff.commands;

import net.minecraft.network.play.client.C03PacketPlayer;
import saint.comandstuff.Command;

public class Hclip extends Command {
   public Hclip() {
      super("hclip", "<blocks>", "horizontalclip", "hc");
   }

   public void run(String message) {
      float blocks = Float.parseFloat(message.split(" ")[1]);
      float dir = mc.thePlayer.rotationYaw;
      if (mc.thePlayer.moveForward < 0.0F) {
         dir += 180.0F;
      }

      if (mc.thePlayer.moveStrafing > 0.0F) {
         dir -= 90.0F * (mc.thePlayer.moveForward < 0.0F ? -0.5F : (mc.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F));
      }

      if (mc.thePlayer.moveStrafing < 0.0F) {
         dir += 90.0F * (mc.thePlayer.moveForward < 0.0F ? -0.5F : (mc.thePlayer.moveForward > 0.0F ? 0.5F : 1.0F));
      }

      double hOff = (double)blocks;
      float xD = (float)((double)((float)Math.cos((double)(dir + 90.0F) * 3.141592653589793D / 180.0D)) * hOff);
      float zD = (float)((double)((float)Math.sin((double)(dir + 90.0F) * 3.141592653589793D / 180.0D)) * hOff);
      mc.thePlayer.setPosition(mc.thePlayer.posX + (double)xD, mc.thePlayer.posY, mc.thePlayer.posZ + (double)zD);
      mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + (double)xD, mc.thePlayer.posY, mc.thePlayer.posZ + (double)zD, false));
   }
}
