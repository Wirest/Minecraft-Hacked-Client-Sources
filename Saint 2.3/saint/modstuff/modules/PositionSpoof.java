package saint.modstuff.modules;

import net.minecraft.network.play.client.C03PacketPlayer;
import saint.eventstuff.Event;
import saint.eventstuff.events.PacketSent;
import saint.eventstuff.events.PreMotion;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class PositionSpoof extends Module {
   private boolean cancel;

   public PositionSpoof() {
      super("PositionSpoof", -2228480, ModManager.Category.EXPLOITS);
      this.setTag("Position Spoof");
   }

   public void onEnabled() {
      super.onEnabled();
      if (mc.thePlayer != null) {
         double[] d = new double[]{0.2D, 0.24D};

         for(int a = 0; a < 100; ++a) {
            for(int i = 0; i < d.length; ++i) {
               mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + d[i], mc.thePlayer.posZ, false));
            }
         }
      }

   }

   public void onEvent(Event event) {
      if (event instanceof PreMotion) {
         if (this.cancel) {
            this.setEnabled(false);
         }
      } else if (event instanceof PacketSent) {
         PacketSent sent = (PacketSent)event;
         if (sent.getPacket() instanceof C03PacketPlayer) {
            C03PacketPlayer player = (C03PacketPlayer)sent.getPacket();
            player.y += 0.09D;
            player.field_149474_g = false;
         }
      }

   }

   public void onDisabled() {
      super.onDisabled();
      if (mc.thePlayer != null) {
         if (!this.cancel) {
            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + Double.MAX_VALUE, mc.thePlayer.posZ, false));
         }

         this.cancel = false;
      }

   }
}
