// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.util.BitSet;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible("no precomputation is done in GWT")
final class SmallCharMatcher extends FastMatcher
{
    static final int MAX_SIZE = 1023;
    private final char[] table;
    private final boolean containsZero;
    private final long filter;
    private static final int C1 = -862048943;
    private static final int C2 = 461845907;
    private static final double DESIRED_LOAD_FACTOR = 0.5;
    
    private SmallCharMatcher(final char[] table, final long filter, final boolean containsZero, final String description) {
        super(description);
        this.table = table;
        this.filter = filter;
        this.containsZero = containsZero;
    }
    
    static int smear(final int hashCode) {
        return 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
    }
    
    private boolean checkFilter(final int c) {
        return 0x1L == (0x1L & this.filter >> c);
    }
    
    @VisibleForTesting
    static int chooseTableSize(final int setSize) {
        if (setSize == 1) {
            return 2;
        }
        int tableSize;
        for (tableSize = Integer.highestOneBit(setSize - 1) << 1; tableSize * 0.5 < setSize; tableSize <<= 1) {}
        return tableSize;
    }
    
    static CharMatcher from(final BitSet chars, final String description) {
        long filter = 0L;
        final int size = chars.cardinality();
        final boolean containsZero = chars.get(0);
        final char[] table = new char[chooseTableSize(size)];
        final int mask = table.length - 1;
        for (int c = chars.nextSetBit(0); c != -1; c = chars.nextSetBit(c + 1)) {
            filter |= 1L << c;
            int index;
            for (index = (smear(c) & mask); table[index] != '\0'; index = (index + 1 & mask)) {}
            table[index] = (char)c;
        }
        return new SmallCharMatcher(table, filter, containsZero, description);
    }
    
    @Override
    public boolean matches(final char c) {
        if (c == '\0') {
            return this.containsZero;
        }
        if (!this.checkFilter(c)) {
            return false;
        }
        final int mask = this.table.length - 1;
        int index;
        final int startingIndex = index = (smear(c) & mask);
        while (this.table[index] != '\0') {
            if (this.table[index] == c) {
                return true;
            }
            index = (index + 1 & mask);
            if (index == startingIndex) {
                return false;
            }
        }
        return false;
    }
    
    @Override
    void setBits(final BitSet table) {
        if (this.containsZero) {
            table.set(0);
        }
        for (final char c : this.table) {
            if (c != '\0') {
                table.set(c);
            }
        }
    }
}
