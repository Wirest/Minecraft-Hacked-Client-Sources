// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.utils;

import java.awt.Color;

public class ColorUtils
{
    public static Color rainbowEffect(final long offset, final float fade) {
        final float hue = System.nanoTime() + offset / 1.0E10f % 1.0f;
        final long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0f, 1.0f)), 16);
        final Color c = new Color((int)color);
        return new Color(c.getRed() / 255.0f * fade, c.getGreen() / 255.0f * fade, c.getBlue() / 255.0f * fade, c.getAlpha() / 255.0f);
    }
    
    private int getRainbow(final int speed, final int offset) {
        float hue = (float)((System.currentTimeMillis() + offset) % speed);
        hue /= speed;
        return Color.getHSBColor(hue, 1.0f, 1.0f).getRGB();
    }
}
