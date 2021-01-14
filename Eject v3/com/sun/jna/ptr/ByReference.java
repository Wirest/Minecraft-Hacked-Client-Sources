package com.sun.jna.ptr;

import com.sun.jna.Memory;
import com.sun.jna.PointerType;

public abstract class ByReference
        extends PointerType {
    protected ByReference(int paramInt) {
        setPointer(new Memory(paramInt));
    }
}




