package com.sun.jna;

import java.lang.reflect.Method;

public abstract interface FunctionMapper {
    public abstract String getFunctionName(NativeLibrary paramNativeLibrary, Method paramMethod);
}




