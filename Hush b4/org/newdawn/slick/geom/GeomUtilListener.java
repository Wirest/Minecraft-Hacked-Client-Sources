// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

public interface GeomUtilListener
{
    void pointExcluded(final float p0, final float p1);
    
    void pointIntersected(final float p0, final float p1);
    
    void pointUsed(final float p0, final float p1);
}
