package saint.modstuff.modules;

import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import saint.eventstuff.Event;
import saint.eventstuff.events.PostMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class SleepFly extends Module {
   private double firstY;

   public SleepFly() {
      super("SleepFly", -13083, ModManager.Category.EXPLOITS);
      this.setTag("Sleep Fly");
   }

   public void onEnabled() {
      super.onEnabled();
      double[] d = new double[]{0.2D, 0.24D};

      for(int a = 0; a < 100; ++a) {
         for(int i = 0; i < d.length; ++i) {
            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + d[i], mc.thePlayer.posZ, false));
         }
      }

      this.firstY = mc.thePlayer.posY;
   }

   public void onEvent(Event event) {
      if (event instanceof PostMotion) {
         if (!mc.thePlayer.capabilities.isFlying && mc.thePlayer.posY < this.firstY && this.firstY != 0.0D) {
            mc.thePlayer.capabilities.isFlying = true;
         }

         mc.thePlayer.capabilities.setFlySpeed(0.012F);
         if (mc.gameSettings.keyBindJump.pressed) {
            mc.thePlayer.motionY = 0.5D;
         }

         if (mc.gameSettings.keyBindSneak.pressed) {
            mc.thePlayer.motionY = -0.5D;
         }

         mc.getNetHandler().addToSendQueue(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SLEEPING));
      }

   }

   public void onDisabled() {
      super.onDisabled();
      if (mc.thePlayer != null) {
         if (mc.thePlayer.capabilities.isFlying) {
            mc.thePlayer.capabilities.isFlying = false;
         }

         mc.thePlayer.capabilities.setFlySpeed(0.05F);
      }

   }
}
