package org.m0jang.crystal.Events;

import com.darkmagician6.eventapi.events.Event;

public class EventRender2D implements Event {
   private float particalTicks;

   public EventRender2D(float particleTicks) {
      this.particalTicks = particleTicks;
   }

   public float getParticalTicks() {
      return this.particalTicks;
   }
}
