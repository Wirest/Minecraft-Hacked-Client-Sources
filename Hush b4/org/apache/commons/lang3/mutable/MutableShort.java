// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.mutable;

public class MutableShort extends Number implements Comparable<MutableShort>, Mutable<Number>
{
    private static final long serialVersionUID = -2135791679L;
    private short value;
    
    public MutableShort() {
    }
    
    public MutableShort(final short value) {
        this.value = value;
    }
    
    public MutableShort(final Number value) {
        this.value = value.shortValue();
    }
    
    public MutableShort(final String value) throws NumberFormatException {
        this.value = Short.parseShort(value);
    }
    
    @Override
    public Short getValue() {
        return this.value;
    }
    
    public void setValue(final short value) {
        this.value = value;
    }
    
    @Override
    public void setValue(final Number value) {
        this.value = value.shortValue();
    }
    
    public void increment() {
        ++this.value;
    }
    
    public void decrement() {
        --this.value;
    }
    
    public void add(final short operand) {
        this.value += operand;
    }
    
    public void add(final Number operand) {
        this.value += operand.shortValue();
    }
    
    public void subtract(final short operand) {
        this.value -= operand;
    }
    
    public void subtract(final Number operand) {
        this.value -= operand.shortValue();
    }
    
    @Override
    public short shortValue() {
        return this.value;
    }
    
    @Override
    public int intValue() {
        return this.value;
    }
    
    @Override
    public long longValue() {
        return this.value;
    }
    
    @Override
    public float floatValue() {
        return this.value;
    }
    
    @Override
    public double doubleValue() {
        return this.value;
    }
    
    public Short toShort() {
        return this.shortValue();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof MutableShort && this.value == ((MutableShort)obj).shortValue();
    }
    
    @Override
    public int hashCode() {
        return this.value;
    }
    
    @Override
    public int compareTo(final MutableShort other) {
        final short anotherVal = other.value;
        return (this.value < anotherVal) ? -1 : ((this.value == anotherVal) ? 0 : 1);
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
