// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.BufferChecks;

public final class ARBComputeVariableGroupSize
{
    public static final int GL_MAX_COMPUTE_VARIABLE_GROUP_INVOCATIONS_ARB = 37700;
    public static final int GL_MAX_COMPUTE_FIXED_GROUP_INVOCATIONS_ARB = 37099;
    public static final int GL_MAX_COMPUTE_VARIABLE_GROUP_SIZE_ARB = 37701;
    public static final int GL_MAX_COMPUTE_FIXED_GROUP_SIZE_ARB = 37311;
    
    private ARBComputeVariableGroupSize() {
    }
    
    public static void glDispatchComputeGroupSizeARB(final int num_groups_x, final int num_groups_y, final int num_groups_z, final int group_size_x, final int group_size_y, final int group_size_z) {
        final ContextCapabilities caps = GLContext.getCapabilities();
        final long function_pointer = caps.glDispatchComputeGroupSizeARB;
        BufferChecks.checkFunctionAddress(function_pointer);
        nglDispatchComputeGroupSizeARB(num_groups_x, num_groups_y, num_groups_z, group_size_x, group_size_y, group_size_z, function_pointer);
    }
    
    static native void nglDispatchComputeGroupSizeARB(final int p0, final int p1, final int p2, final int p3, final int p4, final int p5, final long p6);
}
