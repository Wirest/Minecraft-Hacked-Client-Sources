// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world;

public class ColorizerGrass
{
    private static int[] grassBuffer;
    
    static {
        ColorizerGrass.grassBuffer = new int[65536];
    }
    
    public static void setGrassBiomeColorizer(final int[] p_77479_0_) {
        ColorizerGrass.grassBuffer = p_77479_0_;
    }
    
    public static int getGrassColor(final double p_77480_0_, double p_77480_2_) {
        p_77480_2_ *= p_77480_0_;
        final int i = (int)((1.0 - p_77480_0_) * 255.0);
        final int j = (int)((1.0 - p_77480_2_) * 255.0);
        final int k = j << 8 | i;
        return (k > ColorizerGrass.grassBuffer.length) ? -65281 : ColorizerGrass.grassBuffer[k];
    }
}
