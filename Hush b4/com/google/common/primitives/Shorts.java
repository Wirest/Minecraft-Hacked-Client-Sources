// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.primitives;

import java.util.RandomAccess;
import java.util.AbstractList;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Collection;
import java.util.Comparator;
import com.google.common.annotations.Beta;
import com.google.common.base.Converter;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Shorts
{
    public static final int BYTES = 2;
    public static final short MAX_POWER_OF_TWO = 16384;
    
    private Shorts() {
    }
    
    public static int hashCode(final short value) {
        return value;
    }
    
    public static short checkedCast(final long value) {
        final short result = (short)value;
        if (result != value) {
            throw new IllegalArgumentException("Out of range: " + value);
        }
        return result;
    }
    
    public static short saturatedCast(final long value) {
        if (value > 32767L) {
            return 32767;
        }
        if (value < -32768L) {
            return -32768;
        }
        return (short)value;
    }
    
    public static int compare(final short a, final short b) {
        return a - b;
    }
    
    public static boolean contains(final short[] array, final short target) {
        for (final short value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }
    
    public static int indexOf(final short[] array, final short target) {
        return indexOf(array, target, 0, array.length);
    }
    
    private static int indexOf(final short[] array, final short target, final int start, final int end) {
        for (int i = start; i < end; ++i) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    
    public static int indexOf(final short[] array, final short[] target) {
        Preconditions.checkNotNull(array, (Object)"array");
        Preconditions.checkNotNull(target, (Object)"target");
        if (target.length == 0) {
            return 0;
        }
        int i = 0;
    Label_0023:
        while (i < array.length - target.length + 1) {
            for (int j = 0; j < target.length; ++j) {
                if (array[i + j] != target[j]) {
                    ++i;
                    continue Label_0023;
                }
            }
            return i;
        }
        return -1;
    }
    
    public static int lastIndexOf(final short[] array, final short target) {
        return lastIndexOf(array, target, 0, array.length);
    }
    
    private static int lastIndexOf(final short[] array, final short target, final int start, final int end) {
        for (int i = end - 1; i >= start; --i) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    
    public static short min(final short... array) {
        Preconditions.checkArgument(array.length > 0);
        short min = array[0];
        for (int i = 1; i < array.length; ++i) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }
    
    public static short max(final short... array) {
        Preconditions.checkArgument(array.length > 0);
        short max = array[0];
        for (int i = 1; i < array.length; ++i) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
    
    public static short[] concat(final short[]... arrays) {
        int length = 0;
        for (final short[] array : arrays) {
            length += array.length;
        }
        final short[] result = new short[length];
        int pos = 0;
        for (final short[] array2 : arrays) {
            System.arraycopy(array2, 0, result, pos, array2.length);
            pos += array2.length;
        }
        return result;
    }
    
    @GwtIncompatible("doesn't work")
    public static byte[] toByteArray(final short value) {
        return new byte[] { (byte)(value >> 8), (byte)value };
    }
    
    @GwtIncompatible("doesn't work")
    public static short fromByteArray(final byte[] bytes) {
        Preconditions.checkArgument(bytes.length >= 2, "array too small: %s < %s", bytes.length, 2);
        return fromBytes(bytes[0], bytes[1]);
    }
    
    @GwtIncompatible("doesn't work")
    public static short fromBytes(final byte b1, final byte b2) {
        return (short)(b1 << 8 | (b2 & 0xFF));
    }
    
    @Beta
    public static Converter<String, Short> stringConverter() {
        return ShortConverter.INSTANCE;
    }
    
    public static short[] ensureCapacity(final short[] array, final int minLength, final int padding) {
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? copyOf(array, minLength + padding) : array;
    }
    
    private static short[] copyOf(final short[] original, final int length) {
        final short[] copy = new short[length];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
        return copy;
    }
    
    public static String join(final String separator, final short... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        final StringBuilder builder = new StringBuilder(array.length * 6);
        builder.append(array[0]);
        for (int i = 1; i < array.length; ++i) {
            builder.append(separator).append(array[i]);
        }
        return builder.toString();
    }
    
    public static Comparator<short[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }
    
    public static short[] toArray(final Collection<? extends Number> collection) {
        if (collection instanceof ShortArrayAsList) {
            return ((ShortArrayAsList)collection).toShortArray();
        }
        final Object[] boxedArray = collection.toArray();
        final int len = boxedArray.length;
        final short[] array = new short[len];
        for (int i = 0; i < len; ++i) {
            array[i] = Preconditions.checkNotNull(boxedArray[i]).shortValue();
        }
        return array;
    }
    
    public static List<Short> asList(final short... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new ShortArrayAsList(backingArray);
    }
    
    private static final class ShortConverter extends Converter<String, Short> implements Serializable
    {
        static final ShortConverter INSTANCE;
        private static final long serialVersionUID = 1L;
        
        @Override
        protected Short doForward(final String value) {
            return Short.decode(value);
        }
        
        @Override
        protected String doBackward(final Short value) {
            return value.toString();
        }
        
        @Override
        public String toString() {
            return "Shorts.stringConverter()";
        }
        
        private Object readResolve() {
            return ShortConverter.INSTANCE;
        }
        
        static {
            INSTANCE = new ShortConverter();
        }
    }
    
    private enum LexicographicalComparator implements Comparator<short[]>
    {
        INSTANCE;
        
        @Override
        public int compare(final short[] left, final short[] right) {
            for (int minLength = Math.min(left.length, right.length), i = 0; i < minLength; ++i) {
                final int result = Shorts.compare(left[i], right[i]);
                if (result != 0) {
                    return result;
                }
            }
            return left.length - right.length;
        }
    }
    
    @GwtCompatible
    private static class ShortArrayAsList extends AbstractList<Short> implements RandomAccess, Serializable
    {
        final short[] array;
        final int start;
        final int end;
        private static final long serialVersionUID = 0L;
        
        ShortArrayAsList(final short[] array) {
            this(array, 0, array.length);
        }
        
        ShortArrayAsList(final short[] array, final int start, final int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }
        
        @Override
        public int size() {
            return this.end - this.start;
        }
        
        @Override
        public boolean isEmpty() {
            return false;
        }
        
        @Override
        public Short get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            return this.array[this.start + index];
        }
        
        @Override
        public boolean contains(final Object target) {
            return target instanceof Short && indexOf(this.array, (short)target, this.start, this.end) != -1;
        }
        
        @Override
        public int indexOf(final Object target) {
            if (target instanceof Short) {
                final int i = indexOf(this.array, (short)target, this.start, this.end);
                if (i >= 0) {
                    return i - this.start;
                }
            }
            return -1;
        }
        
        @Override
        public int lastIndexOf(final Object target) {
            if (target instanceof Short) {
                final int i = lastIndexOf(this.array, (short)target, this.start, this.end);
                if (i >= 0) {
                    return i - this.start;
                }
            }
            return -1;
        }
        
        @Override
        public Short set(final int index, final Short element) {
            Preconditions.checkElementIndex(index, this.size());
            final short oldValue = this.array[this.start + index];
            this.array[this.start + index] = Preconditions.checkNotNull(element);
            return oldValue;
        }
        
        @Override
        public List<Short> subList(final int fromIndex, final int toIndex) {
            final int size = this.size();
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            return new ShortArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
        }
        
        @Override
        public boolean equals(final Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof ShortArrayAsList)) {
                return super.equals(object);
            }
            final ShortArrayAsList that = (ShortArrayAsList)object;
            final int size = this.size();
            if (that.size() != size) {
                return false;
            }
            for (int i = 0; i < size; ++i) {
                if (this.array[this.start + i] != that.array[that.start + i]) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int hashCode() {
            int result = 1;
            for (int i = this.start; i < this.end; ++i) {
                result = 31 * result + Shorts.hashCode(this.array[i]);
            }
            return result;
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder(this.size() * 6);
            builder.append('[').append(this.array[this.start]);
            for (int i = this.start + 1; i < this.end; ++i) {
                builder.append(", ").append(this.array[i]);
            }
            return builder.append(']').toString();
        }
        
        short[] toShortArray() {
            final int size = this.size();
            final short[] result = new short[size];
            System.arraycopy(this.array, this.start, result, 0, size);
            return result;
        }
    }
}
