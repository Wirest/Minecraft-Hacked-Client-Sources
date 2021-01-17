// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.PointerWrapperAbstract;

abstract class CLObject extends PointerWrapperAbstract
{
    protected CLObject(final long pointer) {
        super(pointer);
    }
    
    final long getPointerUnsafe() {
        return this.pointer;
    }
}
