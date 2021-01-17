package com.darkmagician6.eventapi.events;

import com.darkmagician6.eventapi.events.Event;

public abstract class EventStoppable implements Event {
   private boolean stopped;

   public void stop() {
      this.stopped = true;
   }

   public boolean isStopped() {
      return this.stopped;
   }
}
