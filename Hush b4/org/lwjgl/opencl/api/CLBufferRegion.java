// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl.api;

import org.lwjgl.PointerBuffer;

public final class CLBufferRegion
{
    public static final int STRUCT_SIZE;
    private final int origin;
    private final int size;
    
    public CLBufferRegion(final int origin, final int size) {
        this.origin = origin;
        this.size = size;
    }
    
    public int getOrigin() {
        return this.origin;
    }
    
    public int getSize() {
        return this.size;
    }
    
    static {
        STRUCT_SIZE = 2 * PointerBuffer.getPointerSize();
    }
}
