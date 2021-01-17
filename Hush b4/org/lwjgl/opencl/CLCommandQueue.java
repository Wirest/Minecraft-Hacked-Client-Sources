// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.PointerBuffer;

public final class CLCommandQueue extends CLObjectChild<CLContext>
{
    private static final InfoUtil<CLCommandQueue> util;
    private final CLDevice device;
    private final CLObjectRegistry<CLEvent> clEvents;
    
    CLCommandQueue(final long pointer, final CLContext context, final CLDevice device) {
        super(pointer, context);
        if (this.isValid()) {
            this.device = device;
            this.clEvents = new CLObjectRegistry<CLEvent>();
            context.getCLCommandQueueRegistry().registerObject(this);
        }
        else {
            this.device = null;
            this.clEvents = null;
        }
    }
    
    public CLDevice getCLDevice() {
        return this.device;
    }
    
    public CLEvent getCLEvent(final long id) {
        return this.clEvents.getObject(id);
    }
    
    public int getInfoInt(final int param_name) {
        return CLCommandQueue.util.getInfoInt(this, param_name);
    }
    
    CLObjectRegistry<CLEvent> getCLEventRegistry() {
        return this.clEvents;
    }
    
    void registerCLEvent(final PointerBuffer event) {
        if (event != null) {
            new CLEvent(event.get(event.position()), this);
        }
    }
    
    @Override
    int release() {
        try {
            return super.release();
        }
        finally {
            if (!this.isValid()) {
                this.getParent().getCLCommandQueueRegistry().unregisterObject(this);
            }
        }
    }
    
    static {
        util = CLPlatform.getInfoUtilInstance(CLCommandQueue.class, "CL_COMMAND_QUEUE_UTIL");
    }
}
