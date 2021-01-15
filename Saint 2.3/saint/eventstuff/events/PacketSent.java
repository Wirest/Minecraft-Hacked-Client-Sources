package saint.eventstuff.events;

import net.minecraft.network.Packet;
import saint.eventstuff.Cancellable;
import saint.eventstuff.Event;

public class PacketSent extends Event implements Cancellable {
   private Packet packet;
   private boolean cancel;

   public PacketSent(Packet packet) {
      this.packet = packet;
   }

   public Packet getPacket() {
      return this.packet;
   }

   public boolean isCancelled() {
      return this.cancel;
   }

   public void setCancelled(boolean shouldCancel) {
      this.cancel = shouldCancel;
   }

   public void setPacket(Packet packet) {
      this.packet = packet;
   }
}
