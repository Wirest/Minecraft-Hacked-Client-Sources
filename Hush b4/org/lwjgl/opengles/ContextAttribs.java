// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengles;

import org.lwjgl.BufferUtils;
import java.nio.IntBuffer;

public final class ContextAttribs
{
    private int version;
    
    public ContextAttribs() {
        this(2);
    }
    
    public ContextAttribs(final int version) {
        if (3 < version) {
            throw new IllegalArgumentException("Invalid OpenGL ES version specified: " + version);
        }
        this.version = version;
    }
    
    private ContextAttribs(final ContextAttribs attribs) {
        this.version = attribs.version;
    }
    
    public int getVersion() {
        return this.version;
    }
    
    public IntBuffer getAttribList() {
        final int attribCount = 1;
        final IntBuffer attribs = BufferUtils.createIntBuffer(attribCount * 2 + 1);
        attribs.put(12440).put(this.version);
        attribs.put(12344);
        attribs.rewind();
        return attribs;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(32);
        sb.append("ContextAttribs:");
        sb.append(" Version=").append(this.version);
        return sb.toString();
    }
}
