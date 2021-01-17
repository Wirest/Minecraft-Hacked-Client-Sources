// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

public final class CLEvent extends CLObjectChild<CLContext>
{
    private static final CLEventUtil util;
    private final CLCommandQueue queue;
    
    CLEvent(final long pointer, final CLContext context) {
        this(pointer, context, null);
    }
    
    CLEvent(final long pointer, final CLCommandQueue queue) {
        this(pointer, queue.getParent(), queue);
    }
    
    CLEvent(final long pointer, final CLContext context, final CLCommandQueue queue) {
        super(pointer, context);
        if (this.isValid()) {
            if ((this.queue = queue) == null) {
                context.getCLEventRegistry().registerObject(this);
            }
            else {
                queue.getCLEventRegistry().registerObject(this);
            }
        }
        else {
            this.queue = null;
        }
    }
    
    public CLCommandQueue getCLCommandQueue() {
        return this.queue;
    }
    
    public int getInfoInt(final int param_name) {
        return CLEvent.util.getInfoInt(this, param_name);
    }
    
    public long getProfilingInfoLong(final int param_name) {
        return CLEvent.util.getProfilingInfoLong(this, param_name);
    }
    
    CLObjectRegistry<CLEvent> getParentRegistry() {
        if (this.queue == null) {
            return this.getParent().getCLEventRegistry();
        }
        return this.queue.getCLEventRegistry();
    }
    
    @Override
    int release() {
        try {
            return super.release();
        }
        finally {
            if (!this.isValid()) {
                if (this.queue == null) {
                    this.getParent().getCLEventRegistry().unregisterObject(this);
                }
                else {
                    this.queue.getCLEventRegistry().unregisterObject(this);
                }
            }
        }
    }
    
    static {
        util = (CLEventUtil)CLPlatform.getInfoUtilInstance(CLEvent.class, "CL_EVENT_UTIL");
    }
    
    interface CLEventUtil extends InfoUtil<CLEvent>
    {
        long getProfilingInfoLong(final CLEvent p0, final int p1);
    }
}
