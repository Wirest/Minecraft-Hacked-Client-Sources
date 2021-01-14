package com.sun.jna;

public abstract interface TypeMapper {
    public abstract FromNativeConverter getFromNativeConverter(Class paramClass);

    public abstract ToNativeConverter getToNativeConverter(Class paramClass);
}




