package me.Corbis.Execution.ui.AltManager;

import java.awt.*;

public class Colors {
    public static int getColor(Color color) {
        return getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int getColor(int brightness) {
        return getColor(brightness, brightness, brightness, 255);
    }

    public static int getColor(int brightness, int alpha) {
        return getColor(brightness, brightness, brightness, alpha);
    }

    public static int getColor(int red, int green, int blue) {
        return getColor(red, green, blue, 255);
    }

    public static int getColor(int red, int green, int blue, int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        color |= blue;
        return color;
    }
    public static int getRainbowcolor(int yOffset){
        float speed = 3000f;
        float hue = (float) (System.currentTimeMillis() % (int)speed) + (yOffset * 8);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        return (int) hue;
    }
}
