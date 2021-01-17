// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.ai.attributes;

public interface IAttribute
{
    String getAttributeUnlocalizedName();
    
    double clampValue(final double p0);
    
    double getDefaultValue();
    
    boolean getShouldWatch();
    
    IAttribute func_180372_d();
}
