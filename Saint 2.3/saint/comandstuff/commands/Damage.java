package saint.comandstuff.commands;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import saint.comandstuff.Command;

public class Damage extends Command {
   public Damage() {
      super("damage", "none", "hurt", "dmg");
   }

   public void run(String message) {
      double x = mc.thePlayer.posX;
      double y = mc.thePlayer.posY;
      double z = mc.thePlayer.posZ;
      if (mc.thePlayer != null) {
         double[] d = new double[]{0.2D, 0.24D};

         for(int a = 0; a < 100; ++a) {
            for(int i = 0; i < d.length; ++i) {
               mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + d[i], mc.thePlayer.posZ, false));
            }
         }
      } else if (mc.thePlayer != null && message.split(" ")[1].equalsIgnoreCase("old")) {
         for(int i = 0; i < 4; ++i) {
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 1.01D, z, false));
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y, z, false));
         }

         Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.8D, z, false));
      }

   }
}
