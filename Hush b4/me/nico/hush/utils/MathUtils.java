// 
// Decompiled by Procyon v0.5.36
// 

package me.nico.hush.utils;

import java.math.RoundingMode;
import java.math.BigDecimal;
import java.util.Random;

public class MathUtils
{
    private static final Random rng;
    
    static {
        rng = new Random();
    }
    
    public static float map(final float x, final float prev_min, final float prev_max, final float new_min, final float new_max) {
        return (x - prev_min) / (prev_max - prev_min) * (new_max - new_min) + new_min;
    }
    
    public static boolean contains(final float x, final float y, final float minX, final float minY, final float maxX, final float maxY) {
        return x > minX && x < maxX && y > minY && y < maxY;
    }
    
    public static boolean isInteger(final String num) {
        try {
            Integer.parseInt(num);
            return true;
        }
        catch (NumberFormatException var2) {
            var2.printStackTrace();
            return false;
        }
    }
    
    public static int getMid(final int x1, final int x2) {
        return (x1 + x2) / 2;
    }
    
    public static boolean isDouble(final String num) {
        try {
            Double.parseDouble(num);
            return true;
        }
        catch (NumberFormatException var2) {
            var2.printStackTrace();
            return false;
        }
    }
    
    public static boolean isFloat(final String num) {
        try {
            Float.parseFloat(num);
            return true;
        }
        catch (NumberFormatException var2) {
            var2.printStackTrace();
            return false;
        }
    }
    
    public static boolean isLong(final String num) {
        try {
            Long.parseLong(num);
            return true;
        }
        catch (NumberFormatException var2) {
            var2.printStackTrace();
            return false;
        }
    }
    
    public static Random getRng() {
        return MathUtils.rng;
    }
    
    public static double round(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static float getRandom() {
        return MathUtils.rng.nextFloat();
    }
    
    public static int getRandom(final int cap) {
        return MathUtils.rng.nextInt(cap);
    }
    
    public static int getRandom(final int floor, final int cap) {
        return floor + MathUtils.rng.nextInt(cap - floor + 1);
    }
    
    public static int randInt(final int min, final int max) {
        return new Random().nextInt(max - min + 1) + min;
    }
    
    public static float clampValue(final float value, final float floor, final float cap) {
        if (value < floor) {
            return floor;
        }
        return (value > cap) ? cap : value;
    }
    
    public static float getSimilarity(final String string1, final String string2) {
        final int halflen = Math.min(string1.length(), string2.length()) / 2 + Math.min(string1.length(), string2.length()) % 2;
        final StringBuffer common1 = getCommonCharacters(string1, string2, halflen);
        final StringBuffer common2 = getCommonCharacters(string2, string1, halflen);
        if (common1.length() == 0 || common2.length() == 0) {
            return 0.0f;
        }
        if (common1.length() != common2.length()) {
            return 0.0f;
        }
        int transpositions = 0;
        for (int n = common1.length(), i = 0; i < n; ++i) {
            if (common1.charAt(i) != common2.charAt(i)) {
                ++transpositions;
            }
        }
        transpositions /= 2;
        return (common1.length() / string1.length() + common2.length() / string2.length() + (common1.length() - transpositions) / common1.length()) / 3.0f;
    }
    
    private static StringBuffer getCommonCharacters(final String string1, final String string2, final int distanceSep) {
        final StringBuffer returnCommons = new StringBuffer();
        final StringBuffer copy = new StringBuffer(string2);
        final int n = string1.length();
        final int m = string2.length();
        for (int i = 0; i < n; ++i) {
            final char ch = string1.charAt(i);
            boolean foundIt = false;
            for (int j = Math.max(0, i - distanceSep); !foundIt && j < Math.min(i + distanceSep, m - 1); ++j) {
                if (copy.charAt(j) == ch) {
                    foundIt = true;
                    returnCommons.append(ch);
                }
            }
        }
        return returnCommons;
    }
    
    public static double meme(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    
    public static int customRandInt(final int min, final int max) {
        return new Random().nextInt(max - min + 1) + min;
    }
    
    public static double roundToPlace(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
