
package me.memewaredevs.altmanager;

public class Expand {
    private float x;
    private float y;
    private float expandX;
    private float expandY;
    private long lastMS;

    public Expand(float x, float y2, float expandX, float expandY) {
        this.x = x;
        this.y = y2;
        this.expandX = expandX;
        this.expandY = expandY;
    }

    public void hardInterpolate(float targetX, float targetY, int xSpeed, int ySpeed) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        this.expandX = AnimationUtil.calculateCompensation(targetX, this.expandX, 8L, xSpeed);
        this.expandY = AnimationUtil.calculateCompensation(targetY, this.expandY, 8L, ySpeed);
    }

    public void interpolate(float targetX, float targetY, int xSpeed, int ySpeed) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        if (delta > 60L) {
            delta = 16L;
        }
        this.lastMS = currentMS;
        this.expandX = AnimationUtil.calculateCompensation(targetX, this.expandX, delta, xSpeed);
        this.expandY = AnimationUtil.calculateCompensation(targetY, this.expandY, delta, ySpeed);
    }

    public float getExpandX() {
        return this.expandX;
    }

    public float getExpandY() {
        return this.expandY;
    }

    public void setExpandX(float expandX) {
        this.expandX = expandX;
    }

    public void setExpandY(float expandY) {
        this.expandY = expandY;
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

    public void setY(float y2) {
        this.y = y2;
    }
}

