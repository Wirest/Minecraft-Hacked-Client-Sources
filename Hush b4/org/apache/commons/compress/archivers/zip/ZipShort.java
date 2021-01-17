// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.compress.archivers.zip;

import java.io.Serializable;

public final class ZipShort implements Cloneable, Serializable
{
    private static final long serialVersionUID = 1L;
    private static final int BYTE_1_MASK = 65280;
    private static final int BYTE_1_SHIFT = 8;
    private final int value;
    
    public ZipShort(final int value) {
        this.value = value;
    }
    
    public ZipShort(final byte[] bytes) {
        this(bytes, 0);
    }
    
    public ZipShort(final byte[] bytes, final int offset) {
        this.value = getValue(bytes, offset);
    }
    
    public byte[] getBytes() {
        final byte[] result = { (byte)(this.value & 0xFF), (byte)((this.value & 0xFF00) >> 8) };
        return result;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public static byte[] getBytes(final int value) {
        final byte[] result = { (byte)(value & 0xFF), (byte)((value & 0xFF00) >> 8) };
        return result;
    }
    
    public static int getValue(final byte[] bytes, final int offset) {
        int value = bytes[offset + 1] << 8 & 0xFF00;
        value += (bytes[offset] & 0xFF);
        return value;
    }
    
    public static int getValue(final byte[] bytes) {
        return getValue(bytes, 0);
    }
    
    @Override
    public boolean equals(final Object o) {
        return o != null && o instanceof ZipShort && this.value == ((ZipShort)o).getValue();
    }
    
    @Override
    public int hashCode() {
        return this.value;
    }
    
    public Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException cnfe) {
            throw new RuntimeException(cnfe);
        }
    }
    
    @Override
    public String toString() {
        return "ZipShort value: " + this.value;
    }
}
