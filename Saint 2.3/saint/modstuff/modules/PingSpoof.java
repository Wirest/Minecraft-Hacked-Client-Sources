package saint.modstuff.modules;

import net.minecraft.network.play.client.C00PacketKeepAlive;
import saint.eventstuff.Event;
import saint.eventstuff.events.PacketSent;
import saint.modstuff.ModManager;
import saint.modstuff.Module;

public class PingSpoof extends Module {
   public PingSpoof() {
      super("PingSpoof", -3342439, ModManager.Category.EXPLOITS);
      this.setTag("Ping Spoof");
   }

   public void onEvent(Event event) {
      if (event instanceof PacketSent) {
         PacketSent sent = (PacketSent)event;
         if (sent.getPacket() instanceof C00PacketKeepAlive) {
            C00PacketKeepAlive packet = (C00PacketKeepAlive)sent.getPacket();
            packet.key = Integer.MAX_VALUE;
         }
      }

   }
}
