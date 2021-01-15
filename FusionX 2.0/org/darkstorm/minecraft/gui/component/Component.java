// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.Point;
import org.darkstorm.minecraft.gui.theme.Theme;

public interface Component
{
    Theme getTheme();
    
    void setTheme(final Theme p0);
    
    void render();
    
    void update();
    
    int getX();
    
    int getY();
    
    int getWidth();
    
    int getHeight();
    
    void setX(final int p0);
    
    void setY(final int p0);
    
    void setWidth(final int p0);
    
    void setHeight(final int p0);
    
    Point getLocation();
    
    Dimension getSize();
    
    Rectangle getArea();
    
    Container getParent();
    
    Color getBackgroundColor();
    
    Color getForegroundColor();
    
    void setBackgroundColor(final Color p0);
    
    void setForegroundColor(final Color p0);
    
    void setParent(final Container p0);
    
    void onMousePress(final int p0, final int p1, final int p2);
    
    void onMouseRelease(final int p0, final int p1, final int p2);
    
    void resize();
    
    boolean isVisible();
    
    void setVisible(final boolean p0);
    
    boolean isEnabled();
    
    void setEnabled(final boolean p0);
}
