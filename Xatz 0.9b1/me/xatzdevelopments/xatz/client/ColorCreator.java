// 
// Decompiled by Procyon v0.5.36
// 

package me.xatzdevelopments.xatz.client;

import java.util.Iterator;
import java.awt.Color;
import java.util.ArrayList;

public class ColorCreator
{
    private static final ArrayList<Color> loggedColors;
    
    static {
        loggedColors = new ArrayList<Color>();
    }
    
    public static int create(final int r, final int g, final int b) {
        for (final Color color : ColorCreator.loggedColors) {
            if (color.getRed() == r && color.getGreen() == g && color.getBlue() == b && color.getAlpha() == 255) {
                return color.getRGB();
            }
        }
        Color color = new Color(r, g, b);
        ColorCreator.loggedColors.add(color);
        return color.getRGB();
    }
    
    public static int create(final int r, final int g, final int b, final int a) {
        for (final Color color : ColorCreator.loggedColors) {
            if (color.getRed() == r && color.getGreen() == g && color.getBlue() == b && color.getAlpha() == a) {
                return color.getRGB();
            }
        }
        Color color = new Color(r, g, b, a);
        ColorCreator.loggedColors.add(color);
        return color.getRGB();
    }
    
    public static int createRainbowFromOffset(final int speed, final int offset) {
        float hue = (float)((System.currentTimeMillis() + offset) % speed);
        return Color.getHSBColor(hue /= speed, 0.6f, 1.0f).getRGB();
    }
}
