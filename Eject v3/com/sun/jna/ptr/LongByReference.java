package com.sun.jna.ptr;

public class LongByReference
        extends ByReference {
    public LongByReference() {
        this(0L);
    }

    public LongByReference(long paramLong) {
        super(8);
        setValue(paramLong);
    }

    public long getValue() {
        return getPointer().getLong(0L);
    }

    public void setValue(long paramLong) {
        getPointer().setLong(0L, paramLong);
    }
}




