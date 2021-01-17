// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.math;

import com.google.common.base.Preconditions;
import java.math.RoundingMode;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class LongMath
{
    @VisibleForTesting
    static final long MAX_POWER_OF_SQRT2_UNSIGNED = -5402926248376769404L;
    @VisibleForTesting
    static final byte[] maxLog10ForLeadingZeros;
    @GwtIncompatible("TODO")
    @VisibleForTesting
    static final long[] powersOf10;
    @GwtIncompatible("TODO")
    @VisibleForTesting
    static final long[] halfPowersOf10;
    @VisibleForTesting
    static final long FLOOR_SQRT_MAX_LONG = 3037000499L;
    static final long[] factorials;
    static final int[] biggestBinomials;
    @VisibleForTesting
    static final int[] biggestSimpleBinomials;
    
    public static boolean isPowerOfTwo(final long x) {
        return x > 0L & (x & x - 1L) == 0x0L;
    }
    
    @VisibleForTesting
    static int lessThanBranchFree(final long x, final long y) {
        return (int)(~(~(x - y)) >>> 63);
    }
    
    public static int log2(final long x, final RoundingMode mode) {
        MathPreconditions.checkPositive("x", x);
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
            }
            case DOWN:
            case FLOOR: {
                return 63 - Long.numberOfLeadingZeros(x);
            }
            case UP:
            case CEILING: {
                return 64 - Long.numberOfLeadingZeros(x - 1L);
            }
            case HALF_DOWN:
            case HALF_UP:
            case HALF_EVEN: {
                final int leadingZeros = Long.numberOfLeadingZeros(x);
                final long cmp = -5402926248376769404L >>> leadingZeros;
                final int logFloor = 63 - leadingZeros;
                return logFloor + lessThanBranchFree(cmp, x);
            }
            default: {
                throw new AssertionError((Object)"impossible");
            }
        }
    }
    
    @GwtIncompatible("TODO")
    public static int log10(final long x, final RoundingMode mode) {
        MathPreconditions.checkPositive("x", x);
        final int logFloor = log10Floor(x);
        final long floorPow = LongMath.powersOf10[logFloor];
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
                return logFloor + lessThanBranchFree(LongMath.halfPowersOf10[logFloor], x);
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    @GwtIncompatible("TODO")
    static int log10Floor(final long x) {
        final int y = LongMath.maxLog10ForLeadingZeros[Long.numberOfLeadingZeros(x)];
        return y - lessThanBranchFree(x, LongMath.powersOf10[y]);
    }
    
    @GwtIncompatible("TODO")
    public static long pow(long b, int k) {
        MathPreconditions.checkNonNegative("exponent", k);
        if (-2L <= b && b <= 2L) {
            switch ((int)b) {
                case 0: {
                    return (k == 0) ? 1 : 0;
                }
                case 1: {
                    return 1L;
                }
                case -1: {
                    return ((k & 0x1) == 0x0) ? 1L : -1L;
                }
                case 2: {
                    return (k < 64) ? (1L << k) : 0L;
                }
                case -2: {
                    if (k < 64) {
                        return ((k & 0x1) == 0x0) ? (1L << k) : (-(1L << k));
                    }
                    return 0L;
                }
                default: {
                    throw new AssertionError();
                }
            }
        }
        else {
            long accum = 1L;
            while (true) {
                switch (k) {
                    case 0: {
                        return accum;
                    }
                    case 1: {
                        return accum * b;
                    }
                    default: {
                        accum *= (((k & 0x1) == 0x0) ? 1L : b);
                        b *= b;
                        k >>= 1;
                        continue;
                    }
                }
            }
        }
    }
    
    @GwtIncompatible("TODO")
    public static long sqrt(final long x, final RoundingMode mode) {
        MathPreconditions.checkNonNegative("x", x);
        if (fitsInInt(x)) {
            return IntMath.sqrt((int)x, mode);
        }
        final long guess = (long)Math.sqrt((double)x);
        final long guessSquared = guess * guess;
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(guessSquared == x);
                return guess;
            }
            case DOWN:
            case FLOOR: {
                if (x < guessSquared) {
                    return guess - 1L;
                }
                return guess;
            }
            case UP:
            case CEILING: {
                if (x > guessSquared) {
                    return guess + 1L;
                }
                return guess;
            }
            case HALF_DOWN:
            case HALF_UP:
            case HALF_EVEN: {
                final long sqrtFloor = guess - ((x < guessSquared) ? 1 : 0);
                final long halfSquare = sqrtFloor * sqrtFloor + sqrtFloor;
                return sqrtFloor + lessThanBranchFree(halfSquare, x);
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    @GwtIncompatible("TODO")
    public static long divide(final long p, final long q, final RoundingMode mode) {
        Preconditions.checkNotNull(mode);
        final long div = p / q;
        final long rem = p - q * div;
        if (rem == 0L) {
            return div;
        }
        final int signum = 0x1 | (int)((p ^ q) >> 63);
        boolean increment = false;
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(rem == 0L);
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
                final long absRem = Math.abs(rem);
                final long cmpRemToHalfDivisor = absRem - (Math.abs(q) - absRem);
                if (cmpRemToHalfDivisor == 0L) {
                    increment = (mode == RoundingMode.HALF_UP | (mode == RoundingMode.HALF_EVEN & (div & 0x1L) != 0x0L));
                    break;
                }
                increment = (cmpRemToHalfDivisor > 0L);
                break;
            }
            default: {
                throw new AssertionError();
            }
        }
        return increment ? (div + signum) : div;
    }
    
    @GwtIncompatible("TODO")
    public static int mod(final long x, final int m) {
        return (int)mod(x, (long)m);
    }
    
    @GwtIncompatible("TODO")
    public static long mod(final long x, final long m) {
        if (m <= 0L) {
            throw new ArithmeticException("Modulus must be positive");
        }
        final long result = x % m;
        return (result >= 0L) ? result : (result + m);
    }
    
    public static long gcd(long a, long b) {
        MathPreconditions.checkNonNegative("a", a);
        MathPreconditions.checkNonNegative("b", b);
        if (a == 0L) {
            return b;
        }
        if (b == 0L) {
            return a;
        }
        final int aTwos = Long.numberOfTrailingZeros(a);
        a >>= aTwos;
        final int bTwos = Long.numberOfTrailingZeros(b);
        long delta;
        long minDeltaOrZero;
        for (b >>= bTwos; a != b; a = delta - minDeltaOrZero - minDeltaOrZero, b += minDeltaOrZero, a >>= Long.numberOfTrailingZeros(a)) {
            delta = a - b;
            minDeltaOrZero = (delta & delta >> 63);
        }
        return a << Math.min(aTwos, bTwos);
    }
    
    @GwtIncompatible("TODO")
    public static long checkedAdd(final long a, final long b) {
        final long result = a + b;
        MathPreconditions.checkNoOverflow((a ^ b) < 0L | (a ^ result) >= 0L);
        return result;
    }
    
    @GwtIncompatible("TODO")
    public static long checkedSubtract(final long a, final long b) {
        final long result = a - b;
        MathPreconditions.checkNoOverflow((a ^ b) >= 0L | (a ^ result) >= 0L);
        return result;
    }
    
    @GwtIncompatible("TODO")
    public static long checkedMultiply(final long a, final long b) {
        final int leadingZeros = Long.numberOfLeadingZeros(a) + Long.numberOfLeadingZeros(~a) + Long.numberOfLeadingZeros(b) + Long.numberOfLeadingZeros(~b);
        if (leadingZeros > 65) {
            return a * b;
        }
        MathPreconditions.checkNoOverflow(leadingZeros >= 64);
        MathPreconditions.checkNoOverflow(a >= 0L | b != Long.MIN_VALUE);
        final long result = a * b;
        MathPreconditions.checkNoOverflow(a == 0L || result / a == b);
        return result;
    }
    
    @GwtIncompatible("TODO")
    public static long checkedPow(long b, int k) {
        MathPreconditions.checkNonNegative("exponent", k);
        if (b >= -2L & b <= 2L) {
            switch ((int)b) {
                case 0: {
                    return (k == 0) ? 1 : 0;
                }
                case 1: {
                    return 1L;
                }
                case -1: {
                    return ((k & 0x1) == 0x0) ? 1L : -1L;
                }
                case 2: {
                    MathPreconditions.checkNoOverflow(k < 63);
                    return 1L << k;
                }
                case -2: {
                    MathPreconditions.checkNoOverflow(k < 64);
                    return ((k & 0x1) == 0x0) ? (1L << k) : (-1L << k);
                }
                default: {
                    throw new AssertionError();
                }
            }
        }
        else {
            long accum = 1L;
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
                            MathPreconditions.checkNoOverflow(b <= 3037000499L);
                            b *= b;
                            continue;
                        }
                        continue;
                    }
                }
            }
        }
    }
    
    @GwtIncompatible("TODO")
    public static long factorial(final int n) {
        MathPreconditions.checkNonNegative("n", n);
        return (n < LongMath.factorials.length) ? LongMath.factorials[n] : Long.MAX_VALUE;
    }
    
    public static long binomial(int n, int k) {
        MathPreconditions.checkNonNegative("n", n);
        MathPreconditions.checkNonNegative("k", k);
        Preconditions.checkArgument(k <= n, "k (%s) > n (%s)", k, n);
        if (k > n >> 1) {
            k = n - k;
        }
        switch (k) {
            case 0: {
                return 1L;
            }
            case 1: {
                return n;
            }
            default: {
                if (n < LongMath.factorials.length) {
                    return LongMath.factorials[n] / (LongMath.factorials[k] * LongMath.factorials[n - k]);
                }
                if (k >= LongMath.biggestBinomials.length || n > LongMath.biggestBinomials[k]) {
                    return Long.MAX_VALUE;
                }
                if (k < LongMath.biggestSimpleBinomials.length && n <= LongMath.biggestSimpleBinomials[k]) {
                    long result = n--;
                    for (int i = 2; i <= k; ++i) {
                        result *= n;
                        result /= i;
                        --n;
                    }
                    return result;
                }
                final int nBits = log2(n, RoundingMode.CEILING);
                long result2 = 1L;
                long numerator = n--;
                long denominator = 1L;
                int numeratorBits = nBits;
                for (int j = 2; j <= k; ++j, --n) {
                    if (numeratorBits + nBits < 63) {
                        numerator *= n;
                        denominator *= j;
                        numeratorBits += nBits;
                    }
                    else {
                        result2 = multiplyFraction(result2, numerator, denominator);
                        numerator = n;
                        denominator = j;
                        numeratorBits = nBits;
                    }
                }
                return multiplyFraction(result2, numerator, denominator);
            }
        }
    }
    
    static long multiplyFraction(long x, final long numerator, long denominator) {
        if (x == 1L) {
            return numerator / denominator;
        }
        final long commonDivisor = gcd(x, denominator);
        x /= commonDivisor;
        denominator /= commonDivisor;
        return x * (numerator / denominator);
    }
    
    static boolean fitsInInt(final long x) {
        return (int)x == x;
    }
    
    public static long mean(final long x, final long y) {
        return (x & y) + ((x ^ y) >> 1);
    }
    
    private LongMath() {
    }
    
    static {
        maxLog10ForLeadingZeros = new byte[] { 19, 18, 18, 18, 18, 17, 17, 17, 16, 16, 16, 15, 15, 15, 15, 14, 14, 14, 13, 13, 13, 12, 12, 12, 12, 11, 11, 11, 10, 10, 10, 9, 9, 9, 9, 8, 8, 8, 7, 7, 7, 6, 6, 6, 6, 5, 5, 5, 4, 4, 4, 3, 3, 3, 3, 2, 2, 2, 1, 1, 1, 0, 0, 0 };
        powersOf10 = new long[] { 1L, 10L, 100L, 1000L, 10000L, 100000L, 1000000L, 10000000L, 100000000L, 1000000000L, 10000000000L, 100000000000L, 1000000000000L, 10000000000000L, 100000000000000L, 1000000000000000L, 10000000000000000L, 100000000000000000L, 1000000000000000000L };
        halfPowersOf10 = new long[] { 3L, 31L, 316L, 3162L, 31622L, 316227L, 3162277L, 31622776L, 316227766L, 3162277660L, 31622776601L, 316227766016L, 3162277660168L, 31622776601683L, 316227766016837L, 3162277660168379L, 31622776601683793L, 316227766016837933L, 3162277660168379331L };
        factorials = new long[] { 1L, 1L, 2L, 6L, 24L, 120L, 720L, 5040L, 40320L, 362880L, 3628800L, 39916800L, 479001600L, 6227020800L, 87178291200L, 1307674368000L, 20922789888000L, 355687428096000L, 6402373705728000L, 121645100408832000L, 2432902008176640000L };
        biggestBinomials = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 3810779, 121977, 16175, 4337, 1733, 887, 534, 361, 265, 206, 169, 143, 125, 111, 101, 94, 88, 83, 79, 76, 74, 72, 70, 69, 68, 67, 67, 66, 66, 66, 66 };
        biggestSimpleBinomials = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 2642246, 86251, 11724, 3218, 1313, 684, 419, 287, 214, 169, 139, 119, 105, 95, 87, 81, 76, 73, 70, 68, 66, 64, 63, 62, 62, 61, 61, 61 };
    }
}
