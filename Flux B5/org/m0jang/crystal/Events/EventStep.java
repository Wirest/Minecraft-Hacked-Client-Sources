package org.m0jang.crystal.Events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventStep extends EventCancellable {
   float stepHeight;

   public EventStep(float stepHeight) {
      this.stepHeight = stepHeight;
   }

   public float getStepHeight() {
      return this.stepHeight;
   }
}
