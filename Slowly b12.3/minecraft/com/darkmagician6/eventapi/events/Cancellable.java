package com.darkmagician6.eventapi.events;

public interface Cancellable {
   boolean isCancelled();

   void setCancelled(boolean var1);
}
