// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.utils;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.awt.Color;
import java.util.Random;

public class ColorUtil
{
    public static int generateRandomColorHex() {
        final Random randomColor = new Random();
        final StringBuilder sb = new StringBuilder();
        sb.append("0x");
        while (sb.length() < 10) {
            sb.append(Integer.toHexString(randomColor.nextInt()));
        }
        sb.setLength(8);
        return Integer.decode(sb.toString());
    }
    
    public static Color getReversedColor(final Color color) {
        final int rgb = color.getRGB();
        final int inverted = 16777215 - (rgb | 0xFF000000) | (rgb & 0xFF000000);
        return new Color(inverted);
    }
}
