// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import java.lang.reflect.Proxy;
import com.google.common.base.Preconditions;
import java.lang.reflect.InvocationHandler;
import com.google.common.annotations.Beta;

@Beta
public final class Reflection
{
    public static String getPackageName(final Class<?> clazz) {
        return getPackageName(clazz.getName());
    }
    
    public static String getPackageName(final String classFullName) {
        final int lastDot = classFullName.lastIndexOf(46);
        return (lastDot < 0) ? "" : classFullName.substring(0, lastDot);
    }
    
    public static void initialize(final Class<?>... classes) {
        for (final Class<?> clazz : classes) {
            try {
                Class.forName(clazz.getName(), true, clazz.getClassLoader());
            }
            catch (ClassNotFoundException e) {
                throw new AssertionError((Object)e);
            }
        }
    }
    
    public static <T> T newProxy(final Class<T> interfaceType, final InvocationHandler handler) {
        Preconditions.checkNotNull(handler);
        Preconditions.checkArgument(interfaceType.isInterface(), "%s is not an interface", interfaceType);
        final Object object = Proxy.newProxyInstance(interfaceType.getClassLoader(), new Class[] { interfaceType }, handler);
        return interfaceType.cast(object);
    }
    
    private Reflection() {
    }
}
