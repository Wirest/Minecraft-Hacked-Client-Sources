// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.primitives;

import java.util.RandomAccess;
import java.util.AbstractList;
import java.io.Serializable;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Collection;
import java.util.Comparator;
import com.google.common.annotations.Beta;
import com.google.common.base.Converter;
import com.google.common.base.Preconditions;
import com.google.common.annotations.GwtIncompatible;
import java.util.regex.Pattern;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true)
public final class Doubles
{
    public static final int BYTES = 8;
    @GwtIncompatible("regular expressions")
    static final Pattern FLOATING_POINT_PATTERN;
    
    private Doubles() {
    }
    
    public static int hashCode(final double value) {
        return Double.valueOf(value).hashCode();
    }
    
    public static int compare(final double a, final double b) {
        return Double.compare(a, b);
    }
    
    public static boolean isFinite(final double value) {
        return Double.NEGATIVE_INFINITY < value & value < Double.POSITIVE_INFINITY;
    }
    
    public static boolean contains(final double[] array, final double target) {
        for (final double value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }
    
    public static int indexOf(final double[] array, final double target) {
        return indexOf(array, target, 0, array.length);
    }
    
    private static int indexOf(final double[] array, final double target, final int start, final int end) {
        for (int i = start; i < end; ++i) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    
    public static int indexOf(final double[] array, final double[] target) {
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
    
    public static int lastIndexOf(final double[] array, final double target) {
        return lastIndexOf(array, target, 0, array.length);
    }
    
    private static int lastIndexOf(final double[] array, final double target, final int start, final int end) {
        for (int i = end - 1; i >= start; --i) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }
    
    public static double min(final double... array) {
        Preconditions.checkArgument(array.length > 0);
        double min = array[0];
        for (int i = 1; i < array.length; ++i) {
            min = Math.min(min, array[i]);
        }
        return min;
    }
    
    public static double max(final double... array) {
        Preconditions.checkArgument(array.length > 0);
        double max = array[0];
        for (int i = 1; i < array.length; ++i) {
            max = Math.max(max, array[i]);
        }
        return max;
    }
    
    public static double[] concat(final double[]... arrays) {
        int length = 0;
        for (final double[] array : arrays) {
            length += array.length;
        }
        final double[] result = new double[length];
        int pos = 0;
        for (final double[] array2 : arrays) {
            System.arraycopy(array2, 0, result, pos, array2.length);
            pos += array2.length;
        }
        return result;
    }
    
    @Beta
    public static Converter<String, Double> stringConverter() {
        return DoubleConverter.INSTANCE;
    }
    
    public static double[] ensureCapacity(final double[] array, final int minLength, final int padding) {
        Preconditions.checkArgument(minLength >= 0, "Invalid minLength: %s", minLength);
        Preconditions.checkArgument(padding >= 0, "Invalid padding: %s", padding);
        return (array.length < minLength) ? copyOf(array, minLength + padding) : array;
    }
    
    private static double[] copyOf(final double[] original, final int length) {
        final double[] copy = new double[length];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, length));
        return copy;
    }
    
    public static String join(final String separator, final double... array) {
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
    
    public static Comparator<double[]> lexicographicalComparator() {
        return LexicographicalComparator.INSTANCE;
    }
    
    public static double[] toArray(final Collection<? extends Number> collection) {
        if (collection instanceof DoubleArrayAsList) {
            return ((DoubleArrayAsList)collection).toDoubleArray();
        }
        final Object[] boxedArray = collection.toArray();
        final int len = boxedArray.length;
        final double[] array = new double[len];
        for (int i = 0; i < len; ++i) {
            array[i] = Preconditions.checkNotNull(boxedArray[i]).doubleValue();
        }
        return array;
    }
    
    public static List<Double> asList(final double... backingArray) {
        if (backingArray.length == 0) {
            return Collections.emptyList();
        }
        return new DoubleArrayAsList(backingArray);
    }
    
    @GwtIncompatible("regular expressions")
    private static Pattern fpPattern() {
        final String decimal = "(?:\\d++(?:\\.\\d*+)?|\\.\\d++)";
        final String completeDec = decimal + "(?:[eE][+-]?\\d++)?[fFdD]?";
        final String hex = "(?:\\p{XDigit}++(?:\\.\\p{XDigit}*+)?|\\.\\p{XDigit}++)";
        final String completeHex = "0[xX]" + hex + "[pP][+-]?\\d++[fFdD]?";
        final String fpPattern = "[+-]?(?:NaN|Infinity|" + completeDec + "|" + completeHex + ")";
        return Pattern.compile(fpPattern);
    }
    
    @Nullable
    @GwtIncompatible("regular expressions")
    @Beta
    public static Double tryParse(final String string) {
        if (Doubles.FLOATING_POINT_PATTERN.matcher(string).matches()) {
            try {
                return Double.parseDouble(string);
            }
            catch (NumberFormatException ex) {}
        }
        return null;
    }
    
    static {
        FLOATING_POINT_PATTERN = fpPattern();
    }
    
    private static final class DoubleConverter extends Converter<String, Double> implements Serializable
    {
        static final DoubleConverter INSTANCE;
        private static final long serialVersionUID = 1L;
        
        @Override
        protected Double doForward(final String value) {
            return Double.valueOf(value);
        }
        
        @Override
        protected String doBackward(final Double value) {
            return value.toString();
        }
        
        @Override
        public String toString() {
            return "Doubles.stringConverter()";
        }
        
        private Object readResolve() {
            return DoubleConverter.INSTANCE;
        }
        
        static {
            INSTANCE = new DoubleConverter();
        }
    }
    
    private enum LexicographicalComparator implements Comparator<double[]>
    {
        INSTANCE;
        
        @Override
        public int compare(final double[] left, final double[] right) {
            for (int minLength = Math.min(left.length, right.length), i = 0; i < minLength; ++i) {
                final int result = Doubles.compare(left[i], right[i]);
                if (result != 0) {
                    return result;
                }
            }
            return left.length - right.length;
        }
    }
    
    @GwtCompatible
    private static class DoubleArrayAsList extends AbstractList<Double> implements RandomAccess, Serializable
    {
        final double[] array;
        final int start;
        final int end;
        private static final long serialVersionUID = 0L;
        
        DoubleArrayAsList(final double[] array) {
            this(array, 0, array.length);
        }
        
        DoubleArrayAsList(final double[] array, final int start, final int end) {
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
        public Double get(final int index) {
            Preconditions.checkElementIndex(index, this.size());
            return this.array[this.start + index];
        }
        
        @Override
        public boolean contains(final Object target) {
            return target instanceof Double && indexOf(this.array, (double)target, this.start, this.end) != -1;
        }
        
        @Override
        public int indexOf(final Object target) {
            if (target instanceof Double) {
                final int i = indexOf(this.array, (double)target, this.start, this.end);
                if (i >= 0) {
                    return i - this.start;
                }
            }
            return -1;
        }
        
        @Override
        public int lastIndexOf(final Object target) {
            if (target instanceof Double) {
                final int i = lastIndexOf(this.array, (double)target, this.start, this.end);
                if (i >= 0) {
                    return i - this.start;
                }
            }
            return -1;
        }
        
        @Override
        public Double set(final int index, final Double element) {
            Preconditions.checkElementIndex(index, this.size());
            final double oldValue = this.array[this.start + index];
            this.array[this.start + index] = Preconditions.checkNotNull(element);
            return oldValue;
        }
        
        @Override
        public List<Double> subList(final int fromIndex, final int toIndex) {
            final int size = this.size();
            Preconditions.checkPositionIndexes(fromIndex, toIndex, size);
            if (fromIndex == toIndex) {
                return Collections.emptyList();
            }
            return new DoubleArrayAsList(this.array, this.start + fromIndex, this.start + toIndex);
        }
        
        @Override
        public boolean equals(final Object object) {
            if (object == this) {
                return true;
            }
            if (!(object instanceof DoubleArrayAsList)) {
                return super.equals(object);
            }
            final DoubleArrayAsList that = (DoubleArrayAsList)object;
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
                result = 31 * result + Doubles.hashCode(this.array[i]);
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
        
        double[] toDoubleArray() {
            final int size = this.size();
            final double[] result = new double[size];
            System.arraycopy(this.array, this.start, result, 0, size);
            return result;
        }
    }
}
