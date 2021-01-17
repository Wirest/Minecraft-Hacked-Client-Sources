// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu;

public interface GLUtessellatorCallback
{
    void begin(final int p0);
    
    void beginData(final int p0, final Object p1);
    
    void edgeFlag(final boolean p0);
    
    void edgeFlagData(final boolean p0, final Object p1);
    
    void vertex(final Object p0);
    
    void vertexData(final Object p0, final Object p1);
    
    void end();
    
    void endData(final Object p0);
    
    void combine(final double[] p0, final Object[] p1, final float[] p2, final Object[] p3);
    
    void combineData(final double[] p0, final Object[] p1, final float[] p2, final Object[] p3, final Object p4);
    
    void error(final int p0);
    
    void errorData(final int p0, final Object p1);
}
