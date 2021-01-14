package splash.client.events.player;


import me.hippo.systems.lwjeb.event.Cancelable;

/**
 * Author: Ice
 * Created: 17:28, 30-May-20
 * Project: Client
 */
public class EventPostMotionUpdate extends Cancelable {

    private double x, y, z;
    private boolean onGround;

    public EventPostMotionUpdate(double x, double y, double z, boolean onGround) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.onGround = onGround;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}
