// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.event;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Set;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang3.reflect.MethodUtils;

public class EventUtils
{
    public static <L> void addEventListener(final Object eventSource, final Class<L> listenerType, final L listener) {
        try {
            MethodUtils.invokeMethod(eventSource, "add" + listenerType.getSimpleName(), listener);
        }
        catch (NoSuchMethodException e2) {
            throw new IllegalArgumentException("Class " + eventSource.getClass().getName() + " does not have a public add" + listenerType.getSimpleName() + " method which takes a parameter of type " + listenerType.getName() + ".");
        }
        catch (IllegalAccessException e3) {
            throw new IllegalArgumentException("Class " + eventSource.getClass().getName() + " does not have an accessible add" + listenerType.getSimpleName() + " method which takes a parameter of type " + listenerType.getName() + ".");
        }
        catch (InvocationTargetException e) {
            throw new RuntimeException("Unable to add listener.", e.getCause());
        }
    }
    
    public static <L> void bindEventsToMethod(final Object target, final String methodName, final Object eventSource, final Class<L> listenerType, final String... eventTypes) {
        final L listener = listenerType.cast(Proxy.newProxyInstance(target.getClass().getClassLoader(), new Class[] { listenerType }, new EventBindingInvocationHandler(target, methodName, eventTypes)));
        addEventListener(eventSource, listenerType, listener);
    }
    
    private static class EventBindingInvocationHandler implements InvocationHandler
    {
        private final Object target;
        private final String methodName;
        private final Set<String> eventTypes;
        
        EventBindingInvocationHandler(final Object target, final String methodName, final String[] eventTypes) {
            this.target = target;
            this.methodName = methodName;
            this.eventTypes = new HashSet<String>(Arrays.asList(eventTypes));
        }
        
        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] parameters) throws Throwable {
            if (!this.eventTypes.isEmpty() && !this.eventTypes.contains(method.getName())) {
                return null;
            }
            if (this.hasMatchingParametersMethod(method)) {
                return MethodUtils.invokeMethod(this.target, this.methodName, parameters);
            }
            return MethodUtils.invokeMethod(this.target, this.methodName, new Object[0]);
        }
        
        private boolean hasMatchingParametersMethod(final Method method) {
            return MethodUtils.getAccessibleMethod(this.target.getClass(), this.methodName, method.getParameterTypes()) != null;
        }
    }
}
