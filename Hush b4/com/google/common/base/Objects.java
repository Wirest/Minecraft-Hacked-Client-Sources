// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.util.Arrays;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class Objects
{
    private Objects() {
    }
    
    @CheckReturnValue
    public static boolean equal(@Nullable final Object a, @Nullable final Object b) {
        return a == b || (a != null && a.equals(b));
    }
    
    public static int hashCode(@Nullable final Object... objects) {
        return Arrays.hashCode(objects);
    }
    
    public static ToStringHelper toStringHelper(final Object self) {
        return new ToStringHelper(simpleName(self.getClass()));
    }
    
    public static ToStringHelper toStringHelper(final Class<?> clazz) {
        return new ToStringHelper(simpleName(clazz));
    }
    
    public static ToStringHelper toStringHelper(final String className) {
        return new ToStringHelper(className);
    }
    
    private static String simpleName(final Class<?> clazz) {
        String name = clazz.getName();
        name = name.replaceAll("\\$[0-9]+", "\\$");
        int start = name.lastIndexOf(36);
        if (start == -1) {
            start = name.lastIndexOf(46);
        }
        return name.substring(start + 1);
    }
    
    public static <T> T firstNonNull(@Nullable final T first, @Nullable final T second) {
        return (first != null) ? first : Preconditions.checkNotNull(second);
    }
    
    public static final class ToStringHelper
    {
        private final String className;
        private ValueHolder holderHead;
        private ValueHolder holderTail;
        private boolean omitNullValues;
        
        private ToStringHelper(final String className) {
            this.holderHead = new ValueHolder();
            this.holderTail = this.holderHead;
            this.omitNullValues = false;
            this.className = Preconditions.checkNotNull(className);
        }
        
        public ToStringHelper omitNullValues() {
            this.omitNullValues = true;
            return this;
        }
        
        public ToStringHelper add(final String name, @Nullable final Object value) {
            return this.addHolder(name, value);
        }
        
        public ToStringHelper add(final String name, final boolean value) {
            return this.addHolder(name, String.valueOf(value));
        }
        
        public ToStringHelper add(final String name, final char value) {
            return this.addHolder(name, String.valueOf(value));
        }
        
        public ToStringHelper add(final String name, final double value) {
            return this.addHolder(name, String.valueOf(value));
        }
        
        public ToStringHelper add(final String name, final float value) {
            return this.addHolder(name, String.valueOf(value));
        }
        
        public ToStringHelper add(final String name, final int value) {
            return this.addHolder(name, String.valueOf(value));
        }
        
        public ToStringHelper add(final String name, final long value) {
            return this.addHolder(name, String.valueOf(value));
        }
        
        public ToStringHelper addValue(@Nullable final Object value) {
            return this.addHolder(value);
        }
        
        public ToStringHelper addValue(final boolean value) {
            return this.addHolder(String.valueOf(value));
        }
        
        public ToStringHelper addValue(final char value) {
            return this.addHolder(String.valueOf(value));
        }
        
        public ToStringHelper addValue(final double value) {
            return this.addHolder(String.valueOf(value));
        }
        
        public ToStringHelper addValue(final float value) {
            return this.addHolder(String.valueOf(value));
        }
        
        public ToStringHelper addValue(final int value) {
            return this.addHolder(String.valueOf(value));
        }
        
        public ToStringHelper addValue(final long value) {
            return this.addHolder(String.valueOf(value));
        }
        
        @Override
        public String toString() {
            final boolean omitNullValuesSnapshot = this.omitNullValues;
            String nextSeparator = "";
            final StringBuilder builder = new StringBuilder(32).append(this.className).append('{');
            for (ValueHolder valueHolder = this.holderHead.next; valueHolder != null; valueHolder = valueHolder.next) {
                if (!omitNullValuesSnapshot || valueHolder.value != null) {
                    builder.append(nextSeparator);
                    nextSeparator = ", ";
                    if (valueHolder.name != null) {
                        builder.append(valueHolder.name).append('=');
                    }
                    builder.append(valueHolder.value);
                }
            }
            return builder.append('}').toString();
        }
        
        private ValueHolder addHolder() {
            final ValueHolder valueHolder = new ValueHolder();
            final ValueHolder holderTail = this.holderTail;
            final ValueHolder valueHolder2 = valueHolder;
            holderTail.next = valueHolder2;
            this.holderTail = valueHolder2;
            return valueHolder;
        }
        
        private ToStringHelper addHolder(@Nullable final Object value) {
            final ValueHolder valueHolder = this.addHolder();
            valueHolder.value = value;
            return this;
        }
        
        private ToStringHelper addHolder(final String name, @Nullable final Object value) {
            final ValueHolder valueHolder = this.addHolder();
            valueHolder.value = value;
            valueHolder.name = Preconditions.checkNotNull(name);
            return this;
        }
        
        private static final class ValueHolder
        {
            String name;
            Object value;
            ValueHolder next;
        }
    }
}
