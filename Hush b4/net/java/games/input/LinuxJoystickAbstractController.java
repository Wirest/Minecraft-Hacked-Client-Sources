// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

final class LinuxJoystickAbstractController extends AbstractController
{
    private final LinuxJoystickDevice device;
    
    protected LinuxJoystickAbstractController(final LinuxJoystickDevice device, final Component[] components, final Controller[] children, final Rumbler[] rumblers) {
        super(device.getName(), components, children, rumblers);
        this.device = device;
    }
    
    protected final void setDeviceEventQueueSize(final int size) throws IOException {
        this.device.setBufferSize(size);
    }
    
    public final void pollDevice() throws IOException {
        this.device.poll();
    }
    
    protected final boolean getNextDeviceEvent(final Event event) throws IOException {
        return this.device.getNextEvent(event);
    }
    
    public Controller.Type getType() {
        return Controller.Type.STICK;
    }
}
