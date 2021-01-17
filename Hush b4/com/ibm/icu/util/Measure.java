// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.util;

public abstract class Measure
{
    private Number number;
    private MeasureUnit unit;
    
    protected Measure(final Number number, final MeasureUnit unit) {
        if (number == null || unit == null) {
            throw new NullPointerException();
        }
        this.number = number;
        this.unit = unit;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        try {
            final Measure m = (Measure)obj;
            return this.unit.equals(m.unit) && numbersEqual(this.number, m.number);
        }
        catch (ClassCastException e) {
            return false;
        }
    }
    
    private static boolean numbersEqual(final Number a, final Number b) {
        return a.equals(b) || a.doubleValue() == b.doubleValue();
    }
    
    @Override
    public int hashCode() {
        return this.number.hashCode() ^ this.unit.hashCode();
    }
    
    @Override
    public String toString() {
        return this.number.toString() + ' ' + this.unit.toString();
    }
    
    public Number getNumber() {
        return this.number;
    }
    
    public MeasureUnit getUnit() {
        return this.unit;
    }
}
