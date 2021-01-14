
package me.memewaredevs.altmanager;

public class Scale {
    private float centerX;
    private float centerY;
    private float width;
    private float height;
    private long lastMS;

    public Scale(float centerX, float centerY, float width, float height) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.height = height;
        this.width = width;
        this.lastMS = System.currentTimeMillis();
    }

    public void interpolate(float tWidth, float tHeight, int speed) {
        long currentMS = System.currentTimeMillis();
        long delta = currentMS - this.lastMS;
        this.lastMS = currentMS;
        float diffW = this.width - tWidth;
        this.width = diffW > (float) speed ? (this.width = this.width - (float) ((long) speed * delta / 16L)) : (diffW < (float) (-speed) ? (this.width = this.width + (float) ((long) speed * delta / 16L)) : tWidth);
        float diffH = this.height - tHeight;
        this.height = diffH > (float) speed ? (this.height = this.height - (float) ((long) speed * delta / 16L)) : (diffH < (float) (-speed) ? (this.height = this.height + (float) ((long) speed * delta / 16L)) : tHeight);
    }

    public float getCenterX() {
        return this.centerX;
    }

    public float getCenterY() {
        return this.centerY;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }
}

