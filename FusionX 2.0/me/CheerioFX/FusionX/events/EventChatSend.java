// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.events;

import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventChatSend extends EventCancellable
{
    private static String message;
    private boolean canceled;
    
    public static String getMessage() {
        return EventChatSend.message;
    }
    
    public void setMessage(final String m) {
        EventChatSend.message = m;
    }
    
    @Override
    public boolean isCancelled() {
        return this.canceled;
    }
    
    @Override
    public void setCancelled(final boolean state) {
        this.canceled = state;
    }
}
