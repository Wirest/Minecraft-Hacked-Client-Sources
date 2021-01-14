package modification.events;

import modification.interfaces.Event;

public final class EventMoveFlying
        implements Event {
    public float strafe;
    public float forward;
    public float friction;
    public float yaw;
    public boolean canceled;

    public EventMoveFlying(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4) {
        this.strafe = paramFloat1;
        this.forward = paramFloat2;
        this.friction = paramFloat3;
        this.yaw = paramFloat4;
    }
}




