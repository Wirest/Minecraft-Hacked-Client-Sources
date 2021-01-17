// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.mutable;

import java.io.Serializable;

public class MutableBoolean implements Mutable<Boolean>, Serializable, Comparable<MutableBoolean>
{
    private static final long serialVersionUID = -4830728138360036487L;
    private boolean value;
    
    public MutableBoolean() {
    }
    
    public MutableBoolean(final boolean value) {
        this.value = value;
    }
    
    public MutableBoolean(final Boolean value) {
        this.value = value;
    }
    
    @Override
    public Boolean getValue() {
        return this.value;
    }
    
    public void setValue(final boolean value) {
        this.value = value;
    }
    
    public void setFalse() {
        this.value = false;
    }
    
    public void setTrue() {
        this.value = true;
    }
    
    @Override
    public void setValue(final Boolean value) {
        this.value = value;
    }
    
    public boolean isTrue() {
        return this.value;
    }
    
    public boolean isFalse() {
        return !this.value;
    }
    
    public boolean booleanValue() {
        return this.value;
    }
    
    public Boolean toBoolean() {
        return this.booleanValue();
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof MutableBoolean && this.value == ((MutableBoolean)obj).booleanValue();
    }
    
    @Override
    public int hashCode() {
        return this.value ? Boolean.TRUE.hashCode() : Boolean.FALSE.hashCode();
    }
    
    @Override
    public int compareTo(final MutableBoolean other) {
        final boolean anotherVal = other.value;
        return (this.value == anotherVal) ? 0 : (this.value ? 1 : -1);
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.value);
    }
}
