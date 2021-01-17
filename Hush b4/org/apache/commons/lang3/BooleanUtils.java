// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3;

import org.apache.commons.lang3.math.NumberUtils;

public class BooleanUtils
{
    public static Boolean negate(final Boolean bool) {
        if (bool == null) {
            return null;
        }
        return ((boolean)bool) ? Boolean.FALSE : Boolean.TRUE;
    }
    
    public static boolean isTrue(final Boolean bool) {
        return Boolean.TRUE.equals(bool);
    }
    
    public static boolean isNotTrue(final Boolean bool) {
        return !isTrue(bool);
    }
    
    public static boolean isFalse(final Boolean bool) {
        return Boolean.FALSE.equals(bool);
    }
    
    public static boolean isNotFalse(final Boolean bool) {
        return !isFalse(bool);
    }
    
    public static boolean toBoolean(final Boolean bool) {
        return bool != null && bool;
    }
    
    public static boolean toBooleanDefaultIfNull(final Boolean bool, final boolean valueIfNull) {
        if (bool == null) {
            return valueIfNull;
        }
        return bool;
    }
    
    public static boolean toBoolean(final int value) {
        return value != 0;
    }
    
    public static Boolean toBooleanObject(final int value) {
        return (value == 0) ? Boolean.FALSE : Boolean.TRUE;
    }
    
    public static Boolean toBooleanObject(final Integer value) {
        if (value == null) {
            return null;
        }
        return (value == 0) ? Boolean.FALSE : Boolean.TRUE;
    }
    
    public static boolean toBoolean(final int value, final int trueValue, final int falseValue) {
        if (value == trueValue) {
            return true;
        }
        if (value == falseValue) {
            return false;
        }
        throw new IllegalArgumentException("The Integer did not match either specified value");
    }
    
    public static boolean toBoolean(final Integer value, final Integer trueValue, final Integer falseValue) {
        if (value == null) {
            if (trueValue == null) {
                return true;
            }
            if (falseValue == null) {
                return false;
            }
        }
        else {
            if (value.equals(trueValue)) {
                return true;
            }
            if (value.equals(falseValue)) {
                return false;
            }
        }
        throw new IllegalArgumentException("The Integer did not match either specified value");
    }
    
    public static Boolean toBooleanObject(final int value, final int trueValue, final int falseValue, final int nullValue) {
        if (value == trueValue) {
            return Boolean.TRUE;
        }
        if (value == falseValue) {
            return Boolean.FALSE;
        }
        if (value == nullValue) {
            return null;
        }
        throw new IllegalArgumentException("The Integer did not match any specified value");
    }
    
    public static Boolean toBooleanObject(final Integer value, final Integer trueValue, final Integer falseValue, final Integer nullValue) {
        if (value == null) {
            if (trueValue == null) {
                return Boolean.TRUE;
            }
            if (falseValue == null) {
                return Boolean.FALSE;
            }
            if (nullValue == null) {
                return null;
            }
        }
        else {
            if (value.equals(trueValue)) {
                return Boolean.TRUE;
            }
            if (value.equals(falseValue)) {
                return Boolean.FALSE;
            }
            if (value.equals(nullValue)) {
                return null;
            }
        }
        throw new IllegalArgumentException("The Integer did not match any specified value");
    }
    
    public static int toInteger(final boolean bool) {
        return bool ? 1 : 0;
    }
    
    public static Integer toIntegerObject(final boolean bool) {
        return bool ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO;
    }
    
    public static Integer toIntegerObject(final Boolean bool) {
        if (bool == null) {
            return null;
        }
        return bool ? NumberUtils.INTEGER_ONE : NumberUtils.INTEGER_ZERO;
    }
    
    public static int toInteger(final boolean bool, final int trueValue, final int falseValue) {
        return bool ? trueValue : falseValue;
    }
    
    public static int toInteger(final Boolean bool, final int trueValue, final int falseValue, final int nullValue) {
        if (bool == null) {
            return nullValue;
        }
        return bool ? trueValue : falseValue;
    }
    
    public static Integer toIntegerObject(final boolean bool, final Integer trueValue, final Integer falseValue) {
        return bool ? trueValue : falseValue;
    }
    
    public static Integer toIntegerObject(final Boolean bool, final Integer trueValue, final Integer falseValue, final Integer nullValue) {
        if (bool == null) {
            return nullValue;
        }
        return bool ? trueValue : falseValue;
    }
    
