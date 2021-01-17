// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.mutable;

public class MutableFloat extends Number implements Comparable<MutableFloat>, Mutable<Number>
{
    private static final long serialVersionUID = 5787169186L;
    private float value;
    
    public MutableFloat() {
    }
    
    public MutableFloat(final float value) {
        this.value = value;
    }
    
    public MutableFloat(final Number value) {
        this.value = value.floatValue();
    }
    
    public MutableFloat(final String value) throws NumberFormatException {
        this.value = Float.parseFloat(value);
    }
    
    @Override
    public Float getValue() {
        return this.value;
    }
    
    public void setValue(final float value) {
        this.value = value;
    }
    
    @Override
    public void setValue(final Number value) {
        this.value = value.floatValue();
    }
    
    public boolean isNaN() {
        return Float.isNaN(this.value);
    }
    
    public boolean isInfinite() {
        return Float.isInfinite(this.value);
    }
    
    public void increment() {
        ++this.value;
    }
    
    public void decrement() {
        --this.value;
    }
    
    public void add(final float operand) {
        this.value += operand;
    }
    
    public void add(final Number operand) {
        this.value += operand.floatValue();
    }
    
    public void subtract(final float operand) {
        this.value -= operand;
    }
    
    public void subtract(final Number operand) {
        this.value -= operand.floatValue();
    }
    
    @Override
    public int intValue() {
        return (int)this.value;
    }
    
    @Override
    public long longValue() {
        return (long)this.value;
    }
    
    @Override
    public float floatValue() {
        return this.value;
    }
    
    @Override
    public double doubleValue() {
        return this.value;
    }
    
    public Float toFloat() {
        return this.floatValue();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof MutableFloat && Float.floatToIntBits(((MutableFloat)obj).value) == Float.floatToIntBits(this.value);
    }
    
    @Override
    public int hashCode() {
        return Float.floatToIntBits(this.value);
    }
    
    @Override
    public int compareTo(final MutableFloat other) {
        final float anotherVal = other.value;
        return Float.compare(this.value, anotherVal);
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
