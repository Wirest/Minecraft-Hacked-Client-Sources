// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import java.lang.reflect.Modifier;
import org.apache.logging.log4j.core.helpers.Loader;
import org.apache.logging.log4j.status.StatusLogger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.logging.log4j.Logger;

public final class ReflectiveCallerClassUtility
{
    private static final Logger LOGGER;
    private static final boolean GET_CALLER_CLASS_SUPPORTED;
    private static final Method GET_CALLER_CLASS_METHOD;
    static final int JAVA_7U25_COMPENSATION_OFFSET;
    
    private ReflectiveCallerClassUtility() {
    }
    
    public static boolean isSupported() {
        return ReflectiveCallerClassUtility.GET_CALLER_CLASS_SUPPORTED;
    }
    
    public static Class<?> getCaller(final int depth) {
        if (!ReflectiveCallerClassUtility.GET_CALLER_CLASS_SUPPORTED) {
            return null;
        }
        try {
            return (Class<?>)ReflectiveCallerClassUtility.GET_CALLER_CLASS_METHOD.invoke(null, depth + 1 + ReflectiveCallerClassUtility.JAVA_7U25_COMPENSATION_OFFSET);
        }
        catch (IllegalAccessException ignore) {
            ReflectiveCallerClassUtility.LOGGER.warn("Should not have failed to call getCallerClass.");
        }
        catch (InvocationTargetException ignore2) {
            ReflectiveCallerClassUtility.LOGGER.warn("Should not have failed to call getCallerClass.");
        }
        return null;
    }
    
    static {
        LOGGER = StatusLogger.getLogger();
        Method getCallerClass = null;
        int java7u25CompensationOffset = 0;
        try {
            final ClassLoader loader = Loader.getClassLoader();
            final Class<?> clazz = loader.loadClass("sun.reflect.Reflection");
            final Method[] arr$;
            final Method[] methods = arr$ = clazz.getMethods();
            for (final Method method : arr$) {
                final int modifier = method.getModifiers();
                final Class<?>[] parameterTypes = method.getParameterTypes();
                if (method.getName().equals("getCallerClass") && Modifier.isStatic(modifier) && parameterTypes.length == 1 && parameterTypes[0] == Integer.TYPE) {
                    getCallerClass = method;
                    break;
                }
            }
            if (getCallerClass == null) {
                ReflectiveCallerClassUtility.LOGGER.info("sun.reflect.Reflection#getCallerClass does not exist.");
            }
            else {
                Object o = getCallerClass.invoke(null, 0);
                if (o == null || o != clazz) {
                    getCallerClass = null;
                    ReflectiveCallerClassUtility.LOGGER.warn("sun.reflect.Reflection#getCallerClass returned unexpected value of [{}] and is unusable. Will fall back to another option.", o);
                }
                else {
                    o = getCallerClass.invoke(null, 1);
                    if (o == clazz) {
                        java7u25CompensationOffset = 1;
                        ReflectiveCallerClassUtility.LOGGER.warn("sun.reflect.Reflection#getCallerClass is broken in Java 7u25. You should upgrade to 7u40. Using alternate stack offset to compensate.");
                    }
                }
            }
        }
        catch (ClassNotFoundException e) {
            ReflectiveCallerClassUtility.LOGGER.info("sun.reflect.Reflection is not installed.");
        }
        catch (IllegalAccessException e2) {
            ReflectiveCallerClassUtility.LOGGER.info("sun.reflect.Reflection#getCallerClass is not accessible.");
        }
        catch (InvocationTargetException e3) {
            ReflectiveCallerClassUtility.LOGGER.info("sun.reflect.Reflection#getCallerClass is not supported.");
        }
        if (getCallerClass == null) {
            GET_CALLER_CLASS_SUPPORTED = false;
            GET_CALLER_CLASS_METHOD = null;
            JAVA_7U25_COMPENSATION_OFFSET = -1;
        }
        else {
            GET_CALLER_CLASS_SUPPORTED = true;
            GET_CALLER_CLASS_METHOD = getCallerClass;
            JAVA_7U25_COMPENSATION_OFFSET = java7u25CompensationOffset;
        }
    }
}
