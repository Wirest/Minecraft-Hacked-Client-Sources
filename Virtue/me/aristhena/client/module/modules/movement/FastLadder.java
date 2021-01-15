// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.movement;

import me.aristhena.event.EventTarget;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.events.MoveEvent;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(displayName = "Fast Ladder")
public class FastLadder extends Module
{
    private static final double MAX_LADDER_SPEED = 0.287299999999994;
    
    @EventTarget
    private void onMove(final MoveEvent event) {
        ClientUtils.mc().timer.timerSpeed = 1.0f;
        if (event.getY() > 0.0 && ClientUtils.player().isOnLadder()) {
            event.setY(0.287299999999994);
        }
    }
}
