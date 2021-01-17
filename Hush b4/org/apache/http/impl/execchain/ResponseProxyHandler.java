// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.impl.execchain;

import java.io.Closeable;
import java.lang.reflect.InvocationTargetException;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import java.lang.reflect.Method;
import org.apache.http.annotation.NotThreadSafe;
import java.lang.reflect.InvocationHandler;

@NotThreadSafe
class ResponseProxyHandler implements InvocationHandler
{
    private static final Method CLOSE_METHOD;
    private final HttpResponse original;
    private final ConnectionHolder connHolder;
    
    ResponseProxyHandler(final HttpResponse original, final ConnectionHolder connHolder) {
        this.original = original;
        this.connHolder = connHolder;
        final HttpEntity entity = original.getEntity();
        if (entity != null && entity.isStreaming() && connHolder != null) {
            this.original.setEntity(new ResponseEntityWrapper(entity, connHolder));
        }
    }
    
    public void close() throws IOException {
        if (this.connHolder != null) {
            this.connHolder.abortConnection();
        }
    }
    
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if (method.equals(ResponseProxyHandler.CLOSE_METHOD)) {
            this.close();
            return null;
        }
        try {
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
            CLOSE_METHOD = Closeable.class.getMethod("close", (Class<?>[])new Class[0]);
        }
        catch (NoSuchMethodException ex) {
            throw new Error(ex);
        }
    }
}
