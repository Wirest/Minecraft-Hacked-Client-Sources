// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.lang.reflect.Method;

public class MethodParameterContext extends FunctionParameterContext
{
    private Method method;
    
    MethodParameterContext(final Function f, final Object[] args, final int index, final Method m) {
        super(f, args, index);
        this.method = m;
    }
    
    public Method getMethod() {
        return this.method;
    }
}
