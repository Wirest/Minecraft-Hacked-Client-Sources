/*
 * Decompiled with CFR 0.150.
 */
package delta.utils;

import java.awt.Color;

public class ColorUtils {
    public static Color getColor(long l, float f, int n) {
        float f2 = (float)(System.nanoTime() + l * (long)n) / 1.0E10f % 1.0f;
        long l2 = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(f2, f, 1.0f)), 16);
        Color color = new Color((int)l2);
        return new Color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f);
    }
}

