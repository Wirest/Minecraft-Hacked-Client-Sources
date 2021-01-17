// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.execchain;

import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import org.apache.http.HttpEntity;
import java.lang.reflect.Method;
import org.apache.http.annotation.NotThreadSafe;
import java.lang.reflect.InvocationHandler;

@NotThreadSafe
class RequestEntityExecHandler implements InvocationHandler
{
    private static final Method WRITE_TO_METHOD;
    private final HttpEntity original;
    private boolean consumed;
    
    RequestEntityExecHandler(final HttpEntity original) {
        this.consumed = false;
        this.original = original;
    }
    
    public HttpEntity getOriginal() {
        return this.original;
    }
    
    public boolean isConsumed() {
        return this.consumed;
    }
    
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        try {
            if (method.equals(RequestEntityExecHandler.WRITE_TO_METHOD)) {
                this.consumed = true;
            }
            return method.invoke(this.original, args);
        }
        catch (InvocationTargetException ex) {
            final Throwable cause = ex.getCause();
            if (cause != null) {
                throw cause;
            }
            throw ex;
        }
    }
    
    static {
        try {
            WRITE_TO_METHOD = HttpEntity.class.getMethod("writeTo", OutputStream.class);
        }
        catch (NoSuchMethodException ex) {
            throw new Error(ex);
        }
    }
}
