// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

public class CalendarCache
{
    private static final int[] primes;
    private int pIndex;
    private int size;
    private int arraySize;
    private int threshold;
    private long[] keys;
    private long[] values;
    public static long EMPTY;
    
    public CalendarCache() {
        this.pIndex = 0;
        this.size = 0;
        this.arraySize = CalendarCache.primes[this.pIndex];
        this.threshold = this.arraySize * 3 / 4;
        this.keys = new long[this.arraySize];
        this.values = new long[this.arraySize];
        this.makeArrays(this.arraySize);
    }
    
    private void makeArrays(final int newSize) {
        this.keys = new long[newSize];
        this.values = new long[newSize];
        for (int i = 0; i < newSize; ++i) {
            this.values[i] = CalendarCache.EMPTY;
        }
        this.arraySize = newSize;
        this.threshold = (int)(this.arraySize * 0.75);
        this.size = 0;
    }
    
    public synchronized long get(final long key) {
        return this.values[this.findIndex(key)];
    }
    
    public synchronized void put(final long key, final long value) {
        if (this.size >= this.threshold) {
            this.rehash();
        }
        final int index = this.findIndex(key);
        this.keys[index] = key;
        this.values[index] = value;
        ++this.size;
    }
    
    private final int findIndex(final long key) {
        int index = this.hash(key);
        for (int delta = 0; this.values[index] != CalendarCache.EMPTY && this.keys[index] != key; index = (index + delta) % this.arraySize) {
            if (delta == 0) {
                delta = this.hash2(key);
            }
        }
        return index;
    }
    
    private void rehash() {
        final int oldSize = this.arraySize;
        final long[] oldKeys = this.keys;
        final long[] oldValues = this.values;
        if (this.pIndex < CalendarCache.primes.length - 1) {
            this.arraySize = CalendarCache.primes[++this.pIndex];
        }
        else {
            this.arraySize = this.arraySize * 2 + 1;
        }
        this.size = 0;
        this.makeArrays(this.arraySize);
        for (int i = 0; i < oldSize; ++i) {
            if (oldValues[i] != CalendarCache.EMPTY) {
                this.put(oldKeys[i], oldValues[i]);
            }
        }
    }
    
    private final int hash(final long key) {
        int h = (int)((key * 15821L + 1L) % this.arraySize);
        if (h < 0) {
            h += this.arraySize;
        }
        return h;
    }
    
    private final int hash2(final long key) {
        return this.arraySize - 2 - (int)(key % (this.arraySize - 2));
    }
    
    static {
        primes = new int[] { 61, 127, 509, 1021, 2039, 4093, 8191, 16381, 32749, 65521, 131071, 262139 };
        CalendarCache.EMPTY = Long.MIN_VALUE;
    }
}
