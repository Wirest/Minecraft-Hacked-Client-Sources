package info.sigmaclient.util.render;

import com.google.gson.annotations.Expose;

public class ColorContainer {
    @Expose
    private int red;
    @Expose
    private int green;
    @Expose
    private int blue;
    @Expose
    private int alpha;

    public ColorContainer(int r, int g, int b, int a) {
        red = r;
        green = g;
        blue = b;
        alpha = a;
    }

    public ColorContainer(int r, int g, int b) {
        this(r, g, b, 225);
    }

    public ColorContainer(int b) {
        this(b, b, b);
    }

    public ColorContainer(int b, int a) {
        this(b, b, b, a);
    }

    public void setColor(int r, int g, int b, int a) {
        red = r;
        green = g;
        blue = b;
        alpha = a;
    }

    public void setRed(int red) {
        this.red = red;
        setColor(red, green, blue, alpha);
    }

    public void setGreen(int green) {
        this.green = green;
        setColor(red, green, blue, alpha);
    }

    public void setBlue(int blue) {
        this.blue = blue;
        setColor(red, green, blue, alpha);
    }

    public void setAlpha(int alpha) {
        this.alpha = alpha;
        setColor(red, green, blue, alpha);
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getIntCol() {
        return Colors.getColor(red, green, blue, alpha);
    }
}
