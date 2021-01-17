// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.primitives;

import java.util.RandomAccess;
import java.util.AbstractList;
import java.io.Serializable;
import com.google.common.annotations.GwtIncompatible;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Collection;
import java.util.Comparator;
import com.google.common.annotations.Beta;
import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Floats
{
    public static final int BYTES = 4;
    
    private Floats() {
    }
    
    public static int hashCode(final float value) {
        return Float.valueOf(value).hashCode();
    }
    
    public static int compare(final float a, final float b) {
        return Float.compare(a, b);
    }
    
    public static boolean isFinite(final float value) {
        return Float.NEGATIVE_INFINITY < value & value < Float.POSITIVE_INFINITY;
    }
    
    public static boolean contains(final float[] array, final float target) {
        for (final float value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }
    
    public static int indexOf(final float[] array, final float target) {
        return indexOf(array, target, 0, array.length);
    }
    
    private static int indexOf(final float[] array, final float target, final int start, final int end) {
        for (int i = start; i < end; ++i) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    
    public static int indexOf(final float[] array, final float[] target) {
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
    
    public static int lastIndexOf(final float[] array, final float target) {
        return lastIndexOf(array, target, 0, array.length);
    }
    
    private static int lastIndexOf(final float[] array, final float target, final int start, final int end) {
        for (int i = end - 1; i >= start; --i) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    
    public static float min(final float... array) {
        Preconditions.checkArgument(array.length > 0);
        float min = array[0];
        for (int i = 1; i < array.length; ++i) {
            min = Math.min(min, array[i]);
        }
        return min;
    }
    
    public static float max(final float... array) {
        Preconditions.checkArgument(array.length > 0);
        float max = array[0];
        for (int i = 1; i < array.length; ++i) {
            max = Math.max(max, array[i]);
        }
        return max;
    }
    
    public static float[] concat(final float[]... arrays) {
        int length = 0;
        for (final float[] array : arrays) {
            length += array.length;
        }
        final float[] result = new float[length];
        int pos = 0;
        for (final float[] array2 : arrays) {
            System.arraycopy(array2, 0, result, pos, array2.length);
            pos += array2.length;
        }
        return result;
    }
    
    @Beta
    public static Converter<String, Float> stringConverter() {
        return FloatConverter.INSTANCE;
    }
    
    public static float[] ensureCapacity(final float[] array, final int minLength, final int padding) {
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? copyOf(array, minLength + padding) : array;
    }
    
    private static float[] copyOf(final float[] original, final int length) {
        final float[] copy = new float[length];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
        return copy;
    }
    
    public static String join(final String separator, final float... array) {
        Preconditions.checkNotNull(separator);
        if (array.length == 0) {
            return "";
        }
        final StringBuilder builder = new StringBuilder(array.length * 12);
        builder.append(array[0]);
        for (int i = 1; i < array.length; ++i) {
            builder.append(separator).append(array[i]);
        }
        return builder.toString();
    }
    
    public static Comparator<float[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }
    
    public static float[] toArray(final Collection<? extends Number> collection) {
        if (collection instanceof FloatArrayAsList) {
            return ((FloatArrayAsList)collection).toFloatArray();
        }
        final Object[] boxedArray = collection.toArray();
        final int len = boxedArray.length;
        final float[] array = new float[len];
        for (int i = 0; i < len; ++i) {
            array[i] = Preconditions.checkNotNull(boxedArray[i]).floatValue();
        }
        return array;
    }
    
    public static List<Float> asList(final float... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new FloatArrayAsList(backingArray);
    }
    
    @Nullable
    @GwtIncompatible("regular expressions")
    @Beta
    public static Float tryParse(final String string) {
        if (Doubles.FLOATING_POINT_PATTERN.matcher(string).matches()) {
            try {
                return Float.parseFloat(string);
            }
            catch (NumberFormatException ex) {}
        }
        return null;
    }
    
    private static final class FloatConverter extends Converter<String, Float> implements Serializable
    {
        static final FloatConverter INSTANCE;
        private static final long serialVersionUID = 1L;
        
        @Override
        protected Float doForward(final String value) {
            return Float.valueOf(value);
        }
        
        @Override
        protected String doBackward(final Float value) {
            return value.toString();
        }
        
        @Override
        public String toString() {
            return "Floats.stringConverter()";
        }
        
        private Object readResolve() {
            return FloatConverter.INSTANCE;
        }
        
        static {
            INSTANCE = new FloatConverter();
        }
    }
    
    private enum LexicographicalComparator implements Comparator<float[]>
    {
        INSTANCE;
        
        @Override
        public int compare(final float[] left, final float[] right) {
            for (int minLength = Math.min(left.length, right.length), i = 0; i < minLength; ++i) {
                final int result = Floats.compare(left[i], right[i]);
                if (result != 0) {
                    return result;
                }
            }
            return left.length - right.length;
        }
    }
    
    @GwtCompatible
    private static class FloatArrayAsList extends AbstractList<Float> implements RandomAccess, Serializable
    {
        final float[] array;
        final int start;
        final int end;
        private static final long serialVersionUID = 0L;
        
        FloatArrayAsList(final float[] array) {
            this(array, 0, array.length);
        }
        
        FloatArrayAsList(final float[] array, final int start, final int end) {
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
        public Float get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            return this.array[this.start + index];
        }
        
        @Override
        public boolean contains(final Object target) {
            return target instanceof Float && indexOf(this.array, (float)target, this.start, this.end) != -1;
        }
        
        @Override
        public int indexOf(final Object target) {
            if (target instanceof Float) {
                final int i = indexOf(this.array, (float)target, this.start, this.end);
                if (i >= 0) {
                    return i - this.start;
                }
            }
            return -1;
        }
        
        @Override
        public int lastIndexOf(final Object target) {
            if (target instanceof Float) {
                final int i = lastIndexOf(this.array, (float)target, this.start, this.end);
                if (i >= 0) {
                    return i - this.start;
                }
            }
            return -1;
        }
        
        @Override
        public Float set(final int index, final Float element) {
            Preconditions.checkElementIndex(index, this.size());
            final float oldValue = this.array[this.start + index];
            this.array[this.start + index] = Preconditions.checkNotNull(element);
            return oldValue;
        }
        
        @Override
        public List<Float> subList(final int fromIndex, final int toIndex) {
            final int size = this.size();
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            return new FloatArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
        }
        
        @Override
        public boolean equals(final Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof FloatArrayAsList)) {
                return super.equals(object);
            }
            final FloatArrayAsList that = (FloatArrayAsList)object;
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
                result = 31 * result + Floats.hashCode(this.array[i]);
            }
            return result;
        }
        
        @Override
        public String toString() {
            final StringBuilder builder = new StringBuilder(this.size() * 12);
            builder.append('[').append(this.array[this.start]);
            for (int i = this.start + 1; i < this.end; ++i) {
                builder.append(", ").append(this.array[i]);
            }
            return builder.append(']').toString();
        }
        
        float[] toFloatArray() {
            final int size = this.size();
            final float[] result = new float[size];
            System.arraycopy(this.array, this.start, result, 0, size);
            return result;
        }
    }
}
