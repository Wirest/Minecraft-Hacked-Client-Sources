package com.sun.jna;

public abstract interface ToNativeConverter {
    public abstract Object toNative(Object paramObject, ToNativeContext paramToNativeContext);

    public abstract Class nativeType();
}




