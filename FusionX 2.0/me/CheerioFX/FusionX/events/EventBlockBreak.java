// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.events;

import net.minecraft.util.EnumFacing;
import com.darkmagician6.eventapi.events.callables.EventCancellable;

public class EventBlockBreak extends EventCancellable
{
    private int[] coords;
    private EnumFacing facing;
    
    public EventBlockBreak(final int x, final int y, final int z, final EnumFacing facing) {
        this.facing = facing;
        (this.coords = new int[3])[0] = x;
        this.coords[1] = y;
        this.coords[2] = z;
    }
    
    public int[] getCoords() {
        return this.coords;
    }
    
    public int getX() {
        return this.coords[0];
    }
    
    public int getY() {
        return this.coords[1];
    }
    
    public EnumFacing getFacing() {
        return this.facing;
    }
    
    public int getZ() {
        return this.coords[2];
    }
}
