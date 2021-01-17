// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.LWJGLException;

public interface InputImplementation
{
    boolean hasWheel();
    
    int getButtonCount();
    
    void createMouse() throws LWJGLException;
    
    void destroyMouse();
    
    void pollMouse(final IntBuffer p0, final ByteBuffer p1);
    
    void readMouse(final ByteBuffer p0);
    
    void grabMouse(final boolean p0);
    
    int getNativeCursorCapabilities();
    
    void setCursorPosition(final int p0, final int p1);
    
    void setNativeCursor(final Object p0) throws LWJGLException;
    
    int getMinCursorSize();
    
    int getMaxCursorSize();
    
    void createKeyboard() throws LWJGLException;
    
    void destroyKeyboard();
    
    void pollKeyboard(final ByteBuffer p0);
    
    void readKeyboard(final ByteBuffer p0);
    
    Object createCursor(final int p0, final int p1, final int p2, final int p3, final int p4, final IntBuffer p5, final IntBuffer p6) throws LWJGLException;
    
    void destroyCursor(final Object p0);
    
    int getWidth();
    
    int getHeight();
    
    boolean isInsideWindow();
}
