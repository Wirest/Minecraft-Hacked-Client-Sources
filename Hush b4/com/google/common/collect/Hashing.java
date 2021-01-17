// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
final class Hashing
{
    private static final int C1 = -862048943;
    private static final int C2 = 461845907;
    private static int MAX_TABLE_SIZE;
    
    private Hashing() {
    }
    
    static int smear(final int hashCode) {
        return 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
    }
    
    static int smearedHash(@Nullable final Object o) {
        return smear((o == null) ? 0 : o.hashCode());
    }
    
    static int closedTableSize(int expectedEntries, final double loadFactor) {
        expectedEntries = Math.max(expectedEntries, 2);
        int tableSize = Integer.highestOneBit(expectedEntries);
        if (expectedEntries > (int)(loadFactor * tableSize)) {
            tableSize <<= 1;
            return (tableSize > 0) ? tableSize : Hashing.MAX_TABLE_SIZE;
        }
        return tableSize;
    }
    
    static boolean needsResizing(final int size, final int tableSize, final double loadFactor) {
        return size > loadFactor * tableSize && tableSize < Hashing.MAX_TABLE_SIZE;
    }
    
    static {
        Hashing.MAX_TABLE_SIZE = 1073741824;
    }
}
