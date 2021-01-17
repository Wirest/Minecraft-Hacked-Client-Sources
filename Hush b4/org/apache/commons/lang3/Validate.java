// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3;

import java.util.regex.Pattern;
import java.util.Iterator;
import java.util.Map;
import java.util.Collection;

public class Validate
{
    private static final String DEFAULT_EXCLUSIVE_BETWEEN_EX_MESSAGE = "The value %s is not in the specified exclusive range of %s to %s";
    private static final String DEFAULT_INCLUSIVE_BETWEEN_EX_MESSAGE = "The value %s is not in the specified inclusive range of %s to %s";
    private static final String DEFAULT_MATCHES_PATTERN_EX = "The string %s does not match the pattern %s";
    private static final String DEFAULT_IS_NULL_EX_MESSAGE = "The validated object is null";
    private static final String DEFAULT_IS_TRUE_EX_MESSAGE = "The validated expression is false";
    private static final String DEFAULT_NO_NULL_ELEMENTS_ARRAY_EX_MESSAGE = "The validated array contains null element at index: %d";
    private static final String DEFAULT_NO_NULL_ELEMENTS_COLLECTION_EX_MESSAGE = "The validated collection contains null element at index: %d";
    private static final String DEFAULT_NOT_BLANK_EX_MESSAGE = "The validated character sequence is blank";
    private static final String DEFAULT_NOT_EMPTY_ARRAY_EX_MESSAGE = "The validated array is empty";
    private static final String DEFAULT_NOT_EMPTY_CHAR_SEQUENCE_EX_MESSAGE = "The validated character sequence is empty";
    private static final String DEFAULT_NOT_EMPTY_COLLECTION_EX_MESSAGE = "The validated collection is empty";
    private static final String DEFAULT_NOT_EMPTY_MAP_EX_MESSAGE = "The validated map is empty";
    private static final String DEFAULT_VALID_INDEX_ARRAY_EX_MESSAGE = "The validated array index is invalid: %d";
    private static final String DEFAULT_VALID_INDEX_CHAR_SEQUENCE_EX_MESSAGE = "The validated character sequence index is invalid: %d";
    private static final String DEFAULT_VALID_INDEX_COLLECTION_EX_MESSAGE = "The validated collection index is invalid: %d";
    private static final String DEFAULT_VALID_STATE_EX_MESSAGE = "The validated state is false";
    private static final String DEFAULT_IS_ASSIGNABLE_EX_MESSAGE = "Cannot assign a %s to a %s";
    private static final String DEFAULT_IS_INSTANCE_OF_EX_MESSAGE = "Expected type: %s, actual: %s";
    
