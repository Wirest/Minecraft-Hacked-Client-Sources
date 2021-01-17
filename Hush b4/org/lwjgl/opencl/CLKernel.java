// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

public final class CLKernel extends CLObjectChild<CLProgram>
{
    private static final CLKernelUtil util;
    
    CLKernel(final long pointer, final CLProgram program) {
        super(pointer, program);
        if (this.isValid()) {
            program.getCLKernelRegistry().registerObject(this);
        }
    }
    
    public CLKernel setArg(final int index, final byte value) {
        CLKernel.util.setArg(this, index, value);
        return this;
    }
    
    public CLKernel setArg(final int index, final short value) {
        CLKernel.util.setArg(this, index, value);
        return this;
    }
    
    public CLKernel setArg(final int index, final int value) {
        CLKernel.util.setArg(this, index, value);
        return this;
    }
    
    public CLKernel setArg(final int index, final long value) {
        CLKernel.util.setArg(this, index, value);
        return this;
    }
    
    public CLKernel setArg(final int index, final float value) {
        CLKernel.util.setArg(this, index, value);
        return this;
    }
    
    public CLKernel setArg(final int index, final double value) {
        CLKernel.util.setArg(this, index, value);
        return this;
    }
    
    public CLKernel setArg(final int index, final CLObject value) {
        CLKernel.util.setArg(this, index, value);
        return this;
    }
    
    public CLKernel setArgSize(final int index, final long size) {
        CLKernel.util.setArgSize(this, index, size);
        return this;
    }
    
    public String getInfoString(final int param_name) {
        return CLKernel.util.getInfoString(this, param_name);
    }
    
    public int getInfoInt(final int param_name) {
        return CLKernel.util.getInfoInt(this, param_name);
    }
    
    public long getWorkGroupInfoSize(final CLDevice device, final int param_name) {
        return CLKernel.util.getWorkGroupInfoSize(this, device, param_name);
    }
    
    public long[] getWorkGroupInfoSizeArray(final CLDevice device, final int param_name) {
        return CLKernel.util.getWorkGroupInfoSizeArray(this, device, param_name);
    }
    
    public long getWorkGroupInfoLong(final CLDevice device, final int param_name) {
        return CLKernel.util.getWorkGroupInfoLong(this, device, param_name);
    }
    
    @Override
    int release() {
        try {
            return super.release();
        }
        finally {
            if (!this.isValid()) {
                this.getParent().getCLKernelRegistry().unregisterObject(this);
            }
        }
    }
    
    static {
        util = (CLKernelUtil)CLPlatform.getInfoUtilInstance(CLKernel.class, "CL_KERNEL_UTIL");
    }
    
    interface CLKernelUtil extends InfoUtil<CLKernel>
    {
        void setArg(final CLKernel p0, final int p1, final byte p2);
        
        void setArg(final CLKernel p0, final int p1, final short p2);
        
        void setArg(final CLKernel p0, final int p1, final int p2);
        
        void setArg(final CLKernel p0, final int p1, final long p2);
        
        void setArg(final CLKernel p0, final int p1, final float p2);
        
        void setArg(final CLKernel p0, final int p1, final double p2);
        
        void setArg(final CLKernel p0, final int p1, final CLObject p2);
        
        void setArgSize(final CLKernel p0, final int p1, final long p2);
        
        long getWorkGroupInfoSize(final CLKernel p0, final CLDevice p1, final int p2);
        
        long[] getWorkGroupInfoSizeArray(final CLKernel p0, final CLDevice p1, final int p2);
        
        long getWorkGroupInfoLong(final CLKernel p0, final CLDevice p1, final int p2);
    }
}
