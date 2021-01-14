package com.sun.jna;

import java.lang.reflect.Method;

public class MethodParameterContext
        extends FunctionParameterContext {
    private Method method;

    MethodParameterContext(Function paramFunction, Object[] paramArrayOfObject, int paramInt, Method paramMethod) {
        super(paramFunction, paramArrayOfObject, paramInt);
        this.method = paramMethod;
    }

    public Method getMethod() {
        return this.method;
    }
}




