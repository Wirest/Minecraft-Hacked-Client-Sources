// 
// Decompiled by Procyon v0.5.36
// 

package oshi.util;

import java.math.BigDecimal;

public abstract class FormatUtil
{
    private static final long kibiByte = 1024L;
    private static final long mebiByte = 1048576L;
    private static final long gibiByte = 1073741824L;
    private static final long tebiByte = 1099511627776L;
    private static final long pebiByte = 1125899906842624L;
    
    public static String formatBytes(final long bytes) {
        if (bytes == 1L) {
            return String.format("%d byte", bytes);
        }
        if (bytes < 1024L) {
            return String.format("%d bytes", bytes);
        }
        if (bytes < 1048576L && bytes % 1024L == 0L) {
            return String.format("%.0f KB", bytes / 1024.0);
        }
        if (bytes < 1048576L) {
            return String.format("%.1f KB", bytes / 1024.0);
        }
        if (bytes < 1073741824L && bytes % 1048576L == 0L) {
            return String.format("%.0f MB", bytes / 1048576.0);
        }
        if (bytes < 1073741824L) {
            return String.format("%.1f MB", bytes / 1048576.0);
        }
        if (bytes % 1073741824L == 0L && bytes < 1099511627776L) {
            return String.format("%.0f GB", bytes / 1.073741824E9);
        }
        if (bytes < 1099511627776L) {
            return String.format("%.1f GB", bytes / 1.073741824E9);
        }
        if (bytes % 1099511627776L == 0L && bytes < 1125899906842624L) {
            return String.format("%.0f TiB", bytes / 1.099511627776E12);
        }
        if (bytes < 1125899906842624L) {
            return String.format("%.1f TiB", bytes / 1.099511627776E12);
        }
        return String.format("%d bytes", bytes);
    }
    
    public static float round(final float d, final int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, 4);
        return bd.floatValue();
    }
}
