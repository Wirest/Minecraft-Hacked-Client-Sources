// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.io.Serializable;

public class AtomicDouble extends Number implements Serializable
{
    private static final long serialVersionUID = 0L;
    private transient volatile long value;
    private static final AtomicLongFieldUpdater<AtomicDouble> updater;
    
    public AtomicDouble(final double initialValue) {
        this.value = Double.doubleToRawLongBits(initialValue);
    }
    
    public AtomicDouble() {
    }
    
    public final double get() {
        return Double.longBitsToDouble(this.value);
    }
    
    public final void set(final double newValue) {
        final long next = Double.doubleToRawLongBits(newValue);
        this.value = next;
    }
    
    public final void lazySet(final double newValue) {
        this.set(newValue);
    }
    
    public final double getAndSet(final double newValue) {
        final long next = Double.doubleToRawLongBits(newValue);
        return Double.longBitsToDouble(AtomicDouble.updater.getAndSet(this, next));
    }
    
    public final boolean compareAndSet(final double expect, final double update) {
        return AtomicDouble.updater.compareAndSet(this, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
    }
    
    public final boolean weakCompareAndSet(final double expect, final double update) {
        return AtomicDouble.updater.weakCompareAndSet(this, Double.doubleToRawLongBits(expect), Double.doubleToRawLongBits(update));
    }
    
    public final double getAndAdd(final double delta) {
        long current;
        double currentVal;
        long next;
        do {
            current = this.value;
            currentVal = Double.longBitsToDouble(current);
            final double nextVal = currentVal + delta;
            next = Double.doubleToRawLongBits(nextVal);
        } while (!AtomicDouble.updater.compareAndSet(this, current, next));
        return currentVal;
    }
    
    public final double addAndGet(final double delta) {
        long current;
        double nextVal;
        long next;
        do {
            current = this.value;
            final double currentVal = Double.longBitsToDouble(current);
            nextVal = currentVal + delta;
            next = Double.doubleToRawLongBits(nextVal);
        } while (!AtomicDouble.updater.compareAndSet(this, current, next));
        return nextVal;
    }
    
    @Override
    public String toString() {
        return Double.toString(this.get());
    }
    
    @Override
    public int intValue() {
        return (int)this.get();
    }
    
    @Override
    public long longValue() {
        return (long)this.get();
    }
    
    @Override
    public float floatValue() {
        return (float)this.get();
    }
    
    @Override
    public double doubleValue() {
        return this.get();
    }
    
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeDouble(this.get());
    }
    
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.set(s.readDouble());
    }
    
    static {
        updater = AtomicLongFieldUpdater.newUpdater(AtomicDouble.class, "value");
    }
}
