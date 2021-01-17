// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu;

import org.lwjgl.opengl.GL11;

class PixelStoreState extends Util
{
    public int unpackRowLength;
    public int unpackAlignment;
    public int unpackSkipRows;
    public int unpackSkipPixels;
    public int packRowLength;
    public int packAlignment;
    public int packSkipRows;
    public int packSkipPixels;
    
    PixelStoreState() {
        this.load();
    }
    
    public void load() {
        this.unpackRowLength = Util.glGetIntegerv(3314);
        this.unpackAlignment = Util.glGetIntegerv(3317);
        this.unpackSkipRows = Util.glGetIntegerv(3315);
        this.unpackSkipPixels = Util.glGetIntegerv(3316);
        this.packRowLength = Util.glGetIntegerv(3330);
        this.packAlignment = Util.glGetIntegerv(3333);
        this.packSkipRows = Util.glGetIntegerv(3331);
        this.packSkipPixels = Util.glGetIntegerv(3332);
    }
    
    public void save() {
        GL11.glPixelStorei(3314, this.unpackRowLength);
        GL11.glPixelStorei(3317, this.unpackAlignment);
        GL11.glPixelStorei(3315, this.unpackSkipRows);
        GL11.glPixelStorei(3316, this.unpackSkipPixels);
        GL11.glPixelStorei(3330, this.packRowLength);
        GL11.glPixelStorei(3333, this.packAlignment);
        GL11.glPixelStorei(3331, this.packSkipRows);
        GL11.glPixelStorei(3332, this.packSkipPixels);
    }
}
