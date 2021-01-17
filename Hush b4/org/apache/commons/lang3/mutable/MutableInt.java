// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.mutable;

public class MutableInt extends Number implements Comparable<MutableInt>, Mutable<Number>
{
    private static final long serialVersionUID = 512176391864L;
    private int value;
    
    public MutableInt() {
    }
    
    public MutableInt(final int value) {
        this.value = value;
    }
    
    public MutableInt(final Number value) {
        this.value = value.intValue();
    }
    
    public MutableInt(final String value) throws NumberFormatException {
        this.value = Integer.parseInt(value);
    }
    
    @Override
    public Integer getValue() {
        return this.value;
    }
    
    public void setValue(final int value) {
        this.value = value;
    }
    
    @Override
    public void setValue(final Number value) {
        this.value = value.intValue();
    }
    
    public void increment() {
        ++this.value;
    }
    
    public void decrement() {
        --this.value;
    }
    
    public void add(final int operand) {
        this.value += operand;
    }
    
    public void add(final Number operand) {
        this.value += operand.intValue();
    }
    
    public void subtract(final int operand) {
        this.value -= operand;
    }
    
    public void subtract(final Number operand) {
        this.value -= operand.intValue();
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
        return (float)this.value;
    }
    
    @Override
    public double doubleValue() {
        return this.value;
    }
    
    public Integer toInteger() {
        return this.intValue();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof MutableInt && this.value == ((MutableInt)obj).intValue();
    }
    
    @Override
    public int hashCode() {
        return this.value;
    }
    
    @Override
    public int compareTo(final MutableInt other) {
        final int anotherVal = other.value;
        return (this.value < anotherVal) ? -1 : ((this.value == anotherVal) ? 0 : 1);
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
