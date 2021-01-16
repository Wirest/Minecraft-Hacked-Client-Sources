package org.m0jang.crystal.Events;

import com.darkmagician6.eventapi.events.Event;
import java.util.UUID;
import net.minecraft.network.play.server.S38PacketPlayerListItem;

public class EventPlayerList implements Event {
   public S38PacketPlayerListItem.Action action;
   public UUID uuid;

   public EventPlayerList(S38PacketPlayerListItem.Action action, UUID uuid) {
      this.action = action;
      this.uuid = uuid;
   }
}
