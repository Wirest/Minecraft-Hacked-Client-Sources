// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;
import org.lwjgl.opencl.CLEvent;
import org.lwjgl.opencl.CLContext;

public final class ARBCLEvent
{
    public static final int GL_SYNC_CL_EVENT_ARB = 33344;
    public static final int GL_SYNC_CL_EVENT_COMPLETE_ARB = 33345;
    
    private ARBCLEvent() {
    }
    
    public static GLSync glCreateSyncFromCLeventARB(final CLContext context, final CLEvent event, final int flags) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glCreateSyncFromCLeventARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        final GLSync __result = new GLSync(nglCreateSyncFromCLeventARB(context.getPointer(), event.getPointer(), flags, function_pointer));
        return __result;
    }
    
    static native long nglCreateSyncFromCLeventARB(final long p0, final long p1, final int p2, final long p3);
}
