// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.math;

import java.math.BigInteger;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class MathPreconditions
{
    static int checkPositive(@Nullable final String role, final int x) {
        if (x <= 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
        }
        return x;
    }
    
    static long checkPositive(@Nullable final String role, final long x) {
        if (x <= 0L) {
            throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
        }
        return x;
    }
    
    static BigInteger checkPositive(@Nullable final String role, final BigInteger x) {
        if (x.signum() <= 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be > 0");
        }
        return x;
    }
    
    static int checkNonNegative(@Nullable final String role, final int x) {
        if (x < 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }
    
    static long checkNonNegative(@Nullable final String role, final long x) {
        if (x < 0L) {
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }
    
    static BigInteger checkNonNegative(@Nullable final String role, final BigInteger x) {
        if (x.signum() < 0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }
    
    static double checkNonNegative(@Nullable final String role, final double x) {
        if (x < 0.0) {
            throw new IllegalArgumentException(role + " (" + x + ") must be >= 0");
        }
        return x;
    }
    
    static void checkRoundingUnnecessary(final boolean condition) {
        if (!condition) {
            throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
        }
    }
    
    static void checkInRange(final boolean condition) {
        if (!condition) {
            throw new ArithmeticException("not in range");
        }
    }
    
    static void checkNoOverflow(final boolean condition) {
        if (!condition) {
            throw new ArithmeticException("overflow");
        }
    }
    
    private MathPreconditions() {
    }
}
