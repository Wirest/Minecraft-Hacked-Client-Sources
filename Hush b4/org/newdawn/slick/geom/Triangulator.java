// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.geom;

import java.io.Serializable;

public interface Triangulator extends Serializable
{
    int getTriangleCount();
    
    float[] getTrianglePoint(final int p0, final int p1);
    
    void addPolyPoint(final float p0, final float p1);
    
    void startHole();
    
    boolean triangulate();
}
