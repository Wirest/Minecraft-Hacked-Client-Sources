package com.sun.jna;

public abstract interface NativeMapped {
    public abstract Object fromNative(Object paramObject, FromNativeContext paramFromNativeContext);

    public abstract Object toNative();

    public abstract Class nativeType();
}




