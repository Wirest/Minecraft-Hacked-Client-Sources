package com.sun.jna.ptr;

import com.sun.jna.Pointer;

public class PointerByReference
        extends ByReference {
    public PointerByReference() {
        this(null);
    }

    public PointerByReference(Pointer paramPointer) {
        super(Pointer.SIZE);
        setValue(paramPointer);
    }

    public Pointer getValue() {
        return getPointer().getPointer(0L);
    }

    public void setValue(Pointer paramPointer) {
        getPointer().setPointer(0L, paramPointer);
    }
}




