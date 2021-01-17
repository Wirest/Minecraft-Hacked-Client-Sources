// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.lang.reflect.Method;

public class CallbackParameterContext extends FromNativeContext
{
    private Method method;
    private Object[] args;
    private int index;
    
    CallbackParameterContext(final Class javaType, final Method m, final Object[] args, final int index) {
        super(javaType);
        this.method = m;
        this.args = args;
        this.index = index;
    }
    
    public Method getMethod() {
        return this.method;
    }
    
    public Object[] getArguments() {
        return this.args;
    }
    
    public int getIndex() {
        return this.index;
    }
}
