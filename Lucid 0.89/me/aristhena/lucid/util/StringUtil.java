/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.util;

public class StringUtil {
    public static String capitalize(String line) {
        return String.valueOf(Character.toUpperCase(line.charAt(0))) + line.substring(1);
    }
}

