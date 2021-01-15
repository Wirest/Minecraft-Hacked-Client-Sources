// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.movement;

import me.aristhena.event.events.MoveEvent;
import me.aristhena.event.EventTarget;
import me.aristhena.utils.ClientUtils;
import me.aristhena.event.Event;
import me.aristhena.event.events.UpdateEvent;
import me.aristhena.client.option.Option;
import me.aristhena.client.module.Module;
import me.aristhena.client.module.Module.Mod;
@Mod
public class Fly extends Module
{
    @Option.Op
    private boolean damage;
    @Option.Op(min = 0.0, max = 9.0, increment = 0.01)
    private double speed;
    
    public Fly() {
        this.speed = 0.8;
    }
    
    @EventTarget
    private void onUpdate(final UpdateEvent event) {
        if (event.getState() == Event.State.PRE) {
            if (ClientUtils.movementInput().jump) {
                ClientUtils.player().motionY = this.speed;
            }
            else if (ClientUtils.movementInput().sneak) {
                ClientUtils.player().motionY = -this.speed;
            }
            else {
                ClientUtils.player().motionY = 0.0;
            }
        }
    }
    
    @EventTarget
    private void onMove(final MoveEvent event) {
        ClientUtils.setMoveSpeed(event, this.speed);
    }
}
