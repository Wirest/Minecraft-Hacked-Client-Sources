package org.m0jang.crystal.Events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;
import net.minecraft.network.Packet;

public class EventPacketSend extends EventCancellable {
   public Packet packet;

   public EventPacketSend(Packet packet) {
      this.packet = packet;
   }
}
