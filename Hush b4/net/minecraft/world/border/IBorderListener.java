// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.border;

public interface IBorderListener
{
    void onSizeChanged(final WorldBorder p0, final double p1);
    
    void onTransitionStarted(final WorldBorder p0, final double p1, final double p2, final long p3);
    
    void onCenterChanged(final WorldBorder p0, final double p1, final double p2);
    
    void onWarningTimeChanged(final WorldBorder p0, final int p1);
    
    void onWarningDistanceChanged(final WorldBorder p0, final int p1);
    
    void onDamageAmountChanged(final WorldBorder p0, final double p1);
    
    void onDamageBufferChanged(final WorldBorder p0, final double p1);
}
