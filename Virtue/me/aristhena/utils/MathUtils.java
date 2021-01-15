// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.utils;

import java.math.RoundingMode;
import java.math.BigDecimal;

public final class MathUtils
{
    public static double roundToPlace(final double value, final int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
