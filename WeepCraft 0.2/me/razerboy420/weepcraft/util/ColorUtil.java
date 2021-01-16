/*
 * Decompiled with CFR 0_122.
 */
package me.razerboy420.weepcraft.util;

import me.razerboy420.weepcraft.settings.EnumColor;

public class ColorUtil {
    public static String g = "no";

    public static String getColor(EnumColor color) {
        int count = 0;
        EnumColor[] arrenumColor = EnumColor.values();
        int n = arrenumColor.length;
        int n2 = 0;
        while (n2 < n) {
            EnumColor e = arrenumColor[n2];
            if (e == color && count < 10) {
                return "\u00a7" + count;
            }
            ++count;
            ++n2;
        }
        if (color == EnumColor.Green) {
            return "\u00a7a";
        }
        if (color == EnumColor.Aqua) {
            return "\u00a7b";
        }
        if (color == EnumColor.Red) {
            return "\u00a7c";
        }
        if (color == EnumColor.Purple) {
            return "\u00a7d";
        }
        if (color == EnumColor.Yellow) {
            return "\u00a7e";
        }
        if (color == EnumColor.White) {
            return "\u00a7f";
        }
        return "\u00a70";
    }

    public static int getHexColor(EnumColor color) {
        if (color == EnumColor.Black) {
            return -16777216;
        }
        if (color == EnumColor.DarkBlue) {
            return -16777046;
        }
        if (color == EnumColor.DarkGreen) {
            return -16733696;
        }
        if (color == EnumColor.DarkAqua) {
            return -16733526;
        }
        if (color == EnumColor.DarkRed) {
            return -5636096;
        }
        if (color == EnumColor.DarkPurple) {
            return -5635926;
        }
        if (color == EnumColor.Gold) {
            return -22016;
        }
        if (color == EnumColor.Gray) {
            return -5592406;
        }
        if (color == EnumColor.DarkGray) {
            return -11184811;
        }
        if (color == EnumColor.Blue) {
            return -11184641;
        }
        if (color == EnumColor.Green) {
            return -11141291;
        }
        if (color == EnumColor.Aqua) {
            return -11141121;
        }
        if (color == EnumColor.Red) {
            return -43691;
        }
        if (color == EnumColor.Purple) {
            return -43521;
        }
        if (color == EnumColor.Yellow) {
            return -171;
        }
        if (color == EnumColor.White) {
            return -1;
        }
        return -16777216;
    }
}

