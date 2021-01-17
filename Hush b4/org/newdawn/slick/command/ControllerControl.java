// 
// Decompiled by Procyon v0.5.36
// 

package org.newdawn.slick.command;

abstract class ControllerControl implements Control
{
    protected static final int BUTTON_EVENT = 0;
    protected static final int LEFT_EVENT = 1;
    protected static final int RIGHT_EVENT = 2;
    protected static final int UP_EVENT = 3;
    protected static final int DOWN_EVENT = 4;
    private int event;
    private int button;
    private int controllerNumber;
    
    protected ControllerControl(final int controllerNumber, final int event, final int button) {
        this.event = event;
        this.button = button;
        this.controllerNumber = controllerNumber;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (!(o instanceof ControllerControl)) {
            return false;
        }
        final ControllerControl c = (ControllerControl)o;
        return c.controllerNumber == this.controllerNumber && c.event == this.event && c.button == this.button;
    }
    
    @Override
    public int hashCode() {
        return this.event + this.button + this.controllerNumber;
    }
}
