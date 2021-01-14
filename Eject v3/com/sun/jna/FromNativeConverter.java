package com.sun.jna;

public abstract interface FromNativeConverter {
    public abstract Object fromNative(Object paramObject, FromNativeContext paramFromNativeContext);

    public abstract Class nativeType();
}




