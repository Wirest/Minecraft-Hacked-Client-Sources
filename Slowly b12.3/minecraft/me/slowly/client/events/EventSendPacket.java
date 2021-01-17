package me.slowly.client.events;

import com.darkmagician6.eventapi.events.Event;
import net.minecraft.network.Packet;

public class EventSendPacket implements Event {
   private Packet packet;
   public boolean cancel;

   public EventSendPacket(Packet packet) {
      this.packet = packet;
   }

   public Packet getPacket() {
      return this.packet;
   }
}


