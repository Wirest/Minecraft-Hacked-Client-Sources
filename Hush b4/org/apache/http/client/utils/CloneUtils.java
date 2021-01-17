// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.http.client.utils;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import org.apache.http.annotation.Immutable;

@Immutable
public class CloneUtils
{
    public static <T> T cloneObject(final T obj) throws CloneNotSupportedException {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Cloneable) {
            final Class<?> clazz = obj.getClass();
            Method m;
            try {
                m = clazz.getMethod("clone", (Class<?>[])null);
            }
            catch (NoSuchMethodException ex) {
                throw new NoSuchMethodError(ex.getMessage());
            }
            try {
                final T result = (T)m.invoke(obj, (Object[])null);
                return result;
            }
            catch (InvocationTargetException ex2) {
                final Throwable cause = ex2.getCause();
                if (cause instanceof CloneNotSupportedException) {
                    throw (CloneNotSupportedException)cause;
                }
                throw new Error("Unexpected exception", cause);
            }
            catch (IllegalAccessException ex3) {
                throw new IllegalAccessError(ex3.getMessage());
            }
        }
        throw new CloneNotSupportedException();
    }
    
    public static Object clone(final Object obj) throws CloneNotSupportedException {
        return cloneObject(obj);
    }
    
    private CloneUtils() {
    }
}
