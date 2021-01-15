// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.movement;

import me.aristhena.event.EventTarget;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(displayName = "No Fall")
public class NoFall extends Module
{
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (!event.shouldAlwaysSend()) {
            event.setGround(true);
        }
    }
}
