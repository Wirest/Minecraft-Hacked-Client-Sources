// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl.duration;

public final class Period
{
    final byte timeLimit;
    final boolean inFuture;
    final int[] counts;
    
    public static Period at(final float count, final TimeUnit unit) {
        checkCount(count);
        return new Period(0, false, count, unit);
    }
    
    public static Period moreThan(final float count, final TimeUnit unit) {
        checkCount(count);
        return new Period(2, false, count, unit);
    }
    
    public static Period lessThan(final float count, final TimeUnit unit) {
        checkCount(count);
        return new Period(1, false, count, unit);
    }
    
    public Period and(final float count, final TimeUnit unit) {
        checkCount(count);
        return this.setTimeUnitValue(unit, count);
    }
    
    public Period omit(final TimeUnit unit) {
        return this.setTimeUnitInternalValue(unit, 0);
    }
    
    public Period at() {
        return this.setTimeLimit((byte)0);
    }
    
    public Period moreThan() {
        return this.setTimeLimit((byte)2);
    }
    
    public Period lessThan() {
        return this.setTimeLimit((byte)1);
    }
    
    public Period inFuture() {
        return this.setFuture(true);
    }
    
    public Period inPast() {
        return this.setFuture(false);
    }
    
    public Period inFuture(final boolean future) {
        return this.setFuture(future);
    }
    
    public Period inPast(final boolean past) {
        return this.setFuture(!past);
    }
    
    public boolean isSet() {
        for (int i = 0; i < this.counts.length; ++i) {
            if (this.counts[i] != 0) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isSet(final TimeUnit unit) {
        return this.counts[unit.ordinal] > 0;
    }
    
    public float getCount(final TimeUnit unit) {
        final int ord = unit.ordinal;
        if (this.counts[ord] == 0) {
            return 0.0f;
        }
        return (this.counts[ord] - 1) / 1000.0f;
    }
    
    public boolean isInFuture() {
        return this.inFuture;
    }
    
    public boolean isInPast() {
        return !this.inFuture;
    }
    
    public boolean isMoreThan() {
        return this.timeLimit == 2;
    }
    
    public boolean isLessThan() {
        return this.timeLimit == 1;
    }
    
    @Override
    public boolean equals(final Object rhs) {
        try {
            return this.equals((Period)rhs);
        }
        catch (ClassCastException e) {
            return false;
        }
    }
    
    public boolean equals(final Period rhs) {
        if (rhs != null && this.timeLimit == rhs.timeLimit && this.inFuture == rhs.inFuture) {
            for (int i = 0; i < this.counts.length; ++i) {
                if (this.counts[i] != rhs.counts[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int hc = this.timeLimit << 1 | (this.inFuture ? 1 : 0);
        for (int i = 0; i < this.counts.length; ++i) {
            hc = (hc << 2 ^ this.counts[i]);
        }
        return hc;
    }
    
    private Period(final int limit, final boolean future, final float count, final TimeUnit unit) {
        this.timeLimit = (byte)limit;
        this.inFuture = future;
        (this.counts = new int[TimeUnit.units.length])[unit.ordinal] = (int)(count * 1000.0f) + 1;
    }
    
    Period(final int timeLimit, final boolean inFuture, final int[] counts) {
        this.timeLimit = (byte)timeLimit;
        this.inFuture = inFuture;
        this.counts = counts;
    }
    
    private Period setTimeUnitValue(final TimeUnit unit, final float value) {
        if (value < 0.0f) {
            throw new IllegalArgumentException("value: " + value);
        }
        return this.setTimeUnitInternalValue(unit, (int)(value * 1000.0f) + 1);
    }
    
    private Period setTimeUnitInternalValue(final TimeUnit unit, final int value) {
        final int ord = unit.ordinal;
        if (this.counts[ord] != value) {
            final int[] newCounts = new int[this.counts.length];
            for (int i = 0; i < this.counts.length; ++i) {
                newCounts[i] = this.counts[i];
            }
            newCounts[ord] = value;
            return new Period(this.timeLimit, this.inFuture, newCounts);
        }
        return this;
    }
    
    private Period setFuture(final boolean future) {
        if (this.inFuture != future) {
            return new Period(this.timeLimit, future, this.counts);
        }
        return this;
    }
    
    private Period setTimeLimit(final byte limit) {
        if (this.timeLimit != limit) {
            return new Period(limit, this.inFuture, this.counts);
        }
        return this;
    }
    
    private static void checkCount(final float count) {
        if (count < 0.0f) {
            throw new IllegalArgumentException("count (" + count + ") cannot be negative");
        }
    }
}
