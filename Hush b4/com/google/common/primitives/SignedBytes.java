// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.primitives;

import java.util.Comparator;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class SignedBytes
{
    public static final byte MAX_POWER_OF_TWO = 64;
    
    private SignedBytes() {
    }
    
    public static byte checkedCast(final long value) {
        final byte result = (byte)value;
        if (result != value) {
            throw new IllegalArgumentException("Out of range: " + value);
        }
        return result;
    }
    
    public static byte saturatedCast(final long value) {
        if (value > 127L) {
            return 127;
        }
        if (value < -128L) {
            return -128;
        }
        return (byte)value;
    }
    
    public static int compare(final byte a, final byte b) {
        return a - b;
    }
    
    public static byte min(final byte... array) {
        Preconditions.checkArgument(array.length > 0);
        byte min = array[0];
        for (int i = 1; i < array.length; ++i) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }
    
    public static byte max(final byte... array) {
        Preconditions.checkArgument(array.length > 0);
        byte max = array[0];
        for (int i = 1; i < array.length; ++i) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
    
    public static String join(final String separator, final byte... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        final StringBuilder builder = new StringBuilder(array.length * 5);
        builder.append(array[0]);
        for (int i = 1; i < array.length; ++i) {
            builder.append(separator).append(array[i]);
        }
        return builder.toString();
    }
    
    public static Comparator<byte[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }
    
    private enum LexicographicalComparator implements Comparator<byte[]>
    {
        INSTANCE;
        
        @Override
        public int compare(final byte[] left, final byte[] right) {
            for (int minLength = Math.min(left.length, right.length), i = 0; i < minLength; ++i) {
                final int result = SignedBytes.compare(left[i], right[i]);
                if (result != 0) {
                    return result;
                }
            }
            return left.length - right.length;
        }
    }
}
