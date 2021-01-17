// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.gui;

import org.lwjgl.input.Cursor;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Font;
import org.newdawn.slick.Input;

public interface GUIContext
{
    Input getInput();
    
    long getTime();
    
    int getScreenWidth();
    
    int getScreenHeight();
    
    int getWidth();
    
    int getHeight();
    
    Font getDefaultFont();
    
    void setMouseCursor(final String p0, final int p1, final int p2) throws SlickException;
    
    void setMouseCursor(final ImageData p0, final int p1, final int p2) throws SlickException;
    
    void setMouseCursor(final Cursor p0, final int p1, final int p2) throws SlickException;
    
    void setDefaultMouseCursor();
}
