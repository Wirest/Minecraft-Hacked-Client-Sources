// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.math;

import java.util.Iterator;
import com.google.common.primitives.Booleans;
import com.google.common.base.Preconditions;
import java.math.BigInteger;
import com.google.common.annotations.GwtIncompatible;
import java.math.RoundingMode;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class DoubleMath
{
    private static final double MIN_INT_AS_DOUBLE = -2.147483648E9;
    private static final double MAX_INT_AS_DOUBLE = 2.147483647E9;
    private static final double MIN_LONG_AS_DOUBLE = -9.223372036854776E18;
    private static final double MAX_LONG_AS_DOUBLE_PLUS_ONE = 9.223372036854776E18;
    private static final double LN_2;
    @VisibleForTesting
    static final int MAX_FACTORIAL = 170;
    @VisibleForTesting
    static final double[] everySixteenthFactorial;
    
    @GwtIncompatible("#isMathematicalInteger, com.google.common.math.DoubleUtils")
    static double roundIntermediate(final double x, final RoundingMode mode) {
        if (!DoubleUtils.isFinite(x)) {
            throw new ArithmeticException("input is infinite or NaN");
        }
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(isMathematicalInteger(x));
                return x;
            }
            case FLOOR: {
                if (x >= 0.0 || isMathematicalInteger(x)) {
                    return x;
                }
                return x - 1.0;
            }
            case CEILING: {
                if (x <= 0.0 || isMathematicalInteger(x)) {
                    return x;
                }
                return x + 1.0;
            }
            case DOWN: {
                return x;
            }
            case UP: {
                if (isMathematicalInteger(x)) {
                    return x;
                }
                return x + Math.copySign(1.0, x);
            }
            case HALF_EVEN: {
                return Math.rint(x);
            }
            case HALF_UP: {
                final double z = Math.rint(x);
                if (Math.abs(x - z) == 0.5) {
                    return x + Math.copySign(0.5, x);
                }
                return z;
            }
            case HALF_DOWN: {
                final double z = Math.rint(x);
                if (Math.abs(x - z) == 0.5) {
                    return x;
                }
                return z;
            }
            default: {
                throw new AssertionError();
            }
        }
    }
    
    @GwtIncompatible("#roundIntermediate")
    public static int roundToInt(final double x, final RoundingMode mode) {
        final double z = roundIntermediate(x, mode);
        MathPreconditions.checkInRange(z > -2.147483649E9 & z < 2.147483648E9);
        return (int)z;
    }
    
    @GwtIncompatible("#roundIntermediate")
    public static long roundToLong(final double x, final RoundingMode mode) {
        final double z = roundIntermediate(x, mode);
        MathPreconditions.checkInRange(-9.223372036854776E18 - z < 1.0 & z < 9.223372036854776E18);
        return (long)z;
    }
    
    @GwtIncompatible("#roundIntermediate, java.lang.Math.getExponent, com.google.common.math.DoubleUtils")
    public static BigInteger roundToBigInteger(double x, final RoundingMode mode) {
        x = roundIntermediate(x, mode);
        if (-9.223372036854776E18 - x < 1.0 & x < 9.223372036854776E18) {
            return BigInteger.valueOf((long)x);
        }
        final int exponent = Math.getExponent(x);
        final long significand = DoubleUtils.getSignificand(x);
        final BigInteger result = BigInteger.valueOf(significand).shiftLeft(exponent - 52);
        return (x < 0.0) ? result.negate() : result;
    }
    
    @GwtIncompatible("com.google.common.math.DoubleUtils")
    public static boolean isPowerOfTwo(final double x) {
        return x > 0.0 && DoubleUtils.isFinite(x) && LongMath.isPowerOfTwo(DoubleUtils.getSignificand(x));
    }
    
    public static double log2(final double x) {
        return Math.log(x) / DoubleMath.LN_2;
    }
    
    @GwtIncompatible("java.lang.Math.getExponent, com.google.common.math.DoubleUtils")
    public static int log2(final double x, final RoundingMode mode) {
        Preconditions.checkArgument(x > 0.0 && DoubleUtils.isFinite(x), (Object)"x must be positive and finite");
        final int exponent = Math.getExponent(x);
        if (!DoubleUtils.isNormal(x)) {
            return log2(x * 4.503599627370496E15, mode) - 52;
        }
        boolean increment = false;
        switch (mode) {
            case UNNECESSARY: {
                MathPreconditions.checkRoundingUnnecessary(isPowerOfTwo(x));
            }
            case FLOOR: {
                increment = false;
                break;
            }
            case CEILING: {
                increment = !isPowerOfTwo(x);
                break;
            }
            case DOWN: {
                increment = (exponent < 0 & !isPowerOfTwo(x));
                break;
            }
            case UP: {
                increment = (exponent >= 0 & !isPowerOfTwo(x));
                break;
            }
            case HALF_EVEN:
            case HALF_UP:
            case HALF_DOWN: {
                final double xScaled = DoubleUtils.scaleNormalize(x);
                increment = (xScaled * xScaled > 2.0);
                break;
            }
            default: {
                throw new AssertionError();
            }
        }
        return increment ? (exponent + 1) : exponent;
    }
    
    @GwtIncompatible("java.lang.Math.getExponent, com.google.common.math.DoubleUtils")
    public static boolean isMathematicalInteger(final double x) {
        return DoubleUtils.isFinite(x) && (x == 0.0 || 52 - Long.numberOfTrailingZeros(DoubleUtils.getSignificand(x)) <= Math.getExponent(x));
    }
    
    public static double factorial(final int n) {
        MathPreconditions.checkNonNegative("n", n);
        if (n > 170) {
            return Double.POSITIVE_INFINITY;
        }
        double accum = 1.0;
        for (int i = 1 + (n & 0xFFFFFFF0); i <= n; ++i) {
            accum *= i;
        }
        return accum * DoubleMath.everySixteenthFactorial[n >> 4];
    }
    
    public static boolean fuzzyEquals(final double a, final double b, final double tolerance) {
        MathPreconditions.checkNonNegative("tolerance", tolerance);
        return Math.copySign(a - b, 1.0) <= tolerance || a == b || (Double.isNaN(a) && Double.isNaN(b));
    }
    
    public static int fuzzyCompare(final double a, final double b, final double tolerance) {
        if (fuzzyEquals(a, b, tolerance)) {
            return 0;
        }
        if (a < b) {
            return -1;
        }
        if (a > b) {
            return 1;
        }
        return Booleans.compare(Double.isNaN(a), Double.isNaN(b));
    }
    
    @GwtIncompatible("MeanAccumulator")
    public static double mean(final double... values) {
        final MeanAccumulator accumulator = new MeanAccumulator();
        for (final double value : values) {
            accumulator.add(value);
        }
        return accumulator.mean();
    }
    
    @GwtIncompatible("MeanAccumulator")
    public static double mean(final int... values) {
        final MeanAccumulator accumulator = new MeanAccumulator();
        for (final int value : values) {
            accumulator.add(value);
        }
        return accumulator.mean();
    }
    
    @GwtIncompatible("MeanAccumulator")
    public static double mean(final long... values) {
        final MeanAccumulator accumulator = new MeanAccumulator();
        for (final long value : values) {
            accumulator.add((double)value);
        }
        return accumulator.mean();
    }
    
    @GwtIncompatible("MeanAccumulator")
    public static double mean(final Iterable<? extends Number> values) {
        final MeanAccumulator accumulator = new MeanAccumulator();
        for (final Number value : values) {
            accumulator.add(value.doubleValue());
        }
        return accumulator.mean();
    }
    
    @GwtIncompatible("MeanAccumulator")
    public static double mean(final Iterator<? extends Number> values) {
        final MeanAccumulator accumulator = new MeanAccumulator();
        while (values.hasNext()) {
            accumulator.add(((Number)values.next()).doubleValue());
        }
        return accumulator.mean();
    }
    
    private DoubleMath() {
    }
    
    static {
        LN_2 = Math.log(2.0);
        everySixteenthFactorial = new double[] { 1.0, 2.0922789888E13, 2.631308369336935E35, 1.2413915592536073E61, 1.2688693218588417E89, 7.156945704626381E118, 9.916779348709496E149, 1.974506857221074E182, 3.856204823625804E215, 5.5502938327393044E249, 4.7147236359920616E284 };
    }
    
    @GwtIncompatible("com.google.common.math.DoubleUtils")
    private static final class MeanAccumulator
    {
        private long count;
        private double mean;
        
        private MeanAccumulator() {
            this.count = 0L;
            this.mean = 0.0;
        }
        
        void add(final double value) {
            Preconditions.checkArgument(DoubleUtils.isFinite(value));
            ++this.count;
            this.mean += (value - this.mean) / this.count;
        }
        
        double mean() {
            Preconditions.checkArgument(this.count > 0L, (Object)"Cannot take mean of 0 values");
            return this.mean;
        }
    }
}
