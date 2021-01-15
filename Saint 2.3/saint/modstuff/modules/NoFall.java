package saint.modstuff.modules;

import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C03PacketPlayer;
import saint.Saint;
import saint.comandstuff.Command;
import saint.eventstuff.Event;
import saint.eventstuff.events.PacketSent;
import saint.modstuff.ModManager;
import saint.modstuff.Module;
import saint.utilities.Logger;
import saint.utilities.TimeHelper;
import saint.valuestuff.Value;

public class NoFall extends Module {
   private Value nocheat = new Value("nofall_nocheat", true);
   private final TimeHelper time = new TimeHelper();

   public NoFall() {
      super("NoFall", -16181, ModManager.Category.PLAYER);
      this.setTag("No Fall");
      Saint.getCommandManager().getContentList().add(new Command("nofallnocheat", "none", new String[]{"nfnocheat", "nfnc"}) {
         public void run(String message) {
            NoFall.this.nocheat.setValueState(!(Boolean)NoFall.this.nocheat.getValueState());
            Logger.writeChat("No Fall will " + ((Boolean)NoFall.this.nocheat.getValueState() ? "now" : "no longer") + " bypass nocheat.");
         }
      });
   }

   public void onEnabled() {
      super.onEnabled();
      if (mc.thePlayer != null && (Boolean)this.nocheat.getValueState()) {
         double x = mc.thePlayer.posX;
         double y = mc.thePlayer.posY;
         double z = mc.thePlayer.posZ;
         Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.6D, z, false));

         for(int i = 0; i < 15; ++i) {
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.5D, z, false));
            Minecraft.getMinecraft().thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.2D, z, false));
         }

         this.time.reset();
      }

   }

   public void onEvent(Event event) {
      if (event instanceof PacketSent) {
         PacketSent sent = (PacketSent)event;
         if (sent.getPacket() instanceof C03PacketPlayer && this.time.hasReached(1000L)) {
            C03PacketPlayer player = (C03PacketPlayer)sent.getPacket();
            if ((Boolean)this.nocheat.getValueState() && this.time.hasReached(500L)) {
               float offset = 0.6F;
               player.x = mc.thePlayer.posX;
               player.y = mc.thePlayer.posY + 0.6000000238418579D;
               player.field_149474_g = false;
            } else if (!(Boolean)this.nocheat.getValueState() && mc.thePlayer.fallDistance >= 3.0F) {
               player.field_149474_g = true;
            }
         }
      }

   }
}
