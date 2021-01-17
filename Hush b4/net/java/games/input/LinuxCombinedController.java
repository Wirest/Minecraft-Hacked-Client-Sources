// 
// Decompiled by Procyon v0.5.36
// 

package net.java.games.input;

import java.io.IOException;

public class LinuxCombinedController extends AbstractController
{
    private LinuxAbstractController eventController;
    private LinuxJoystickAbstractController joystickController;
    
    LinuxCombinedController(final LinuxAbstractController eventController, final LinuxJoystickAbstractController joystickController) {
        super(eventController.getName(), joystickController.getComponents(), eventController.getControllers(), eventController.getRumblers());
        this.eventController = eventController;
        this.joystickController = joystickController;
    }
    
    protected boolean getNextDeviceEvent(final Event event) throws IOException {
        return this.joystickController.getNextDeviceEvent(event);
    }
    
    public final Controller.PortType getPortType() {
        return this.eventController.getPortType();
    }
    
    public final void pollDevice() throws IOException {
        this.eventController.pollDevice();
        this.joystickController.pollDevice();
    }
    
    public Controller.Type getType() {
        return this.eventController.getType();
    }
}
