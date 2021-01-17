// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.atomic.AtomicLongArray;
import java.io.Serializable;

public class AtomicDoubleArray implements Serializable
{
    private static final long serialVersionUID = 0L;
    private transient AtomicLongArray longs;
    
    public AtomicDoubleArray(final int length) {
        this.longs = new AtomicLongArray(length);
    }
    
    public AtomicDoubleArray(final double[] array) {
        final int len = array.length;
        final long[] longArray = new long[len];
        for (int i = 0; i < len; ++i) {
            longArray[i] = Double.doubleToRawLongBits(array[i]);
        }
        this.longs = new AtomicLongArray(longArray);
    }
    
    public final int length() {
        return this.longs.length();
    }
    
    public final double get(final int i) {
        return Double.longBitsToDouble(this.longs.get(i));
    }
    
    public final void set(final int i, final double newValue) {
        final long next = Double.doubleToRawLongBits(newValue);
        this.longs.set(i, next);
    }
    
    public final void lazySet(final int i, final double newValue) {
        this.set(i, newValue);
    }
    
    public final double getAndSet(final int i, final double newValue) {
        final long next = Double.doubleToRawLongBits(newValue);
        return Double.longBitsToDouble(this.longs.getAndSet(i, next));
    }
    
    public final boolean compareAndSet(final int i, final double expect, final double update) {
        return this.longs.compareAndSet(i, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
    }
    
    public final boolean weakCompareAndSet(final int i, final double expect, final double update) {
        return this.longs.weakCompareAndSet(i, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
    }
    
    public final double getAndAdd(final int i, final double delta) {
        long current;
        double currentVal;
        long next;
        do {
            current = this.longs.get(i);
            currentVal = Double.longBitsToDouble(current);
            final double nextVal = currentVal + delta;
            next = Double.doubleToRawLongBits(nextVal);
        } while (!this.longs.compareAndSet(i, current, next));
        return currentVal;
    }
    
    public double addAndGet(final int i, final double delta) {
        long current;
        double nextVal;
        long next;
        do {
            current = this.longs.get(i);
            final double currentVal = Double.longBitsToDouble(current);
            nextVal = currentVal + delta;
            next = Double.doubleToRawLongBits(nextVal);
        } while (!this.longs.compareAndSet(i, current, next));
        return nextVal;
    }
    
    @Override
    public String toString() {
        final int iMax = this.length() - 1;
        if (iMax == -1) {
            return "[]";
        }
        final StringBuilder b = new StringBuilder(19 * (iMax + 1));
        b.append('[');
        int i = 0;
        while (true) {
            b.append(Double.longBitsToDouble(this.longs.get(i)));
            if (i == iMax) {
                break;
            }
            b.append(',').append(' ');
            ++i;
        }
        return b.append(']').toString();
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        final int length = this.length();
        s.writeInt(length);
        for (int i = 0; i < length; ++i) {
            s.writeDouble(this.get(i));
        }
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        final int length = s.readInt();
        this.longs = new AtomicLongArray(length);
        for (int i = 0; i < length; ++i) {
            this.set(i, s.readDouble());
        }
    }
}
