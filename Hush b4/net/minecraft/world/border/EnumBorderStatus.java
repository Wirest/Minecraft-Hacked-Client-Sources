// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.border;

public enum EnumBorderStatus
{
    GROWING("GROWING", 0, 4259712), 
    SHRINKING("SHRINKING", 1, 16724016), 
    STATIONARY("STATIONARY", 2, 2138367);
    
    private final int id;
    
    private EnumBorderStatus(final String name, final int ordinal, final int id) {
        this.id = id;
    }
    
    public int getID() {
        return this.id;
    }
}
