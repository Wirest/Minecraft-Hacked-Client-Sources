package com.sun.jna.ptr;

public class IntByReference
        extends ByReference {
    public IntByReference() {
        this(0);
    }

    public IntByReference(int paramInt) {
        super(4);
        setValue(paramInt);
    }

    public int getValue() {
        return getPointer().getInt(0L);
    }

    public void setValue(int paramInt) {
        getPointer().setInt(0L, paramInt);
    }
}




