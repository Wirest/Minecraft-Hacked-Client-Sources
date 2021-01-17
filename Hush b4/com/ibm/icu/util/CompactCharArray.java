// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import com.ibm.icu.impl.Utility;

public final class CompactCharArray implements Cloneable
{
    @Deprecated
    public static final int UNICODECOUNT = 65536;
    @Deprecated
    public static final int BLOCKSHIFT = 5;
    static final int BLOCKCOUNT = 32;
    static final int INDEXSHIFT = 11;
    static final int INDEXCOUNT = 2048;
    static final int BLOCKMASK = 31;
    private char[] values;
    private char[] indices;
    private int[] hashes;
    private boolean isCompact;
    char defaultValue;
    
    @Deprecated
    public CompactCharArray() {
        this('\0');
    }
    
    @Deprecated
    public CompactCharArray(final char defaultValue) {
        this.values = new char[65536];
        this.indices = new char[2048];
        this.hashes = new int[2048];
        for (int i = 0; i < 65536; ++i) {
            this.values[i] = defaultValue;
        }
        for (int i = 0; i < 2048; ++i) {
            this.indices[i] = (char)(i << 5);
            this.hashes[i] = 0;
        }
        this.isCompact = false;
        this.defaultValue = defaultValue;
    }
    
    @Deprecated
    public CompactCharArray(final char[] indexArray, final char[] newValues) {
        if (indexArray.length != 2048) {
            throw new IllegalArgumentException("Index out of bounds.");
        }
        for (int i = 0; i < 2048; ++i) {
            final char index = indexArray[i];
            if (index < '\0' || index >= newValues.length + 32) {
                throw new IllegalArgumentException("Index out of bounds.");
            }
        }
        this.indices = indexArray;
        this.values = newValues;
        this.isCompact = true;
    }
    
    @Deprecated
    public CompactCharArray(final String indexArray, final String valueArray) {
        this(Utility.RLEStringToCharArray(indexArray), Utility.RLEStringToCharArray(valueArray));
    }
    
    @Deprecated
    public char elementAt(final char index) {
        final int ix = (this.indices[index >> 5] & '\uffff') + (index & '\u001f');
        return (ix >= this.values.length) ? this.defaultValue : this.values[ix];
    }
    
    @Deprecated
    public void setElementAt(final char index, final char value) {
        if (this.isCompact) {
            this.expand();
        }
        this.touchBlock(index >> 5, this.values[index] = value);
    }
    
    @Deprecated
    public void setElementAt(final char start, final char end, final char value) {
        if (this.isCompact) {
            this.expand();
        }
        for (int i = start; i <= end; ++i) {
            this.touchBlock(i >> 5, this.values[i] = value);
        }
    }
    
    @Deprecated
    public void compact() {
        this.compact(true);
    }
    
    @Deprecated
    public void compact(final boolean exhaustive) {
        if (!this.isCompact) {
            int iBlockStart = 0;
            char iUntouched = '\uffff';
            int newSize = 0;
            final char[] target = exhaustive ? new char[65536] : this.values;
            for (int i = 0; i < this.indices.length; ++i, iBlockStart += 32) {
                this.indices[i] = '\uffff';
                final boolean touched = this.blockTouched(i);
                if (!touched && iUntouched != '\uffff') {
                    this.indices[i] = iUntouched;
                }
                else {
                    int jBlockStart = 0;
                    for (int j = 0; j < i; ++j, jBlockStart += 32) {
                        if (this.hashes[i] == this.hashes[j] && arrayRegionMatches(this.values, iBlockStart, this.values, jBlockStart, 32)) {
                            this.indices[i] = this.indices[j];
                        }
                    }
                    if (this.indices[i] == '\uffff') {
                        int dest;
                        if (exhaustive) {
                            dest = this.FindOverlappingPosition(iBlockStart, target, newSize);
                        }
                        else {
                            dest = newSize;
                        }
                        final int limit = dest + 32;
                        if (limit > newSize) {
                            for (int k = newSize; k < limit; ++k) {
                                target[k] = this.values[iBlockStart + k - dest];
                            }
                            newSize = limit;
                        }
                        this.indices[i] = (char)dest;
                        if (!touched) {
                            iUntouched = (char)jBlockStart;
                        }
                    }
                }
            }
            final char[] result = new char[newSize];
            System.arraycopy(target, 0, result, 0, newSize);
            this.values = result;
            this.isCompact = true;
            this.hashes = null;
        }
    }
    
    private int FindOverlappingPosition(final int start, final char[] tempValues, final int tempCount) {
        for (int i = 0; i < tempCount; ++i) {
            int currentCount = 32;
            if (i + 32 > tempCount) {
                currentCount = tempCount - i;
            }
            if (arrayRegionMatches(this.values, start, tempValues, i, currentCount)) {
                return i;
            }
        }
        return tempCount;
    }
    
    static final boolean arrayRegionMatches(final char[] source, final int sourceStart, final char[] target, final int targetStart, final int len) {
        final int sourceEnd = sourceStart + len;
        final int delta = targetStart - sourceStart;
        for (int i = sourceStart; i < sourceEnd; ++i) {
            if (source[i] != target[i + delta]) {
                return false;
            }
        }
        return true;
    }
    
    private final void touchBlock(final int i, final int value) {
        this.hashes[i] = (this.hashes[i] + (value << 1) | 0x1);
    }
    
    private final boolean blockTouched(final int i) {
        return this.hashes[i] != 0;
    }
    
    @Deprecated
    public char[] getIndexArray() {
        return this.indices;
    }
    
    @Deprecated
    public char[] getValueArray() {
        return this.values;
    }
    
    @Deprecated
    public Object clone() {
        try {
            final CompactCharArray other = (CompactCharArray)super.clone();
            other.values = this.values.clone();
            other.indices = this.indices.clone();
            if (this.hashes != null) {
                other.hashes = this.hashes.clone();
            }
            return other;
        }
        catch (CloneNotSupportedException e) {
            throw new IllegalStateException();
        }
    }
    
    @Override
    @Deprecated
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final CompactCharArray other = (CompactCharArray)obj;
        for (int i = 0; i < 65536; ++i) {
            if (this.elementAt((char)i) != other.elementAt((char)i)) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    @Deprecated
    public int hashCode() {
        int result = 0;
        for (int increment = Math.min(3, this.values.length / 16), i = 0; i < this.values.length; i += increment) {
            result = result * 37 + this.values[i];
        }
        return result;
    }
    
    private void expand() {
        if (this.isCompact) {
            this.hashes = new int[2048];
            final char[] tempArray = new char[65536];
            for (int i = 0; i < 65536; ++i) {
                tempArray[i] = this.elementAt((char)i);
            }
            for (int i = 0; i < 2048; ++i) {
                this.indices[i] = (char)(i << 5);
            }
            this.values = null;
            this.values = tempArray;
            this.isCompact = false;
        }
    }
}
