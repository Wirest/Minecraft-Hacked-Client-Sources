// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;

public class Quadric
{
    protected int drawStyle;
    protected int orientation;
    protected boolean textureFlag;
    protected int normals;
    
    public Quadric() {
        this.drawStyle = 100012;
        this.orientation = 100020;
        this.textureFlag = false;
        this.normals = 100000;
    }
    
    protected void normal3f(float x, float y, float z) {
        final float mag = (float)Math.sqrt(x * x + y * y + z * z);
        if (mag > 1.0E-5f) {
            x /= mag;
            y /= mag;
            z /= mag;
        }
        GL11.glNormal3f(x, y, z);
    }
    
    public void setDrawStyle(final int drawStyle) {
        this.drawStyle = drawStyle;
    }
    
    public void setNormals(final int normals) {
        this.normals = normals;
    }
    
    public void setOrientation(final int orientation) {
        this.orientation = orientation;
    }
    
    public void setTextureFlag(final boolean textureFlag) {
        this.textureFlag = textureFlag;
    }
    
    public int getDrawStyle() {
        return this.drawStyle;
    }
    
    public int getNormals() {
        return this.normals;
    }
    
    public int getOrientation() {
        return this.orientation;
    }
    
    public boolean getTextureFlag() {
        return this.textureFlag;
    }
    
    protected void TXTR_COORD(final float x, final float y) {
        if (this.textureFlag) {
            GL11.glTexCoord2f(x, y);
        }
    }
    
    protected float sin(final float r) {
        return (float)Math.sin(r);
    }
    
    protected float cos(final float r) {
        return (float)Math.cos(r);
    }
}
