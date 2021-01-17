// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

import com.ibm.icu.impl.Utility;

public final class CompactByteArray implements Cloneable
{
    @Deprecated
    public static final int UNICODECOUNT = 65536;
    private static final int BLOCKSHIFT = 7;
    private static final int BLOCKCOUNT = 128;
    private static final int INDEXSHIFT = 9;
    private static final int INDEXCOUNT = 512;
    private static final int BLOCKMASK = 127;
    private byte[] values;
    private char[] indices;
    private int[] hashes;
    private boolean isCompact;
    byte defaultValue;
    
    @Deprecated
    public CompactByteArray() {
        this((byte)0);
    }
    
    @Deprecated
    public CompactByteArray(final byte defaultValue) {
        this.values = new byte[65536];
        this.indices = new char[512];
        this.hashes = new int[512];
        for (int i = 0; i < 65536; ++i) {
            this.values[i] = defaultValue;
        }
        for (int i = 0; i < 512; ++i) {
            this.indices[i] = (char)(i << 7);
            this.hashes[i] = 0;
        }
        this.isCompact = false;
        this.defaultValue = defaultValue;
    }
    
    @Deprecated
    public CompactByteArray(final char[] indexArray, final byte[] newValues) {
        if (indexArray.length != 512) {
            throw new IllegalArgumentException("Index out of bounds.");
        }
        for (int i = 0; i < 512; ++i) {
            final char index = indexArray[i];
            if (index < '\0' || index >= newValues.length + 128) {
                throw new IllegalArgumentException("Index out of bounds.");
            }
        }
        this.indices = indexArray;
        this.values = newValues;
        this.isCompact = true;
    }
    
    @Deprecated
    public CompactByteArray(final String indexArray, final String valueArray) {
        this(Utility.RLEStringToCharArray(indexArray), Utility.RLEStringToByteArray(valueArray));
    }
    
    @Deprecated
    public byte elementAt(final char index) {
        return this.values[(this.indices[index >> 7] & '\uffff') + (index & '\u007f')];
    }
    
    @Deprecated
    public void setElementAt(final char index, final byte value) {
        if (this.isCompact) {
            this.expand();
        }
        this.touchBlock(index >> 7, this.values[index] = value);
    }
    
    @Deprecated
    public void setElementAt(final char start, final char end, final byte value) {
        if (this.isCompact) {
            this.expand();
        }
        for (int i = start; i <= end; ++i) {
            this.touchBlock(i >> 7, this.values[i] = value);
        }
    }
    
    @Deprecated
    public void compact() {
        this.compact(false);
    }
    
    @Deprecated
    public void compact(final boolean exhaustive) {
        if (!this.isCompact) {
            int limitCompacted = 0;
            int iBlockStart = 0;
            char iUntouched = '\uffff';
            for (int i = 0; i < this.indices.length; ++i, iBlockStart += 128) {
                this.indices[i] = '\uffff';
                final boolean touched = this.blockTouched(i);
                if (!touched && iUntouched != '\uffff') {
                    this.indices[i] = iUntouched;
                }
                else {
                    int jBlockStart;
                    int j;
                    for (jBlockStart = 0, j = 0, j = 0; j < limitCompacted; ++j, jBlockStart += 128) {
                        if (this.hashes[i] == this.hashes[j] && arrayRegionMatches(this.values, iBlockStart, this.values, jBlockStart, 128)) {
                            this.indices[i] = (char)jBlockStart;
                            break;
                        }
                    }
                    if (this.indices[i] == '\uffff') {
                        System.arraycopy(this.values, iBlockStart, this.values, jBlockStart, 128);
                        this.indices[i] = (char)jBlockStart;
                        this.hashes[j] = this.hashes[i];
                        ++limitCompacted;
                        if (!touched) {
                            iUntouched = (char)jBlockStart;
                        }
                    }
                }
            }
            final int newSize = limitCompacted * 128;
            final byte[] result = new byte[newSize];
            System.arraycopy(this.values, 0, result, 0, newSize);
            this.values = result;
            this.isCompact = true;
            this.hashes = null;
        }
    }
    
    static final boolean arrayRegionMatches(final byte[] source, final int sourceStart, final byte[] target, final int targetStart, final int len) {
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
    public byte[] getValueArray() {
        return this.values;
    }
    
    @Deprecated
    public Object clone() {
        try {
            final CompactByteArray other = (CompactByteArray)super.clone();
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
        final CompactByteArray other = (CompactByteArray)obj;
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
            this.hashes = new int[512];
            final byte[] tempArray = new byte[65536];
            for (int i = 0; i < 65536; ++i) {
                final byte value = this.elementAt((char)i);
                this.touchBlock(i >> 7, tempArray[i] = value);
            }
            for (int i = 0; i < 512; ++i) {
                this.indices[i] = (char)(i << 7);
            }
            this.values = null;
            this.values = tempArray;
            this.isCompact = false;
        }
    }
}
