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
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class Bytes
{
    private Bytes() {
    }
    
    public static int hashCode(final byte value) {
        return value;
    }
    
    public static boolean contains(final byte[] array, final byte target) {
        for (final byte value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }
    
    public static int indexOf(final byte[] array, final byte target) {
        return indexOf(array, target, 0, array.length);
    }
    
    private static int indexOf(final byte[] array, final byte target, final int start, final int end) {
        for (int i = start; i < end; ++i) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    
    public static int indexOf(final byte[] array, final byte[] target) {
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
    
    public static int lastIndexOf(final byte[] array, final byte target) {
        return lastIndexOf(array, target, 0, array.length);
    }
    
    private static int lastIndexOf(final byte[] array, final byte target, final int start, final int end) {
        for (int i = end - 1; i >= start; --i) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    
    public static byte[] concat(final byte[]... arrays) {
        int length = 0;
        for (final byte[] array : arrays) {
            length += array.length;
        }
        final byte[] result = new byte[length];
        int pos = 0;
        for (final byte[] array2 : arrays) {
            System.arraycopy(array2, 0, result, pos, array2.length);
            pos += array2.length;
        }
        return result;
    }
    
    public static byte[] ensureCapacity(final byte[] array, final int minLength, final int padding) {
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? copyOf(array, minLength + padding) : array;
    }
    
    private static byte[] copyOf(final byte[] original, final int length) {
        final byte[] copy = new byte[length];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
        return copy;
    }
    
    public static byte[] toArray(final Collection<? extends Number> collection) {
        if (collection instanceof ByteArrayAsList) {
            return ((ByteArrayAsList)collection).toByteArray();
        }
        final Object[] boxedArray = collection.toArray();
        final int len = boxedArray.length;
        final byte[] array = new byte[len];
        for (int i = 0; i < len; ++i) {
            array[i] = Preconditions.checkNotNull(boxedArray[i]).byteValue();
        }
        return array;
    }
    
    public static List<Byte> asList(final byte... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new ByteArrayAsList(backingArray);
    }
    
    @GwtCompatible
    private static class ByteArrayAsList extends AbstractList<Byte> implements RandomAccess, Serializable
    {
        final byte[] array;
        final int start;
        final int end;
        private static final long serialVersionUID = 0L;
        
        ByteArrayAsList(final byte[] array) {
            this(array, 0, array.length);
        }
        
        ByteArrayAsList(final byte[] array, final int start, final int end) {
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
        public Byte get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            return this.array[this.start + index];
        }
        
        @Override
        public boolean contains(final Object target) {
            return target instanceof Byte && indexOf(this.array, (byte)target, this.start, this.end) != -1;
        }
        
        @Override
        public int indexOf(final Object target) {
            if (target instanceof Byte) {
                final int i = indexOf(this.array, (byte)target, this.start, this.end);
                if (i >= 0) {
                    return i - this.start;
                }
            }
            return -1;
        }
        
        @Override
        public int lastIndexOf(final Object target) {
            if (target instanceof Byte) {
                final int i = lastIndexOf(this.array, (byte)target, this.start, this.end);
                if (i >= 0) {
                    return i - this.start;
                }
            }
            return -1;
        }
        
        @Override
        public Byte set(final int index, final Byte element) {
            Preconditions.checkElementIndex(index, this.size());
            final byte oldValue = this.array[this.start + index];
            this.array[this.start + index] = Preconditions.checkNotNull(element);
            return oldValue;
        }
        
        @Override
        public List<Byte> subList(final int fromIndex, final int toIndex) {
            final int size = this.size();
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            return new ByteArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
        }
        
        @Override
        public boolean equals(final Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof ByteArrayAsList)) {
                return super.equals(object);
            }
            final ByteArrayAsList that = (ByteArrayAsList)object;
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
                result = 31 * result + Bytes.hashCode(this.array[i]);
            }
            return result;
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder(this.size() * 5);
            builder.append('[').append(this.array[this.start]);
            for (int i = this.start + 1; i < this.end; ++i) {
                builder.append(", ").append(this.array[i]);
            }
            return builder.append(']').toString();
        }
        
        byte[] toByteArray() {
            final int size = this.size();
            final byte[] result = new byte[size];
            System.arraycopy(this.array, this.start, result, 0, size);
            return result;
        }
    }
}
