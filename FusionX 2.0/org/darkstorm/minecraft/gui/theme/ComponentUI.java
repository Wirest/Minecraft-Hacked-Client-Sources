// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.theme;

import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import org.darkstorm.minecraft.gui.component.Container;
import org.darkstorm.minecraft.gui.component.Component;

public interface ComponentUI
{
    void render(final Component p0);
    
    Rectangle getChildRenderArea(final Container p0);
    
    Dimension getDefaultSize(final Component p0);
    
    Color getDefaultBackgroundColor(final Component p0);
    
    Color getDefaultForegroundColor(final Component p0);
    
    Rectangle[] getInteractableRegions(final Component p0);
    
    void handleInteraction(final Component p0, final Point p1, final int p2);
    
    void handleUpdate(final Component p0);
}
