// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventChatSend extends EventCancellable
{
    public String message;
    
    public EventChatSend(final String message) {
        this.message = message;
    }
}
