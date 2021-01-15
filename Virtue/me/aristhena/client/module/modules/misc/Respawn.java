// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.misc;

import me.aristhena.event.EventTarget;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod
public class Respawn extends Module
{
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (event.getState() == Event.State.POST && !ClientUtils.player().isEntityAlive()) {
            ClientUtils.player().respawnPlayer();
        }
    }
}
