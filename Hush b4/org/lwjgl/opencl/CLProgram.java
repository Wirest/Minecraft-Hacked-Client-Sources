// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.PointerBuffer;
import java.nio.ByteBuffer;

public final class CLProgram extends CLObjectChild<CLContext>
{
    private static final CLProgramUtil util;
    private final CLObjectRegistry<CLKernel> clKernels;
    
    CLProgram(final long pointer, final CLContext context) {
        super(pointer, context);
        if (this.isValid()) {
            context.getCLProgramRegistry().registerObject(this);
            this.clKernels = new CLObjectRegistry<CLKernel>();
        }
        else {
            this.clKernels = null;
        }
    }
    
    public CLKernel getCLKernel(final long id) {
        return this.clKernels.getObject(id);
    }
    
    public CLKernel[] createKernelsInProgram() {
        return CLProgram.util.createKernelsInProgram(this);
    }
    
    public String getInfoString(final int param_name) {
        return CLProgram.util.getInfoString(this, param_name);
    }
    
    public int getInfoInt(final int param_name) {
        return CLProgram.util.getInfoInt(this, param_name);
    }
    
    public long[] getInfoSizeArray(final int param_name) {
        return CLProgram.util.getInfoSizeArray(this, param_name);
    }
    
    public CLDevice[] getInfoDevices() {
        return CLProgram.util.getInfoDevices(this);
    }
    
    public ByteBuffer getInfoBinaries(final ByteBuffer target) {
        return CLProgram.util.getInfoBinaries(this, target);
    }
    
    public ByteBuffer[] getInfoBinaries(final ByteBuffer[] target) {
        return CLProgram.util.getInfoBinaries(this, target);
    }
    
    public String getBuildInfoString(final CLDevice device, final int param_name) {
        return CLProgram.util.getBuildInfoString(this, device, param_name);
    }
    
    public int getBuildInfoInt(final CLDevice device, final int param_name) {
        return CLProgram.util.getBuildInfoInt(this, device, param_name);
    }
    
    CLObjectRegistry<CLKernel> getCLKernelRegistry() {
        return this.clKernels;
    }
    
    void registerCLKernels(final PointerBuffer kernels) {
        for (int i = kernels.position(); i < kernels.limit(); ++i) {
            final long pointer = kernels.get(i);
            if (pointer != 0L) {
                new CLKernel(pointer, this);
            }
        }
    }
    
    @Override
    int release() {
        try {
            return super.release();
        }
        finally {
            if (!this.isValid()) {
                this.getParent().getCLProgramRegistry().unregisterObject(this);
            }
        }
    }
    
    static {
        util = (CLProgramUtil)CLPlatform.getInfoUtilInstance(CLProgram.class, "CL_PROGRAM_UTIL");
    }
    
    interface CLProgramUtil extends InfoUtil<CLProgram>
    {
        CLKernel[] createKernelsInProgram(final CLProgram p0);
        
        CLDevice[] getInfoDevices(final CLProgram p0);
        
        ByteBuffer getInfoBinaries(final CLProgram p0, final ByteBuffer p1);
        
        ByteBuffer[] getInfoBinaries(final CLProgram p0, final ByteBuffer[] p1);
        
        String getBuildInfoString(final CLProgram p0, final CLDevice p1, final int p2);
        
        int getBuildInfoInt(final CLProgram p0, final CLDevice p1, final int p2);
    }
}
