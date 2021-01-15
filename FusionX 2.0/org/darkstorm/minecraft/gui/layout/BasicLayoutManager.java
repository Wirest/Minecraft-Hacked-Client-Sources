// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.layout;

import java.awt.Dimension;
import java.awt.Rectangle;

public class BasicLayoutManager implements LayoutManager
{
    @Override
    public void reposition(final Rectangle area, final Rectangle[] componentAreas, final Constraint[][] constraints) {
        int offset = 0;
        for (final Rectangle componentArea : componentAreas) {
            if (componentArea == null) {
                throw new NullPointerException();
            }
            componentArea.x = area.x;
            componentArea.y = area.y + offset;
            offset += componentArea.height;
        }
    }
    
    @Override
    public Dimension getOptimalPositionedSize(final Rectangle[] componentAreas, final Constraint[][] constraints) {
        int width = 0;
        int height = 0;
        for (final Rectangle component : componentAreas) {
            if (component == null) {
                throw new NullPointerException();
            }
            height += component.height;
            width = Math.max(width, component.width);
        }
        return new Dimension(width, height);
    }
}
