/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.util;

import java.awt.Color;

public class ColorUtil {
    public static int blend(int color1, int color2, float perc) {
        Color blended;
        Color x = new Color(color1);
        Color y = new Color(color2);
        float inverse_blending = 1.0f - perc;
        float red = (float)x.getRed() * perc + (float)y.getRed() * inverse_blending;
        float green = (float)x.getGreen() * perc + (float)y.getGreen() * inverse_blending;
        float blue = (float)x.getBlue() * perc + (float)y.getBlue() * inverse_blending;
        try {
            blended = new Color(red / 255.0f, green / 255.0f, blue / 255.0f);
        }
        catch (Exception e) {
            blended = new Color(-1);
        }
        return blended.getRGB();
    }
}

