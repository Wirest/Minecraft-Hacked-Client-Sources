// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick;

import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.geom.Shape;

public interface ShapeFill
{
    Color colorAt(final Shape p0, final float p1, final float p2);
    
    Vector2f getOffsetAt(final Shape p0, final float p1, final float p2);
}
