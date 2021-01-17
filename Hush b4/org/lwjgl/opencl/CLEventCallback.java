// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opencl;

import org.lwjgl.PointerWrapperAbstract;

public abstract class CLEventCallback extends PointerWrapperAbstract
{
    private CLObjectRegistry<CLEvent> eventRegistry;
    
    protected CLEventCallback() {
        super(CallbackUtil.getEventCallback());
    }
    
    void setRegistry(final CLObjectRegistry<CLEvent> eventRegistry) {
        this.eventRegistry = eventRegistry;
    }
    
    private void handleMessage(final long event_address, final int event_command_exec_status) {
        this.handleMessage(this.eventRegistry.getObject(event_address), event_command_exec_status);
    }
    
    protected abstract void handleMessage(final CLEvent p0, final int p1);
}
