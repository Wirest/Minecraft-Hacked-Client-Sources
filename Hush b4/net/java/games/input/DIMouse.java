// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

final class DIMouse extends Mouse
{
    private final IDirectInputDevice device;
    
    protected DIMouse(final IDirectInputDevice device, final Component[] components, final Controller[] children, final Rumbler[] rumblers) {
        super(device.getProductName(), components, children, rumblers);
        this.device = device;
    }
    
    public final void pollDevice() throws IOException {
        this.device.pollAll();
    }
    
    protected final boolean getNextDeviceEvent(final Event event) throws IOException {
        return DIControllers.getNextDeviceEvent(event, this.device);
    }
    
    protected final void setDeviceEventQueueSize(final int size) throws IOException {
        this.device.setBufferSize(size);
    }
}
