package com.etb.client.utils.value.parse;

import com.google.common.primitives.Primitives;

public final class NumberCaster {

    public static <T extends Number, V extends Number> T cast(Class<T> numberClass, final V value) {
        numberClass = Primitives.wrap(numberClass);
        Object casted;
        if (numberClass == Byte.class) {
            casted = value.byteValue();
        } else if (numberClass == Short.class) {
            casted = value.shortValue();
        } else if (numberClass == Integer.class) {
            casted = value.intValue();
        } else if (numberClass == Long.class) {
            casted = value.longValue();
        } else if (numberClass == Float.class) {
            casted = value.floatValue();
        } else {
            if (numberClass != Double.class) {
                throw new ClassCastException(String.format("%s cannot be casted to %s", value.getClass(), numberClass));
            }
            casted = value.doubleValue();
        }
        return (T) casted;
    }

}