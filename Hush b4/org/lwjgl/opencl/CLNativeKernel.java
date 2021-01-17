// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import java.nio.ByteBuffer;
import org.lwjgl.PointerWrapperAbstract;

public abstract class CLNativeKernel extends PointerWrapperAbstract
{
    protected CLNativeKernel() {
        super(CallbackUtil.getNativeKernelCallback());
    }
    
    protected abstract void execute(final ByteBuffer[] p0);
}
