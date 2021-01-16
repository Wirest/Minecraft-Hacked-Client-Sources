package org.m0jang.crystal.Events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventChatSend extends EventCancellable {
   public String message;

   public EventChatSend(String message) {
      this.message = message;
   }
}
