package org.m0jang.crystal.Events;

import com.darkmagician6.eventapi.events.Event;

public class EventRender3D implements Event {
   public float partialTicks;

   public EventRender3D(float partialTicks) {
      this.partialTicks = partialTicks;
   }

   public float getPartialTicks() {
      return this.partialTicks;
   }
}
