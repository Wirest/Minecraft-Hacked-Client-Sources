/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.util;

import darkmagician6.EventManager;
import darkmagician6.EventTarget;
import darkmagician6.events.EventPreMotionUpdates;
import java.awt.Color;

public class RainbowUtil {
    private static Color color = Color.BLUE;
    public static float hue = 0.0f;

    public RainbowUtil() {
        EventManager.register(this);
    }

    @EventTarget
    public void onTick(EventPreMotionUpdates event) {
        float v = 90.0f;
        if ((hue += 2.0f) > 270.0f) {
            hue = 0.0f;
        }
        color = RainbowUtil.getColorViaHue(hue);
    }

    public Color getColor() {
        return color;
    }

    public static Color getColorViaHue(float hue) {
        float v = 100.0f;
        if (hue > 270.0f) {
            hue = 0.0f;
        }
        return Color.getHSBColor(hue / 270.0f, 0.7f, v / 100.0f);
    }
}

