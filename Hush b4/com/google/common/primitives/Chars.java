// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.primitives;

import java.io.Serializable;
import java.util.RandomAccess;
import java.util.AbstractList;
import java.util.Collections;
import java.util.List;
import java.util.Collection;
import java.util.Comparator;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Chars
{
    public static final int BYTES = 2;
    
    private Chars() {
    }
    
    public static int hashCode(final char value) {
        return value;
    }
    
    public static char checkedCast(final long value) {
        final char result = (char)value;
        if (result != value) {
            throw new IllegalArgumentException("Out of range: " + value);
        }
        return result;
    }
    
    public static char saturatedCast(final long value) {
        if (value > 65535L) {
            return '\uffff';
        }
        if (value < 0L) {
            return '\0';
        }
        return (char)value;
    }
    
    public static int compare(final char a, final char b) {
        return a - b;
    }
    
    public static boolean contains(final char[] array, final char target) {
        for (final char value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }
    
    public static int indexOf(final char[] array, final char target) {
        return indexOf(array, target, 0, array.length);
    }
    
    private static int indexOf(final char[] array, final char target, final int start, final int end) {
        for (int i = start; i < end; ++i) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    
    public static int indexOf(final char[] array, final char[] target) {
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
    
    public static int lastIndexOf(final char[] array, final char target) {
        return lastIndexOf(array, target, 0, array.length);
    }
    
    private static int lastIndexOf(final char[] array, final char target, final int start, final int end) {
        for (int i = end - 1; i >= start; --i) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    
    public static char min(final char... array) {
        Preconditions.checkArgument(array.length > 0);
        char min = array[0];
        for (int i = 1; i < array.length; ++i) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }
    
    public static char max(final char... array) {
        Preconditions.checkArgument(array.length > 0);
        char max = array[0];
        for (int i = 1; i < array.length; ++i) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }
    
    public static char[] concat(final char[]... arrays) {
        int length = 0;
        for (final char[] array : arrays) {
            length += array.length;
        }
        final char[] result = new char[length];
        int pos = 0;
        for (final char[] array2 : arrays) {
            System.arraycopy(array2, 0, result, pos, array2.length);
            pos += array2.length;
        }
        return result;
    }
    
    @GwtIncompatible("doesn't work")
    public static byte[] toByteArray(final char value) {
        return new byte[] { (byte)(value >> 8), (byte)value };
    }
    
    @GwtIncompatible("doesn't work")
    public static char fromByteArray(final byte[] bytes) {
        Preconditions.checkArgument(bytes.length >= 2, "array too small: %s < %s", bytes.length, 2);
        return fromBytes(bytes[0], bytes[1]);
    }
    
    @GwtIncompatible("doesn't work")
    public static char fromBytes(final byte b1, final byte b2) {
        return (char)(b1 << 8 | (b2 & 0xFF));
    }
    
    public static char[] ensureCapacity(final char[] array, final int minLength, final int padding) {
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? copyOf(array, minLength + padding) : array;
    }
    
    private static char[] copyOf(final char[] original, final int length) {
        final char[] copy = new char[length];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
        return copy;
    }
    
    public static String join(final String separator, final char... array) {
        Preconditions.checkNotNull(separator);
        final int len = array.length;
        if (len == 0) {
            return "";
        }
        final StringBuilder builder = new StringBuilder(len + separator.length() * (len - 1));
        builder.append(array[0]);
        for (int i = 1; i < len; ++i) {
            builder.append(separator).append(array[i]);
        }
        return builder.toString();
    }
    
    public static Comparator<char[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }
    
    public static char[] toArray(final Collection<Character> collection) {
        if (collection instanceof CharArrayAsList) {
            return ((CharArrayAsList)collection).toCharArray();
        }
        final Object[] boxedArray = collection.toArray();
        final int len = boxedArray.length;
        final char[] array = new char[len];
        for (int i = 0; i < len; ++i) {
            array[i] = Preconditions.checkNotNull(boxedArray[i]);
        }
        return array;
    }
    
    public static List<Character> asList(final char... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new CharArrayAsList(backingArray);
    }
    
    private enum LexicographicalComparator implements Comparator<char[]>
    {
        INSTANCE;
        
        @Override
        public int compare(final char[] left, final char[] right) {
            for (int minLength = Math.min(left.length, right.length), i = 0; i < minLength; ++i) {
                final int result = Chars.compare(left[i], right[i]);
                if (result != 0) {
                    return result;
                }
            }
            return left.length - right.length;
        }
    }
    
    @GwtCompatible
    private static class CharArrayAsList extends AbstractList<Character> implements RandomAccess, Serializable
    {
        final char[] array;
        final int start;
        final int end;
        private static final long serialVersionUID = 0L;
        
        CharArrayAsList(final char[] array) {
            this(array, 0, array.length);
        }
        
        CharArrayAsList(final char[] array, final int start, final int end) {
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
        public Character get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            return this.array[this.start + index];
        }
        
        @Override
        public boolean contains(final Object target) {
            return target instanceof Character && indexOf(this.array, (char)target, this.start, this.end) != -1;
        }
        
        @Override
        public int indexOf(final Object target) {
            if (target instanceof Character) {
                final int i = indexOf(this.array, (char)target, this.start, this.end);
                if (i >= 0) {
                    return i - this.start;
                }
            }
            return -1;
        }
        
        @Override
        public int lastIndexOf(final Object target) {
            if (target instanceof Character) {
                final int i = lastIndexOf(this.array, (char)target, this.start, this.end);
                if (i >= 0) {
                    return i - this.start;
                }
            }
            return -1;
        }
        
        @Override
        public Character set(final int index, final Character element) {
            Preconditions.checkElementIndex(index, this.size());
            final char oldValue = this.array[this.start + index];
            this.array[this.start + index] = Preconditions.checkNotNull(element);
            return oldValue;
        }
        
        @Override
        public List<Character> subList(final int fromIndex, final int toIndex) {
            final int size = this.size();
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            return new CharArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
        }
        
        @Override
        public boolean equals(final Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof CharArrayAsList)) {
                return super.equals(object);
            }
            final CharArrayAsList that = (CharArrayAsList)object;
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
                result = 31 * result + Chars.hashCode(this.array[i]);
            }
            return result;
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder(this.size() * 3);
            builder.append('[').append(this.array[this.start]);
            for (int i = this.start + 1; i < this.end; ++i) {
                builder.append(", ").append(this.array[i]);
            }
            return builder.append(']').toString();
        }
        
        char[] toCharArray() {
            final int size = this.size();
            final char[] result = new char[size];
            System.arraycopy(this.array, this.start, result, 0, size);
            return result;
        }
    }
}
