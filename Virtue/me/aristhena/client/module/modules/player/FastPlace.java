// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.player;

import me.aristhena.event.EventTarget;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(displayName = "Fast Place")
public class FastPlace extends Module
{
    @Option.Op(name = "Half Speed")
    private boolean halfSpeed;
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (event.getState() == Event.State.PRE) {
            ClientUtils.mc().rightClickDelayTimer = Math.min(ClientUtils.mc().rightClickDelayTimer, this.halfSpeed ? 2 : 1);
        }
    }
}
