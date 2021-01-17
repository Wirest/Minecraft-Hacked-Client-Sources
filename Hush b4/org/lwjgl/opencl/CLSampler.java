// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

public final class CLSampler extends CLObjectChild<CLContext>
{
    private static final InfoUtil<CLSampler> util;
    
    CLSampler(final long pointer, final CLContext context) {
        super(pointer, context);
        if (this.isValid()) {
            context.getCLSamplerRegistry().registerObject(this);
        }
    }
    
    public int getInfoInt(final int param_name) {
        return CLSampler.util.getInfoInt(this, param_name);
    }
    
    public long getInfoLong(final int param_name) {
        return CLSampler.util.getInfoLong(this, param_name);
    }
    
    @Override
    int release() {
        try {
            return super.release();
        }
        finally {
            if (!this.isValid()) {
                this.getParent().getCLSamplerRegistry().unregisterObject(this);
            }
        }
    }
    
    static {
        util = CLPlatform.getInfoUtilInstance(CLSampler.class, "CL_SAMPLER_UTIL");
    }
}
