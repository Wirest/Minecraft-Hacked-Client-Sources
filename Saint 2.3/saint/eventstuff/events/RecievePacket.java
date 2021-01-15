package saint.eventstuff.events;

import net.minecraft.network.Packet;
import saint.eventstuff.Cancellable;
import saint.eventstuff.Event;

public class RecievePacket extends Event implements Cancellable {
   private Packet packet;
   private boolean cancel;

   public RecievePacket(Packet packet) {
      this.packet = packet;
   }

   public final Packet getPacket() {
      return this.packet;
   }

   public void setPacket(Packet packet) {
      this.packet = packet;
   }

   public boolean isCancelled() {
      return this.cancel;
   }

   public void setCancelled(boolean shouldCancel) {
      this.cancel = shouldCancel;
   }
}
