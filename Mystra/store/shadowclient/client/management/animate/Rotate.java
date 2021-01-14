package store.shadowclient.client.management.animate;

public class Rotate {
    private float angle;
    private long lastMS;

    public Rotate(float angle) {
        this.angle = angle;
    }

    public void interpolate(float targetAngle, int speed) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        this.angle = AnimationUtil.calculateCompensation(targetAngle, this.angle, delta, speed);
    }

    public float getAngle() {
        return this.angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}

