// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

abstract class CLObjectRetainable extends CLObject
{
    private int refCount;
    
    protected CLObjectRetainable(final long pointer) {
        super(pointer);
        if (super.isValid()) {
            this.refCount = 1;
        }
    }
    
    public final int getReferenceCount() {
        return this.refCount;
    }
    
    @Override
    public final boolean isValid() {
        return this.refCount > 0;
    }
    
    int retain() {
        this.checkValid();
        return ++this.refCount;
    }
    
    int release() {
        this.checkValid();
        return --this.refCount;
    }
}
