// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui;

import org.darkstorm.minecraft.gui.theme.Theme;
import org.darkstorm.minecraft.gui.component.Frame;

public interface GuiManager
{
    void setup();
    
    void addFrame(final Frame p0);
    
    void removeFrame(final Frame p0);
    
    Frame[] getFrames();
    
    void bringForward(final Frame p0);
    
    Theme getTheme();
    
    void setTheme(final Theme p0);
    
    void render();
    
    void renderPinned();
    
    void update();
}
