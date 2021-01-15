package saint.modstuff.modules;

import net.minecraft.network.play.client.C03PacketPlayer;
import saint.eventstuff.Event;
import saint.eventstuff.events.PostMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class God extends Module {
   private int delay;
   private boolean set = false;

   public God() {
      super("God", -65536, ModManager.Category.EXPLOITS);
   }

   public void onEnabled() {
      super.onEnabled();
      this.delay = 0;
      this.set = false;
   }

   public void onDisabled() {
      super.onDisabled();
      if (this.set) {
         mc.thePlayer.setSprinting(false);
         this.set = false;
         this.delay = 0;
      }

      this.setTag("God");
   }

   public void onEvent(Event event) {
      if (event instanceof PostMotion) {
         boolean still = !mc.gameSettings.keyBindForward.pressed && !mc.gameSettings.keyBindBack.pressed && !mc.gameSettings.keyBindLeft.pressed && !mc.gameSettings.keyBindRight.pressed;
         this.delay += still ? 6 : (mc.thePlayer.isSneaking() ? 3 : (still ? 8 : 1));
         if (this.delay >= 61) {
            if (mc.thePlayer.isSprinting()) {
               mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.125D, mc.thePlayer.posZ, mc.thePlayer.onGround));
               mc.thePlayer.setSprinting(true);
            } else {
               mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.125D, mc.thePlayer.posZ, mc.thePlayer.onGround));
            }

            this.delay = 0;
         } else if (this.delay >= 60) {
            if (mc.thePlayer.isSprinting()) {
               mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.125D, mc.thePlayer.posZ, mc.thePlayer.onGround));
               mc.thePlayer.setSprinting(true);
            } else {
               mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.125D, mc.thePlayer.posZ, mc.thePlayer.onGround));
            }
         }

         this.setTag("God §7" + this.delay);
      }

   }
}
