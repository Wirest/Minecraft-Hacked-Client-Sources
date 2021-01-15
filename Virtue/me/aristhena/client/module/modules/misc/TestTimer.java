// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.misc;

import me.aristhena.event.EventTarget;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.events.MoveEvent;
import me.aristhena.utils.Timer;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod(displayName = "Test Timer")
public class TestTimer extends Module
{
    private Timer timer;
    private boolean started;
    
    public TestTimer() {
        this.timer = new Timer();
    }
    
    @Override
    public void enable() {
        this.started = false;
        super.enable();
    }
    
    @EventTarget
    private void onMove(final MoveEvent event) {
        if (!this.started && (ClientUtils.player().moveForward != 0.0f || ClientUtils.player().moveStrafing != 0.0f)) {
            this.timer.reset();
            this.started = true;
        }
        else if (this.started && (ClientUtils.player().isCollidedHorizontally || (ClientUtils.player().moveForward == 0.0f && ClientUtils.player().moveStrafing == 0.0f))) {
            this.disable();
        }
    }
    
    @Override
    public void disable() {
        ClientUtils.sendMessage("Time: " + this.timer.getDifference() + " ms.");
        super.disable();
    }
}
