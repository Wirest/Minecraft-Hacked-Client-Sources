// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.player;

import me.aristhena.event.EventTarget;
import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.client.module.Module;

public class Fire extends Module
{
    @EventTarget
    private void onPostUpdate(final UpdateEvent event) {
        event.getState();
        final Event.State pre = Event.State.PRE;
    }
}
