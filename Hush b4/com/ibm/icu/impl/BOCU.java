// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import com.ibm.icu.text.UCharacterIterator;

public class BOCU
{
    private static final int SLOPE_MIN_ = 3;
    private static final int SLOPE_MAX_ = 255;
    private static final int SLOPE_MIDDLE_ = 129;
    private static final int SLOPE_TAIL_COUNT_ = 253;
    private static final int SLOPE_SINGLE_ = 80;
    private static final int SLOPE_LEAD_2_ = 42;
    private static final int SLOPE_LEAD_3_ = 3;
    private static final int SLOPE_REACH_POS_1_ = 80;
    private static final int SLOPE_REACH_NEG_1_ = -80;
    private static final int SLOPE_REACH_POS_2_ = 10667;
    private static final int SLOPE_REACH_NEG_2_ = -10668;
    private static final int SLOPE_REACH_POS_3_ = 192785;
    private static final int SLOPE_REACH_NEG_3_ = -192786;
    private static final int SLOPE_START_POS_2_ = 210;
    private static final int SLOPE_START_POS_3_ = 252;
    private static final int SLOPE_START_NEG_2_ = 49;
    private static final int SLOPE_START_NEG_3_ = 7;
    
    public static int compress(final String source, final byte[] buffer, int offset) {
        int prev = 0;
        final UCharacterIterator iterator = UCharacterIterator.getInstance(source);
        for (int codepoint = iterator.nextCodePoint(); codepoint != -1; codepoint = iterator.nextCodePoint()) {
            if (prev < 19968 || prev >= 40960) {
                prev = (prev & 0xFFFFFF80) + 80;
            }
            else {
                prev = 30292;
            }
            offset = writeDiff(codepoint - prev, buffer, offset);
            prev = codepoint;
        }
        return offset;
    }
    
    public static int getCompressionLength(final String source) {
        int prev = 0;
        int result = 0;
        final UCharacterIterator iterator = UCharacterIterator.getInstance(source);
        for (int codepoint = iterator.nextCodePoint(); codepoint != -1; codepoint = iterator.nextCodePoint(), result += lengthOfDiff(codepoint - prev), prev = codepoint) {
            if (prev < 19968 || prev >= 40960) {
                prev = (prev & 0xFFFFFF80) + 80;
            }
            else {
                prev = 30292;
            }
        }
        return result;
    }
    
    private BOCU() {
    }
    
    private static final long getNegDivMod(final int number, final int factor) {
        int modulo = number % factor;
        long result = number / factor;
        if (modulo < 0) {
            --result;
            modulo += factor;
        }
        return result << 32 | (long)modulo;
    }
    
    private static final int writeDiff(int diff, final byte[] buffer, int offset) {
        if (diff >= -80) {
            if (diff <= 80) {
                buffer[offset++] = (byte)(129 + diff);
            }
            else if (diff <= 10667) {
                buffer[offset++] = (byte)(210 + diff / 253);
                buffer[offset++] = (byte)(3 + diff % 253);
            }
            else if (diff <= 192785) {
                buffer[offset + 2] = (byte)(3 + diff % 253);
                diff /= 253;
                buffer[offset + 1] = (byte)(3 + diff % 253);
                buffer[offset] = (byte)(252 + diff / 253);
                offset += 3;
            }
            else {
                buffer[offset + 3] = (byte)(3 + diff % 253);
                diff /= 253;
                buffer[offset] = (byte)(3 + diff % 253);
                diff /= 253;
                buffer[offset + 1] = (byte)(3 + diff % 253);
                buffer[offset] = -1;
                offset += 4;
            }
        }
        else {
            long division = getNegDivMod(diff, 253);
            int modulo = (int)division;
            if (diff >= -10668) {
                diff = (int)(division >> 32);
                buffer[offset++] = (byte)(49 + diff);
                buffer[offset++] = (byte)(3 + modulo);
            }
            else if (diff >= -192786) {
                buffer[offset + 2] = (byte)(3 + modulo);
                diff = (int)(division >> 32);
                division = getNegDivMod(diff, 253);
                modulo = (int)division;
                diff = (int)(division >> 32);
                buffer[offset + 1] = (byte)(3 + modulo);
                buffer[offset] = (byte)(7 + diff);
                offset += 3;
            }
            else {
                buffer[offset + 3] = (byte)(3 + modulo);
                diff = (int)(division >> 32);
                division = getNegDivMod(diff, 253);
                modulo = (int)division;
                diff = (int)(division >> 32);
                buffer[offset + 2] = (byte)(3 + modulo);
                division = getNegDivMod(diff, 253);
                modulo = (int)division;
                buffer[offset + 1] = (byte)(3 + modulo);
                buffer[offset] = 3;
                offset += 4;
            }
        }
        return offset;
    }
    
    private static final int lengthOfDiff(final int diff) {
        if (diff >= -80) {
            if (diff <= 80) {
                return 1;
            }
            if (diff <= 10667) {
                return 2;
            }
            if (diff <= 192785) {
                return 3;
            }
            return 4;
        }
        else {
            if (diff >= -10668) {
                return 2;
            }
            if (diff >= -192786) {
                return 3;
            }
            return 4;
        }
    }
}
