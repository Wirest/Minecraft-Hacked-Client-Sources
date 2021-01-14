package store.shadowclient.client.management.animate;

public class Translate {
    private float x;
    private float y;
    private long lastMS;

    public Translate(float x, float y) {
        this.x = x;
        this.y = y;
        this.lastMS = System.currentTimeMillis();
    }

    public void interpolate(float targetX, float targetY, int xSpeed, int ySpeed) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        this.x = AnimationUtil.calculateCompensation(targetX, this.x, delta, xSpeed);
        this.y = AnimationUtil.calculateCompensation(targetY, this.y, delta, ySpeed);
    }

    public void interpolate(float targetX, float targetY, int speed) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        this.x = AnimationUtil.calculateCompensation(targetX, this.x, delta, speed);
        this.y = AnimationUtil.calculateCompensation(targetY, this.y, delta, speed);
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }
}

