// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

public class NativeLong extends IntegerType
{
    public static final int SIZE;
    
    public NativeLong() {
        this(0L);
    }
    
    public NativeLong(final long value) {
        super(NativeLong.SIZE, value);
    }
    
    static {
        SIZE = Native.LONG_SIZE;
    }
}
