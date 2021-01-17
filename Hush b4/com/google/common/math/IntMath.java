// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.math;

import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtIncompatible;
import java.math.RoundingMode;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class IntMath
{
    @VisibleForTesting
    static final int MAX_POWER_OF_SQRT2_UNSIGNED = -1257966797;
    @VisibleForTesting
    static final byte[] maxLog10ForLeadingZeros;
    @VisibleForTesting
    static final int[] powersOf10;
    @VisibleForTesting
    static final int[] halfPowersOf10;
    @VisibleForTesting
    static final int FLOOR_SQRT_MAX_INT = 46340;
    private static final int[] factorials;
    @VisibleForTesting
    static int[] biggestBinomials;
    
    public static boolean isPowerOfTwo(final int x) {
        return x > 0 & (x & x - 1) == 0x0;
    }
    
    @VisibleForTesting
    static int lessThanBranchFree(final int x, final int y) {
        return ~(~(x - y)) >>> 31;
    }
    
    public static int log2(final int x, final RoundingMode mode) {
        MathPreconditions.checkPositive("x", x);
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
            }
            case DOWN:
            case FLOOR: {
                return 31 - Integer.numberOfLeadingZeros(x);
            }
            case UP:
            case CEILING: {
                return 32 - Integer.numberOfLeadingZeros(x - 1);
            }
            case HALF_DOWN:
            case HALF_UP:
            case HALF_EVEN: {
                final int leadingZeros = Integer.numberOfLeadingZeros(x);
                final int cmp = -1257966797 >>> leadingZeros;
                final int logFloor = 31 - leadingZeros;
                return logFloor + lessThanBranchFree(cmp, x);
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    @GwtIncompatible("need BigIntegerMath to adequately test")
    public static int log10(final int x, final RoundingMode mode) {
        MathPreconditions.checkPositive("x", x);
        final int logFloor = log10Floor(x);
        final int floorPow = IntMath.powersOf10[logFloor];
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(x == floorPow);
            }
            case DOWN:
            case FLOOR: {
                return logFloor;
            }
            case UP:
            case CEILING: {
                return logFloor + lessThanBranchFree(floorPow, x);
            }
            case HALF_DOWN:
            case HALF_UP:
            case HALF_EVEN: {
                return logFloor + lessThanBranchFree(IntMath.halfPowersOf10[logFloor], x);
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    private static int log10Floor(final int x) {
        final int y = IntMath.maxLog10ForLeadingZeros[Integer.numberOfLeadingZeros(x)];
        return y - lessThanBranchFree(x, IntMath.powersOf10[y]);
    }
    
    @GwtIncompatible("failing tests")
    public static int pow(int b, int k) {
        MathPreconditions.checkNonNegative("exponent", k);
        switch (b) {
            case 0: {
                return (k == 0) ? 1 : 0;
            }
            case 1: {
                return 1;
            }
            case -1: {
                return ((k & 0x1) == 0x0) ? 1 : -1;
            }
            case 2: {
                return (k < 32) ? (1 << k) : 0;
            }
            case -2: {
                if (k < 32) {
                    return ((k & 0x1) == 0x0) ? (1 << k) : (-(1 << k));
                }
                return 0;
            }
            default: {
                int accum = 1;
                while (true) {
                    switch (k) {
                        case 0: {
                            return accum;
                        }
                        case 1: {
                            return b * accum;
                        }
                        default: {
                            accum *= (((k & 0x1) == 0x0) ? 1 : b);
                            b *= b;
                            k >>= 1;
                            continue;
                        }
                    }
                }
                break;
            }
        }
    }
    
    @GwtIncompatible("need BigIntegerMath to adequately test")
    public static int sqrt(final int x, final RoundingMode mode) {
        MathPreconditions.checkNonNegative("x", x);
        final int sqrtFloor = sqrtFloor(x);
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(sqrtFloor * sqrtFloor == x);
            }
            case DOWN:
            case FLOOR: {
                return sqrtFloor;
            }
            case UP:
            case CEILING: {
                return sqrtFloor + lessThanBranchFree(sqrtFloor * sqrtFloor, x);
            }
            case HALF_DOWN:
            case HALF_UP:
            case HALF_EVEN: {
                final int halfSquare = sqrtFloor * sqrtFloor + sqrtFloor;
                return sqrtFloor + lessThanBranchFree(halfSquare, x);
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    private static int sqrtFloor(final int x) {
        return (int)Math.sqrt(x);
    }
    
    public static int divide(final int p, final int q, final RoundingMode mode) {
        Preconditions.checkNotNull(mode);
        if (q == 0) {
            throw new ArithmeticException("/ by zero");
        }
        final int div = p / q;
        final int rem = p - q * div;
        if (rem == 0) {
            return div;
        }
        final int signum = 0x1 | (p ^ q) >> 31;
        boolean increment = false;
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(rem == 0);
            }
            case DOWN: {
                increment = false;
                break;
            }
            case UP: {
                increment = true;
                break;
            }
            case CEILING: {
                increment = (signum > 0);
                break;
            }
            case FLOOR: {
                increment = (signum < 0);
                break;
            }
            case HALF_DOWN:
            case HALF_UP:
            case HALF_EVEN: {
                final int absRem = Math.abs(rem);
                final int cmpRemToHalfDivisor = absRem - (Math.abs(q) - absRem);
                if (cmpRemToHalfDivisor == 0) {
                    increment = (mode == RoundingMode.HALF_UP || (mode == RoundingMode.HALF_EVEN & (div & 0x1) != 0x0));
                    break;
                }
                increment = (cmpRemToHalfDivisor > 0);
                break;
            }
            default: {
                throw new AssertionError();
            }
        }
        return increment ? (div + signum) : div;
    }
    
    public static int mod(final int x, final int m) {
        if (m <= 0) {
            throw new ArithmeticException("Modulus " + m + " must be > 0");
        }
        final int result = x % m;
        return (result >= 0) ? result : (result + m);
    }
    
    public static int gcd(int a, int b) {
        MathPreconditions.checkNonNegative("a", a);
        MathPreconditions.checkNonNegative("b", b);
        if (a == 0) {
            return b;
        }
        if (b == 0) {
            return a;
        }
        final int aTwos = Integer.numberOfTrailingZeros(a);
        a >>= aTwos;
        final int bTwos = Integer.numberOfTrailingZeros(b);
        int delta;
        int minDeltaOrZero;
        for (b >>= bTwos; a != b; a = delta - minDeltaOrZero - minDeltaOrZero, b += minDeltaOrZero, a >>= Integer.numberOfTrailingZeros(a)) {
            delta = a - b;
            minDeltaOrZero = (delta & delta >> 31);
        }
        return a << Math.min(aTwos, bTwos);
    }
    
    public static int checkedAdd(final int a, final int b) {
        final long result = a + (long)b;
        MathPreconditions.checkNoOverflow(result == (int)result);
        return (int)result;
    }
    
    public static int checkedSubtract(final int a, final int b) {
        final long result = a - (long)b;
        MathPreconditions.checkNoOverflow(result == (int)result);
        return (int)result;
    }
    
    public static int checkedMultiply(final int a, final int b) {
        final long result = a * (long)b;
        MathPreconditions.checkNoOverflow(result == (int)result);
        return (int)result;
    }
    
    public static int checkedPow(int b, int k) {
        MathPreconditions.checkNonNegative("exponent", k);
        switch (b) {
            case 0: {
                return (k == 0) ? 1 : 0;
            }
            case 1: {
                return 1;
            }
            case -1: {
                return ((k & 0x1) == 0x0) ? 1 : -1;
            }
            case 2: {
                MathPreconditions.checkNoOverflow(k < 31);
                return 1 << k;
            }
            case -2: {
                MathPreconditions.checkNoOverflow(k < 32);
                return ((k & 0x1) == 0x0) ? (1 << k) : (-1 << k);
            }
            default: {
                int accum = 1;
                while (true) {
                    switch (k) {
                        case 0: {
                            return accum;
                        }
                        case 1: {
                            return checkedMultiply(accum, b);
                        }
                        default: {
                            if ((k & 0x1) != 0x0) {
                                accum = checkedMultiply(accum, b);
                            }
                            k >>= 1;
                            if (k > 0) {
                                MathPreconditions.checkNoOverflow(-46340 <= b & b <= 46340);
                                b *= b;
                                continue;
                            }
                            continue;
                        }
                    }
                }
                break;
            }
        }
    }
    
    public static int factorial(final int n) {
        MathPreconditions.checkNonNegative("n", n);
        return (n < IntMath.factorials.length) ? IntMath.factorials[n] : Integer.MAX_VALUE;
    }
    
    @GwtIncompatible("need BigIntegerMath to adequately test")
    public static int binomial(final int n, int k) {
        MathPreconditions.checkNonNegative("n", n);
        MathPreconditions.checkNonNegative("k", k);
        Preconditions.checkArgument(k <= n, "k (%s) > n (%s)", k, n);
        if (k > n >> 1) {
            k = n - k;
        }
        if (k >= IntMath.biggestBinomials.length || n > IntMath.biggestBinomials[k]) {
            return Integer.MAX_VALUE;
        }
        switch (k) {
            case 0: {
                return 1;
            }
            case 1: {
                return n;
            }
            default: {
                long result = 1L;
                for (int i = 0; i < k; ++i) {
                    result *= n - i;
                    result /= i + 1;
                }
                return (int)result;
            }
        }
    }
    
    public static int mean(final int x, final int y) {
        return (x & y) + ((x ^ y) >> 1);
    }
    
    private IntMath() {
    }
    
    static {
        maxLog10ForLeadingZeros = new byte[] { 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0, 0 };
        powersOf10 = new int[] { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000, 1000000000 };
        halfPowersOf10 = new int[] { 3, 31, 316, 3162, 31622, 316227, 3162277, 31622776, 316227766, Integer.MAX_VALUE };
        factorials = new int[] { 1, 1, 2, 6, 24, 120, 720, 5040, 40320, 362880, 3628800, 39916800, 479001600 };
        IntMath.biggestBinomials = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, 65536, 2345, 477, 193, 110, 75, 58, 49, 43, 39, 37, 35, 34, 34, 33 };
    }
}
