package com.google.gson.internal;

import java.io.ObjectStreamException;
import java.math.BigDecimal;

public final class LazilyParsedNumber
        extends Number {
    private final String value;

    public LazilyParsedNumber(String paramString) {
        this.value = paramString;
    }

    public int intValue() {
        try {
            return Integer.parseInt(this.value);
        } catch (NumberFormatException localNumberFormatException1) {
            try {
                return (int) Long.parseLong(this.value);
            } catch (NumberFormatException localNumberFormatException2) {
            }
        }
        return new BigDecimal(this.value).intValue();
    }

    public long longValue() {
        try {
            return Long.parseLong(this.value);
        } catch (NumberFormatException localNumberFormatException) {
        }
        return new BigDecimal(this.value).longValue();
    }

    public float floatValue() {
        return Float.parseFloat(this.value);
    }

    public double doubleValue() {
        return Double.parseDouble(this.value);
    }

    public String toString() {
        return this.value;
    }

    private Object writeReplace()
            throws ObjectStreamException {
        return new BigDecimal(this.value);
    }
}




