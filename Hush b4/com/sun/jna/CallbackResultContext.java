// 
// Decompiled by Procyon v0.5.36
// 

package com.sun.jna;

import java.lang.reflect.Method;

public class CallbackResultContext extends ToNativeContext
{
    private Method method;
    
    CallbackResultContext(final Method callbackMethod) {
        this.method = callbackMethod;
    }
    
    public Method getMethod() {
        return this.method;
    }
}
