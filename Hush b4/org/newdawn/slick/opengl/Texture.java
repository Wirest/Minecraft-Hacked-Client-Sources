// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.opengl;

public interface Texture
{
    boolean hasAlpha();
    
    String getTextureRef();
    
    void bind();
    
    int getImageHeight();
    
    int getImageWidth();
    
    float getHeight();
    
    float getWidth();
    
    int getTextureHeight();
    
    int getTextureWidth();
    
    void release();
    
    int getTextureID();
    
    byte[] getTextureData();
    
    void setTextureFilter(final int p0);
}
