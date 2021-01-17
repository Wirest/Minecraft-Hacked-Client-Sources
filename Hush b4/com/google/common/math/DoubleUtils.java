// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.math;

import java.math.BigInteger;
import com.google.common.base.Preconditions;

final class DoubleUtils
{
    static final long SIGNIFICAND_MASK = 4503599627370495L;
    static final long EXPONENT_MASK = 9218868437227405312L;
    static final long SIGN_MASK = Long.MIN_VALUE;
    static final int SIGNIFICAND_BITS = 52;
    static final int EXPONENT_BIAS = 1023;
    static final long IMPLICIT_BIT = 4503599627370496L;
    private static final long ONE_BITS;
    
    private DoubleUtils() {
    }
    
    static double nextDown(final double d) {
        return -Math.nextUp(-d);
    }
    
    static long getSignificand(final double d) {
        Preconditions.checkArgument(isFinite(d), (Object)"not a normal value");
        final int exponent = Math.getExponent(d);
        long bits = Double.doubleToRawLongBits(d);
        bits &= 0xFFFFFFFFFFFFFL;
        return (exponent == -1023) ? (bits << 1) : (bits | 0x10000000000000L);
    }
    
    static boolean isFinite(final double d) {
        return Math.getExponent(d) <= 1023;
    }
    
    static boolean isNormal(final double d) {
        return Math.getExponent(d) >= -1022;
    }
    
    static double scaleNormalize(final double x) {
        final long significand = Double.doubleToRawLongBits(x) & 0xFFFFFFFFFFFFFL;
        return Double.longBitsToDouble(significand | DoubleUtils.ONE_BITS);
    }
    
    static double bigToDouble(final BigInteger x) {
        final BigInteger absX = x.abs();
        final int exponent = absX.bitLength() - 1;
        if (exponent < 63) {
            return (double)x.longValue();
        }
        if (exponent > 1023) {
            return x.signum() * Double.POSITIVE_INFINITY;
        }
        final int shift = exponent - 52 - 1;
        final long twiceSignifFloor = absX.shiftRight(shift).longValue();
        long signifFloor = twiceSignifFloor >> 1;
        signifFloor &= 0xFFFFFFFFFFFFFL;
        final boolean increment = (twiceSignifFloor & 0x1L) != 0x0L && ((signifFloor & 0x1L) != 0x0L || absX.getLowestSetBit() < shift);
        final long signifRounded = increment ? (signifFloor + 1L) : signifFloor;
        long bits = (long)(exponent + 1023) << 52;
        bits += signifRounded;
        bits |= ((long)x.signum() & Long.MIN_VALUE);
        return Double.longBitsToDouble(bits);
    }
    
    static double ensureNonNegative(final double value) {
        Preconditions.checkArgument(!Double.isNaN(value));
        if (value > 0.0) {
            return value;
        }
        return 0.0;
    }
    
    static {
        ONE_BITS = Double.doubleToRawLongBits(1.0);
    }
}
