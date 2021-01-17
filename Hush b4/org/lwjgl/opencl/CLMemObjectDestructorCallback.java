// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.PointerWrapperAbstract;

public abstract class CLMemObjectDestructorCallback extends PointerWrapperAbstract
{
    protected CLMemObjectDestructorCallback() {
        super(CallbackUtil.getMemObjectDestructorCallback());
    }
    
    protected abstract void handleMessage(final long p0);
}
