// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.layout;

import java.awt.Dimension;
import java.awt.Rectangle;

public interface LayoutManager
{
    void reposition(final Rectangle p0, final Rectangle[] p1, final Constraint[][] p2);
    
    Dimension getOptimalPositionedSize(final Rectangle[] p0, final Constraint[][] p1);
}
