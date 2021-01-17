// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.mutable;

public class MutableLong extends Number implements Comparable<MutableLong>, Mutable<Number>
{
    private static final long serialVersionUID = 62986528375L;
    private long value;
    
    public MutableLong() {
    }
    
    public MutableLong(final long value) {
        this.value = value;
    }
    
    public MutableLong(final Number value) {
        this.value = value.longValue();
    }
    
    public MutableLong(final String value) throws NumberFormatException {
        this.value = Long.parseLong(value);
    }
    
    @Override
    public Long getValue() {
        return this.value;
    }
    
    public void setValue(final long value) {
        this.value = value;
    }
    
    @Override
    public void setValue(final Number value) {
        this.value = value.longValue();
    }
    
    public void increment() {
        ++this.value;
    }
    
    public void decrement() {
        --this.value;
    }
    
    public void add(final long operand) {
        this.value += operand;
    }
    
    public void add(final Number operand) {
        this.value += operand.longValue();
    }
    
    public void subtract(final long operand) {
        this.value -= operand;
    }
    
    public void subtract(final Number operand) {
        this.value -= operand.longValue();
    }
    
    @Override
    public int intValue() {
        return (int)this.value;
    }
    
    @Override
    public long longValue() {
        return this.value;
    }
    
    @Override
    public float floatValue() {
        return (float)this.value;
    }
    
    @Override
    public double doubleValue() {
        return (double)this.value;
    }
    
    public Long toLong() {
        return this.longValue();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof MutableLong && this.value == ((MutableLong)obj).longValue();
    }
    
    @Override
    public int hashCode() {
        return (int)(this.value ^ this.value >>> 32);
    }
    
    @Override
    public int compareTo(final MutableLong other) {
        final long anotherVal = other.value;
        return (this.value < anotherVal) ? -1 : ((this.value == anotherVal) ? 0 : 1);
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