    public static void isTrue(final boolean expression, final String message, final long value) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, value));
        }
    }
    
    public static void isTrue(final boolean expression, final String message, final double value) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, value));
        }
    }
    
    public static void isTrue(final boolean expression, final String message, final Object... values) {
        if (!expression) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }
    
    public static void isTrue(final boolean expression) {
        if (!expression) {
            throw new IllegalArgumentException("The validated expression is false");
        }
    }
    
    public static <T> T notNull(final T object) {
        return notNull(object, "The validated object is null", new Object[0]);
    }
    
    public static <T> T notNull(final T object, final String message, final Object... values) {
        if (object == null) {
            throw new NullPointerException(String.format(message, values));
        }
        return object;
    }
    
    public static <T> T[] notEmpty(final T[] array, final String message, final Object... values) {
        if (array == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (array.length == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return array;
    }
    
    public static <T> T[] notEmpty(final T[] array) {
        return notEmpty(array, "The validated array is empty", new Object[0]);
    }
    
    public static <T extends Collection<?>> T notEmpty(final T collection, final String message, final Object... values) {
        if (collection == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return collection;
    }
    
    public static <T extends Collection<?>> T notEmpty(final T collection) {
        return notEmpty(collection, "The validated collection is empty", new Object[0]);
    }
    
    public static <T extends Map<?, ?>> T notEmpty(final T map, final String message, final Object... values) {
        if (map == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (map.isEmpty()) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return map;
    }
    
    public static <T extends Map<?, ?>> T notEmpty(final T map) {
        return notEmpty(map, "The validated map is empty", new Object[0]);
    }
    
    public static <T extends CharSequence> T notEmpty(final T chars, final String message, final Object... values) {
        if (chars == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (chars.length() == 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return chars;
    }
    
    public static <T extends CharSequence> T notEmpty(final T chars) {
        return notEmpty(chars, "The validated character sequence is empty", new Object[0]);
    }
    
    public static <T extends CharSequence> T notBlank(final T chars, final String message, final Object... values) {
        if (chars == null) {
            throw new NullPointerException(String.format(message, values));
        }
        if (StringUtils.isBlank(chars)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
        return chars;
    }
    
    public static <T extends CharSequence> T notBlank(final T chars) {
        return notBlank(chars, "The validated character sequence is blank", new Object[0]);
    }
    
    public static <T> T[] noNullElements(final T[] array, final String message, final Object... values) {
        notNull(array);
        for (int i = 0; i < array.length; ++i) {
            if (array[i] == null) {
                final Object[] values2 = ArrayUtils.add(values, i);
                throw new IllegalArgumentException(String.format(message, values2));
            }
        }
        return array;
    }
    
    public static <T> T[] noNullElements(final T[] array) {
        return noNullElements(array, "The validated array contains null element at index: %d", new Object[0]);
    }
    
    public static <T extends Iterable<?>> T noNullElements(final T iterable, final String message, final Object... values) {
        notNull(iterable);
        int i = 0;
        final Iterator<?> it = iterable.iterator();
        while (it.hasNext()) {
            if (it.next() == null) {
                final Object[] values2 = ArrayUtils.addAll(values, i);
                throw new IllegalArgumentException(String.format(message, values2));
            }
            ++i;
        }
        return iterable;
    }
    
    public static <T extends Iterable<?>> T noNullElements(final T iterable) {
        return noNullElements(iterable, "The validated collection contains null element at index: %d", new Object[0]);
    }
    
    public static <T> T[] validIndex(final T[] array, final int index, final String message, final Object... values) {
        notNull(array);
        if (index < 0 || index >= array.length) {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
        return array;
    }
    
    public static <T> T[] validIndex(final T[] array, final int index) {
        return validIndex(array, index, "The validated array index is invalid: %d", new Object[] { index });
    }
    
    public static <T extends Collection<?>> T validIndex(final T collection, final int index, final String message, final Object... values) {
        notNull(collection);
        if (index < 0 || index >= collection.size()) {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
        return collection;
    }
    
    public static <T extends Collection<?>> T validIndex(final T collection, final int index) {
        return validIndex(collection, index, "The validated collection index is invalid: %d", new Object[] { index });
    }
    
    public static <T extends CharSequence> T validIndex(final T chars, final int index, final String message, final Object... values) {
        notNull(chars);
        if (index < 0 || index >= chars.length()) {
            throw new IndexOutOfBoundsException(String.format(message, values));
        }
        return chars;
    }
    
    public static <T extends CharSequence> T validIndex(final T chars, final int index) {
        return validIndex(chars, index, "The validated character sequence index is invalid: %d", new Object[] { index });
    }
    
    public static void validState(final boolean expression) {
        if (!expression) {
            throw new IllegalStateException("The validated state is false");
        }
    }
    
    public static void validState(final boolean expression, final String message, final Object... values) {
        if (!expression) {
            throw new IllegalStateException(String.format(message, values));
        }
    }
    
    public static void matchesPattern(final CharSequence input, final String pattern) {
        if (!Pattern.matches(pattern, input)) {
            throw new IllegalArgumentException(String.format("The string %s does not match the pattern %s", input, pattern));
        }
    }
    
    public static void matchesPattern(final CharSequence input, final String pattern, final String message, final Object... values) {
        if (!Pattern.matches(pattern, input)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }
    
    public static <T> void inclusiveBetween(final T start, final T end, final Comparable<T> value) {
        if (value.compareTo(start) < 0 || value.compareTo(end) > 0) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified inclusive range of %s to %s", value, start, end));
        }
    }
    
    public static <T> void inclusiveBetween(final T start, final T end, final Comparable<T> value, final String message, final Object... values) {
        if (value.compareTo(start) < 0 || value.compareTo(end) > 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }
    
    public static void inclusiveBetween(final long start, final long end, final long value) {
        if (value < start || value > end) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified inclusive range of %s to %s", value, start, end));
        }
    }
    
    public static void inclusiveBetween(final long start, final long end, final long value, final String message) {
        if (value < start || value > end) {
            throw new IllegalArgumentException(String.format(message, new Object[0]));
        }
    }
    
    public static void inclusiveBetween(final double start, final double end, final double value) {
        if (value < start || value > end) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified inclusive range of %s to %s", value, start, end));
        }
    }
    
    public static void inclusiveBetween(final double start, final double end, final double value, final String message) {
        if (value < start || value > end) {
            throw new IllegalArgumentException(String.format(message, new Object[0]));
        }
    }
    
    public static <T> void exclusiveBetween(final T start, final T end, final Comparable<T> value) {
        if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified exclusive range of %s to %s", value, start, end));
        }
    }
    
    public static <T> void exclusiveBetween(final T start, final T end, final Comparable<T> value, final String message, final Object... values) {
        if (value.compareTo(start) <= 0 || value.compareTo(end) >= 0) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }
    
    public static void exclusiveBetween(final long start, final long end, final long value) {
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified exclusive range of %s to %s", value, start, end));
        }
    }
    
    public static void exclusiveBetween(final long start, final long end, final long value, final String message) {
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(String.format(message, new Object[0]));
        }
    }
    
    public static void exclusiveBetween(final double start, final double end, final double value) {
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(String.format("The value %s is not in the specified exclusive range of %s to %s", value, start, end));
        }
    }
    
    public static void exclusiveBetween(final double start, final double end, final double value, final String message) {
        if (value <= start || value >= end) {
            throw new IllegalArgumentException(String.format(message, new Object[0]));
        }
    }
    
    public static void isInstanceOf(final Class<?> type, final Object obj) {
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(String.format("Expected type: %s, actual: %s", type.getName(), (obj == null) ? "null" : obj.getClass().getName()));
        }
    }
    
    public static void isInstanceOf(final Class<?> type, final Object obj, final String message, final Object... values) {
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }
    
    public static void isAssignableFrom(final Class<?> superType, final Class<?> type) {
        if (!superType.isAssignableFrom(type)) {
            throw new IllegalArgumentException(String.format("Cannot assign a %s to a %s", (type == null) ? "null" : type.getName(), superType.getName()));
        }
    }
    
    public static void isAssignableFrom(final Class<?> superType, final Class<?> type, final String message, final Object... values) {
        if (!superType.isAssignableFrom(type)) {
            throw new IllegalArgumentException(String.format(message, values));
        }
    }
}
