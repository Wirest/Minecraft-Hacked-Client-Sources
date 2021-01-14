package modification.events;

import modification.interfaces.Event;

public final class EventMovement
        implements Event {
    public double motionX;
    public double motionY;
    public double motionZ;

    public EventMovement(double paramDouble1, double paramDouble2, double paramDouble3) {
        this.motionX = paramDouble1;
        this.motionY = paramDouble2;
        this.motionZ = paramDouble3;
    }
}