    public static Boolean toBooleanObject(final String str) {
        if (str == "true") {
            return Boolean.TRUE;
        }
        if (str == null) {
            return null;
        }
        switch (str.length()) {
            case 1: {
                final char ch0 = str.charAt(0);
                if (ch0 == 'y' || ch0 == 'Y' || ch0 == 't' || ch0 == 'T') {
                    return Boolean.TRUE;
                }
                if (ch0 == 'n' || ch0 == 'N' || ch0 == 'f' || ch0 == 'F') {
                    return Boolean.FALSE;
                }
                break;
            }
            case 2: {
                final char ch0 = str.charAt(0);
                final char ch2 = str.charAt(1);
                if ((ch0 == 'o' || ch0 == 'O') && (ch2 == 'n' || ch2 == 'N')) {
                    return Boolean.TRUE;
                }
                if ((ch0 == 'n' || ch0 == 'N') && (ch2 == 'o' || ch2 == 'O')) {
                    return Boolean.FALSE;
                }
                break;
            }
            case 3: {
                final char ch0 = str.charAt(0);
                final char ch2 = str.charAt(1);
                final char ch3 = str.charAt(2);
                if ((ch0 == 'y' || ch0 == 'Y') && (ch2 == 'e' || ch2 == 'E') && (ch3 == 's' || ch3 == 'S')) {
                    return Boolean.TRUE;
                }
                if ((ch0 == 'o' || ch0 == 'O') && (ch2 == 'f' || ch2 == 'F') && (ch3 == 'f' || ch3 == 'F')) {
                    return Boolean.FALSE;
                }
                break;
            }
            case 4: {
                final char ch0 = str.charAt(0);
                final char ch2 = str.charAt(1);
                final char ch3 = str.charAt(2);
                final char ch4 = str.charAt(3);
                if ((ch0 == 't' || ch0 == 'T') && (ch2 == 'r' || ch2 == 'R') && (ch3 == 'u' || ch3 == 'U') && (ch4 == 'e' || ch4 == 'E')) {
                    return Boolean.TRUE;
                }
                break;
            }
            case 5: {
                final char ch0 = str.charAt(0);
                final char ch2 = str.charAt(1);
                final char ch3 = str.charAt(2);
                final char ch4 = str.charAt(3);
                final char ch5 = str.charAt(4);
                if ((ch0 == 'f' || ch0 == 'F') && (ch2 == 'a' || ch2 == 'A') && (ch3 == 'l' || ch3 == 'L') && (ch4 == 's' || ch4 == 'S') && (ch5 == 'e' || ch5 == 'E')) {
                    return Boolean.FALSE;
                }
                break;
            }
        }
        return null;
    }
    
    public static Boolean toBooleanObject(final String str, final String trueString, final String falseString, final String nullString) {
        if (str == null) {
            if (trueString == null) {
                return Boolean.TRUE;
            }
            if (falseString == null) {
                return Boolean.FALSE;
            }
            if (nullString == null) {
                return null;
            }
        }
        else {
            if (str.equals(trueString)) {
                return Boolean.TRUE;
            }
            if (str.equals(falseString)) {
                return Boolean.FALSE;
            }
            if (str.equals(nullString)) {
                return null;
            }
        }
        throw new IllegalArgumentException("The String did not match any specified value");
    }
    
    public static boolean toBoolean(final String str) {
        return toBooleanObject(str) == Boolean.TRUE;
    }
    
    public static boolean toBoolean(final String str, final String trueString, final String falseString) {
        if (str == trueString) {
            return true;
        }
        if (str == falseString) {
            return false;
        }
        if (str != null) {
            if (str.equals(trueString)) {
                return true;
            }
            if (str.equals(falseString)) {
                return false;
            }
        }
        throw new IllegalArgumentException("The String did not match either specified value");
    }
    
    public static String toStringTrueFalse(final Boolean bool) {
        return toString(bool, "true", "false", null);
    }
    
    public static String toStringOnOff(final Boolean bool) {
        return toString(bool, "on", "off", null);
    }
    
    public static String toStringYesNo(final Boolean bool) {
        return toString(bool, "yes", "no", null);
    }
    
    public static String toString(final Boolean bool, final String trueString, final String falseString, final String nullString) {
        if (bool == null) {
            return nullString;
        }
        return bool ? trueString : falseString;
    }
    
    public static String toStringTrueFalse(final boolean bool) {
        return toString(bool, "true", "false");
    }
    
    public static String toStringOnOff(final boolean bool) {
        return toString(bool, "on", "off");
    }
    
    public static String toStringYesNo(final boolean bool) {
        return toString(bool, "yes", "no");
    }
    
    public static String toString(final boolean bool, final String trueString, final String falseString) {
        return bool ? trueString : falseString;
    }
    
    public static boolean and(final boolean... array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        for (final boolean element : array) {
            if (!element) {
                return false;
            }
        }
        return true;
    }
    
    public static Boolean and(final Boolean... array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        try {
            final boolean[] primitive = ArrayUtils.toPrimitive(array);
            return and(primitive) ? Boolean.TRUE : Boolean.FALSE;
        }
        catch (NullPointerException ex) {
            throw new IllegalArgumentException("The array must not contain any null elements");
        }
    }
    
    public static boolean or(final boolean... array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        for (final boolean element : array) {
            if (element) {
                return true;
            }
        }
        return false;
    }
    
    public static Boolean or(final Boolean... array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        try {
            final boolean[] primitive = ArrayUtils.toPrimitive(array);
            return or(primitive) ? Boolean.TRUE : Boolean.FALSE;
        }
        catch (NullPointerException ex) {
            throw new IllegalArgumentException("The array must not contain any null elements");
        }
    }
    
    public static boolean xor(final boolean... array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        boolean result = false;
        for (final boolean element : array) {
            result ^= element;
        }
        return result;
    }
    
    public static Boolean xor(final Boolean... array) {
        if (array == null) {
            throw new IllegalArgumentException("The Array must not be null");
        }
        if (array.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        try {
            final boolean[] primitive = ArrayUtils.toPrimitive(array);
            return xor(primitive) ? Boolean.TRUE : Boolean.FALSE;
        }
        catch (NullPointerException ex) {
            throw new IllegalArgumentException("The array must not contain any null elements");
        }
    }
}
