
package me.memewaredevs.client.util.render;

import java.awt.Color;

public class Colors {
    public static int getColor(final Color color) {
        return Colors.getColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public static int getColor(final int brightness) {
        return Colors.getColor(brightness, brightness, brightness, 255);
    }

    public static int getColor(final int brightness, final int alpha) {
        return Colors.getColor(brightness, brightness, brightness, alpha);
    }

    public static int getColor(final int red, final int green, final int blue) {
        return Colors.getColor(red, green, blue, 255);
    }

    public static int getColor(final int red, final int green, final int blue, final int alpha) {
        int color = 0;
        color |= alpha << 24;
        color |= red << 16;
        color |= green << 8;
        return color |= blue;
    }
}
