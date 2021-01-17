// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson.internal;

import java.io.ObjectStreamException;
import java.math.BigDecimal;

public final class LazilyParsedNumber extends Number
{
    private final String value;
    
    public LazilyParsedNumber(final String value) {
        this.value = value;
    }
    
    @Override
    public int intValue() {
        try {
            return Integer.parseInt(this.value);
        }
        catch (NumberFormatException e) {
            try {
                return (int)Long.parseLong(this.value);
            }
            catch (NumberFormatException nfe) {
                return new BigDecimal(this.value).intValue();
            }
        }
    }
    
    @Override
    public long longValue() {
        try {
            return Long.parseLong(this.value);
        }
        catch (NumberFormatException e) {
            return new BigDecimal(this.value).longValue();
        }
    }
    
    @Override
    public float floatValue() {
        return Float.parseFloat(this.value);
    }
    
    @Override
    public double doubleValue() {
        return Double.parseDouble(this.value);
    }
    
    @Override
    public String toString() {
        return this.value;
    }
    
    private Object writeReplace() throws ObjectStreamException {
        return new BigDecimal(this.value);
    }
}
