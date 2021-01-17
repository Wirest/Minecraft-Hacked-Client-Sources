// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

final class LinuxKeyboard extends Keyboard
{
    private final Controller.PortType port;
    private final LinuxEventDevice device;
    
    protected LinuxKeyboard(final LinuxEventDevice device, final Component[] components, final Controller[] children, final Rumbler[] rumblers) throws IOException {
        super(device.getName(), components, children, rumblers);
        this.device = device;
        this.port = device.getPortType();
    }
    
    public final Controller.PortType getPortType() {
        return this.port;
    }
    
    protected final boolean getNextDeviceEvent(final Event event) throws IOException {
        return LinuxControllers.getNextDeviceEvent(event, this.device);
    }
    
    public final void pollDevice() throws IOException {
        this.device.pollKeyStates();
    }
}
