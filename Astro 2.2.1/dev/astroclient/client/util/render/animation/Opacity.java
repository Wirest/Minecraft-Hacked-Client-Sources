package dev.astroclient.client.util.render.animation;

public class Opacity {
    private float color;

    public Opacity(int color) {
        this.color = (float) color;
    }

    public void increase(float targetOpacity, float speed) {
        this.color += speed;
        if (color > targetOpacity)
            color = 0;
    }

    public float getColor() {
        return this.color;
    }

    public void setColor(float color) {
        this.color = color;
    }
}