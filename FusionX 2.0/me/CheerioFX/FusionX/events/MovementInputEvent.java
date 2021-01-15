// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.events;

import net.minecraft.util.MovementInput;
import com.darkmagician6.eventapi.events.Event;

public class MovementInputEvent implements Event
{
    private MovementInput movementInput;
    
    public MovementInputEvent(final MovementInput movementInput) {
        this.movementInput = movementInput;
    }
    
    public MovementInput getMovementInput() {
        return this.movementInput;
    }
}
