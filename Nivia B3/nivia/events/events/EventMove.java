package nivia.events.events;

import nivia.events.Event;

public class EventMove implements Event
{
    public double x;
    public double y;
    public double z;

    public EventMove(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getMotionX() {
        return this.x;
    }

    public double getMotionY() {
        return this.y;
    }

    public double getMotionZ() {
        return this.z;
    }

    public void setMotionX(final double motionX) {
        this.x = motionX;
    }

    public void setMotionY(final double motionY) {
        this.y = motionY;
    }

    public void setMotionZ(final double motionZ) {
        this.z = motionZ;
    }
}
