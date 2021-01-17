// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.util;

public enum EnumWorldBlockLayer
{
    SOLID("SOLID", 0, "Solid"), 
    CUTOUT_MIPPED("CUTOUT_MIPPED", 1, "Mipped Cutout"), 
    CUTOUT("CUTOUT", 2, "Cutout"), 
    TRANSLUCENT("TRANSLUCENT", 3, "Translucent");
    
    private final String layerName;
    
    private EnumWorldBlockLayer(final String name, final int ordinal, final String layerNameIn) {
        this.layerName = layerNameIn;
    }
    
    @Override
    public String toString() {
        return this.layerName;
    }
}
