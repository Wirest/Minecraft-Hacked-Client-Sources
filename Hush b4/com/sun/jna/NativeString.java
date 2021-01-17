// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.nio.CharBuffer;

class NativeString implements CharSequence, Comparable
{
    private Pointer pointer;
    private boolean wide;
    
    public NativeString(final String string) {
        this(string, false);
    }
    
    public NativeString(final String string, final boolean wide) {
        if (string == null) {
            throw new NullPointerException("String must not be null");
        }
        this.wide = wide;
        if (wide) {
            final int len = (string.length() + 1) * Native.WCHAR_SIZE;
            (this.pointer = new Memory(len)).setString(0L, string, true);
        }
        else {
            final byte[] data = Native.getBytes(string);
            (this.pointer = new Memory(data.length + 1)).write(0L, data, 0, data.length);
            this.pointer.setByte(data.length, (byte)0);
        }
    }
    
    public int hashCode() {
        return this.toString().hashCode();
    }
    
    public boolean equals(final Object other) {
        return other instanceof CharSequence && this.compareTo(other) == 0;
    }
    
    public String toString() {
        String s = this.wide ? "const wchar_t*" : "const char*";
        s = s + "(" + this.pointer.getString(0L, this.wide) + ")";
        return s;
    }
    
    public Pointer getPointer() {
        return this.pointer;
    }
    
    public char charAt(final int index) {
        return this.toString().charAt(index);
    }
    
    public int length() {
        return this.toString().length();
    }
    
    public CharSequence subSequence(final int start, final int end) {
        return CharBuffer.wrap(this.toString()).subSequence(start, end);
    }
    
    public int compareTo(final Object other) {
        if (other == null) {
            return 1;
        }
        return this.toString().compareTo(other.toString());
    }
}
