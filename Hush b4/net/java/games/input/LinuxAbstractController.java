// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

final class LinuxAbstractController extends AbstractController
{
    private final Controller.PortType port;
    private final LinuxEventDevice device;
    private final Controller.Type type;
    
    protected LinuxAbstractController(final LinuxEventDevice device, final Component[] components, final Controller[] children, final Rumbler[] rumblers, final Controller.Type type) throws IOException {
        super(device.getName(), components, children, rumblers);
        this.device = device;
        this.port = device.getPortType();
        this.type = type;
    }
    
    public final Controller.PortType getPortType() {
        return this.port;
    }
    
    public final void pollDevice() throws IOException {
        this.device.pollKeyStates();
    }
    
    protected final boolean getNextDeviceEvent(final Event event) throws IOException {
        return LinuxControllers.getNextDeviceEvent(event, this.device);
    }
    
    public Controller.Type getType() {
        return this.type;
    }
}
