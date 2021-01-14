package rip.autumn.events.packet;

import net.minecraft.network.Packet;
import rip.autumn.events.Cancellable;
import rip.autumn.events.Event;

public final class ReceivePacketEvent extends Cancellable implements Event {
   private final Packet packet;

   public ReceivePacketEvent(Packet packet) {
      this.packet = packet;
   }

   public Packet getPacket() {
      return this.packet;
   }
}
