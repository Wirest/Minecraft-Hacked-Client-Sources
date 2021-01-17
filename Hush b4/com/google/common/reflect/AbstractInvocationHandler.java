// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.reflect;

import java.util.Arrays;
import java.lang.reflect.Proxy;
import javax.annotation.Nullable;
import java.lang.reflect.Method;
import com.google.common.annotations.Beta;
import java.lang.reflect.InvocationHandler;

@Beta
public abstract class AbstractInvocationHandler implements InvocationHandler
{
    private static final Object[] NO_ARGS;
    
    @Override
    public final Object invoke(final Object proxy, final Method method, @Nullable Object[] args) throws Throwable {
        if (args == null) {
            args = AbstractInvocationHandler.NO_ARGS;
        }
        if (args.length == 0 && method.getName().equals("hashCode")) {
            return this.hashCode();
        }
        if (args.length == 1 && method.getName().equals("equals") && method.getParameterTypes()[0] == Object.class) {
            final Object arg = args[0];
            if (arg == null) {
                return false;
            }
            if (proxy == arg) {
                return true;
            }
            return isProxyOfSameInterfaces(arg, proxy.getClass()) && this.equals(Proxy.getInvocationHandler(arg));
        }
        else {
            if (args.length == 0 && method.getName().equals("toString")) {
                return this.toString();
            }
            return this.handleInvocation(proxy, method, args);
        }
    }
    
    protected abstract Object handleInvocation(final Object p0, final Method p1, final Object[] p2) throws Throwable;
    
    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }
    
    @Override
    public int hashCode() {
        return super.hashCode();
    }
    
    @Override
    public String toString() {
        return super.toString();
    }
    
    private static boolean isProxyOfSameInterfaces(final Object arg, final Class<?> proxyClass) {
        return proxyClass.isInstance(arg) || (Proxy.isProxyClass(arg.getClass()) && Arrays.equals(arg.getClass().getInterfaces(), proxyClass.getInterfaces()));
    }
    
    static {
        NO_ARGS = new Object[0];
    }
}
