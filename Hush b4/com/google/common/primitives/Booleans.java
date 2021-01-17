// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.primitives;

import java.io.Serializable;
import java.util.RandomAccess;
import java.util.AbstractList;
import com.google.common.annotations.Beta;
import java.util.Collections;
import java.util.List;
import java.util.Collection;
import java.util.Comparator;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class Booleans
{
    private Booleans() {
    }
    
    public static int hashCode(final boolean value) {
        return value ? 1231 : 1237;
    }
    
    public static int compare(final boolean a, final boolean b) {
        return (a == b) ? 0 : (a ? 1 : -1);
    }
    
    public static boolean contains(final boolean[] array, final boolean target) {
        for (final boolean value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }
    
    public static int indexOf(final boolean[] array, final boolean target) {
        return indexOf(array, target, 0, array.length);
    }
    
    private static int indexOf(final boolean[] array, final boolean target, final int start, final int end) {
        for (int i = start; i < end; ++i) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    
    public static int indexOf(final boolean[] array, final boolean[] target) {
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
    
    public static int lastIndexOf(final boolean[] array, final boolean target) {
        return lastIndexOf(array, target, 0, array.length);
    }
    
    private static int lastIndexOf(final boolean[] array, final boolean target, final int start, final int end) {
        for (int i = end - 1; i >= start; --i) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    
    public static boolean[] concat(final boolean[]... arrays) {
        int length = 0;
        for (final boolean[] array : arrays) {
            length += array.length;
        }
        final boolean[] result = new boolean[length];
        int pos = 0;
        for (final boolean[] array2 : arrays) {
            System.arraycopy(array2, 0, result, pos, array2.length);
            pos += array2.length;
        }
        return result;
    }
    
    public static boolean[] ensureCapacity(final boolean[] array, final int minLength, final int padding) {
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? copyOf(array, minLength + padding) : array;
    }
    
    private static boolean[] copyOf(final boolean[] original, final int length) {
        final boolean[] copy = new boolean[length];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
        return copy;
    }
    
    public static String join(final String separator, final boolean... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        final StringBuilder builder = new StringBuilder(array.length * 7);
        builder.append(array[0]);
        for (int i = 1; i < array.length; ++i) {
            builder.append(separator).append(array[i]);
        }
        return builder.toString();
    }
    
    public static Comparator<boolean[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }
    
    public static boolean[] toArray(final Collection<Boolean> collection) {
        if (collection instanceof BooleanArrayAsList) {
            return ((BooleanArrayAsList)collection).toBooleanArray();
        }
        final Object[] boxedArray = collection.toArray();
        final int len = boxedArray.length;
        final boolean[] array = new boolean[len];
        for (int i = 0; i < len; ++i) {
            array[i] = Preconditions.checkNotNull(boxedArray[i]);
        }
        return array;
    }
    
    public static List<Boolean> asList(final boolean... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new BooleanArrayAsList(backingArray);
    }
    
    @Beta
    public static int countTrue(final boolean... values) {
        int count = 0;
        for (final boolean value : values) {
            if (value) {
                ++count;
            }
        }
        return count;
    }
    
    private enum LexicographicalComparator implements Comparator<boolean[]>
    {
        INSTANCE;
        
        @Override
        public int compare(final boolean[] left, final boolean[] right) {
            for (int minLength = Math.min(left.length, right.length), i = 0; i < minLength; ++i) {
                final int result = Booleans.compare(left[i], right[i]);
                if (result != 0) {
                    return result;
                }
            }
            return left.length - right.length;
        }
    }
    
    @GwtCompatible
    private static class BooleanArrayAsList extends AbstractList<Boolean> implements RandomAccess, Serializable
    {
        final boolean[] array;
        final int start;
        final int end;
        private static final long serialVersionUID = 0L;
        
        BooleanArrayAsList(final boolean[] array) {
            this(array, 0, array.length);
        }
        
        BooleanArrayAsList(final boolean[] array, final int start, final int end) {
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
        public Boolean get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            return this.array[this.start + index];
        }
        
        @Override
        public boolean contains(final Object target) {
            return target instanceof Boolean && indexOf(this.array, (boolean)target, this.start, this.end) != -1;
        }
        
        @Override
        public int indexOf(final Object target) {
            if (target instanceof Boolean) {
                final int i = indexOf(this.array, (boolean)target, this.start, this.end);
                if (i >= 0) {
                    return i - this.start;
                }
            }
            return -1;
        }
        
        @Override
        public int lastIndexOf(final Object target) {
            if (target instanceof Boolean) {
                final int i = lastIndexOf(this.array, (boolean)target, this.start, this.end);
                if (i >= 0) {
                    return i - this.start;
                }
            }
            return -1;
        }
        
        @Override
        public Boolean set(final int index, final Boolean element) {
            Preconditions.checkElementIndex(index, this.size());
            final boolean oldValue = this.array[this.start + index];
            this.array[this.start + index] = Preconditions.checkNotNull(element);
            return oldValue;
        }
        
        @Override
        public List<Boolean> subList(final int fromIndex, final int toIndex) {
            final int size = this.size();
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            return new BooleanArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
        }
        
        @Override
        public boolean equals(final Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof BooleanArrayAsList)) {
                return super.equals(object);
            }
            final BooleanArrayAsList that = (BooleanArrayAsList)object;
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
                result = 31 * result + Booleans.hashCode(this.array[i]);
            }
            return result;
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder(this.size() * 7);
            builder.append(this.array[this.start] ? "[true" : "[false");
            for (int i = this.start + 1; i < this.end; ++i) {
                builder.append(this.array[i] ? ", true" : ", false");
            }
            return builder.append(']').toString();
        }
        
        boolean[] toBooleanArray() {
            final int size = this.size();
            final boolean[] result = new boolean[size];
            System.arraycopy(this.array, this.start, result, 0, size);
            return result;
        }
    }
}
