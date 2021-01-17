// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.primitives;

import javax.annotation.Nullable;
import javax.annotation.CheckReturnValue;
import java.math.BigInteger;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;

@GwtCompatible(serializable = true)
public final class UnsignedLong extends Number implements Comparable<UnsignedLong>, Serializable
{
    private static final long UNSIGNED_MASK = Long.MAX_VALUE;
    public static final UnsignedLong ZERO;
    public static final UnsignedLong ONE;
    public static final UnsignedLong MAX_VALUE;
    private final long value;
    
    private UnsignedLong(final long value) {
        this.value = value;
    }
    
    public static UnsignedLong fromLongBits(final long bits) {
        return new UnsignedLong(bits);
    }
    
    public static UnsignedLong valueOf(final long value) {
        Preconditions.checkArgument(value >= 0L, "value (%s) is outside the range for an unsigned long value", value);
        return fromLongBits(value);
    }
    
    public static UnsignedLong valueOf(final BigInteger value) {
        Preconditions.checkNotNull(value);
        Preconditions.checkArgument(value.signum() >= 0 && value.bitLength() <= 64, "value (%s) is outside the range for an unsigned long value", value);
        return fromLongBits(value.longValue());
    }
    
    public static UnsignedLong valueOf(final String string) {
        return valueOf(string, 10);
    }
    
    public static UnsignedLong valueOf(final String string, final int radix) {
        return fromLongBits(UnsignedLongs.parseUnsignedLong(string, radix));
    }
    
    public UnsignedLong plus(final UnsignedLong val) {
        return fromLongBits(this.value + Preconditions.checkNotNull(val).value);
    }
    
    public UnsignedLong minus(final UnsignedLong val) {
        return fromLongBits(this.value - Preconditions.checkNotNull(val).value);
    }
    
    @CheckReturnValue
    public UnsignedLong times(final UnsignedLong val) {
        return fromLongBits(this.value * Preconditions.checkNotNull(val).value);
    }
    
    @CheckReturnValue
    public UnsignedLong dividedBy(final UnsignedLong val) {
        return fromLongBits(UnsignedLongs.divide(this.value, Preconditions.checkNotNull(val).value));
    }
    
    @CheckReturnValue
    public UnsignedLong mod(final UnsignedLong val) {
        return fromLongBits(UnsignedLongs.remainder(this.value, Preconditions.checkNotNull(val).value));
    }
    
    @Override
    public int intValue() {
        return (int)this.value;
    }
    
    @Override
    public long longValue() {
        return this.value;
    }
    
    @Override
    public float floatValue() {
        float fValue = (float)(this.value & Long.MAX_VALUE);
        if (this.value < 0L) {
            fValue += 9.223372E18f;
        }
        return fValue;
    }
    
    @Override
    public double doubleValue() {
        double dValue = (double)(this.value & Long.MAX_VALUE);
        if (this.value < 0L) {
            dValue += 9.223372036854776E18;
        }
        return dValue;
    }
    
    public BigInteger bigIntegerValue() {
        BigInteger bigInt = BigInteger.valueOf(this.value & Long.MAX_VALUE);
        if (this.value < 0L) {
            bigInt = bigInt.setBit(63);
        }
        return bigInt;
    }
    
    @Override
    public int compareTo(final UnsignedLong o) {
        Preconditions.checkNotNull(o);
        return UnsignedLongs.compare(this.value, o.value);
    }
    
    @Override
    public int hashCode() {
        return Longs.hashCode(this.value);
    }
    
    @Override
    public boolean equals(@Nullable final Object obj) {
        if (obj instanceof UnsignedLong) {
            final UnsignedLong other = (UnsignedLong)obj;
            return this.value == other.value;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return UnsignedLongs.toString(this.value);
    }
    
    public String toString(final int radix) {
        return UnsignedLongs.toString(this.value, radix);
    }
    
    static {
        ZERO = new UnsignedLong(0L);
        ONE = new UnsignedLong(1L);
        MAX_VALUE = new UnsignedLong(-1L);
    }
}
