// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.reflect;

import java.lang.reflect.Modifier;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Member;
import org.apache.commons.lang3.Validate;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ArrayUtils;

public class ConstructorUtils
{
    public static <T> T invokeConstructor(final Class<T> cls, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        args = ArrayUtils.nullToEmpty(args);
        final Class<?>[] parameterTypes = ClassUtils.toClass(args);
        return invokeConstructor(cls, args, parameterTypes);
    }
    
    public static <T> T invokeConstructor(final Class<T> cls, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        args = ArrayUtils.nullToEmpty(args);
        parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        final Constructor<T> ctor = getMatchingAccessibleConstructor(cls, parameterTypes);
        if (ctor == null) {
            throw new NoSuchMethodException("No such accessible constructor on object: " + cls.getName());
        }
        return ctor.newInstance(args);
    }
    
    public static <T> T invokeExactConstructor(final Class<T> cls, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        args = ArrayUtils.nullToEmpty(args);
        final Class<?>[] parameterTypes = ClassUtils.toClass(args);
        return invokeExactConstructor(cls, args, parameterTypes);
    }
    
    public static <T> T invokeExactConstructor(final Class<T> cls, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        args = ArrayUtils.nullToEmpty(args);
        parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        final Constructor<T> ctor = getAccessibleConstructor(cls, parameterTypes);
        if (ctor == null) {
            throw new NoSuchMethodException("No such accessible constructor on object: " + cls.getName());
        }
        return ctor.newInstance(args);
    }
    
    public static <T> Constructor<T> getAccessibleConstructor(final Class<T> cls, final Class<?>... parameterTypes) {
        Validate.notNull(cls, "class cannot be null", new Object[0]);
        try {
            return getAccessibleConstructor(cls.getConstructor(parameterTypes));
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    public static <T> Constructor<T> getAccessibleConstructor(final Constructor<T> ctor) {
        Validate.notNull(ctor, "constructor cannot be null", new Object[0]);
        return (MemberUtils.isAccessible(ctor) && isAccessible(ctor.getDeclaringClass())) ? ctor : null;
    }
    
    public static <T> Constructor<T> getMatchingAccessibleConstructor(final Class<T> cls, final Class<?>... parameterTypes) {
        Validate.notNull(cls, "class cannot be null", new Object[0]);
        try {
            final Constructor<T> ctor = cls.getConstructor(parameterTypes);
            MemberUtils.setAccessibleWorkaround(ctor);
            return ctor;
        }
        catch (NoSuchMethodException e) {
            Constructor<T> result = null;
            final Constructor[] arr$;
            final Constructor<?>[] ctors = (Constructor<?>[])(arr$ = cls.getConstructors());
            for (Constructor<?> ctor2 : arr$) {
                if (ClassUtils.isAssignable(parameterTypes, ctor2.getParameterTypes(), true)) {
                    ctor2 = getAccessibleConstructor(ctor2);
                    if (ctor2 != null) {
                        MemberUtils.setAccessibleWorkaround(ctor2);
                        if (result == null || MemberUtils.compareParameterTypes(ctor2.getParameterTypes(), result.getParameterTypes(), parameterTypes) < 0) {
                            final Constructor<T> constructor = result = (Constructor<T>)ctor2;
                        }
                    }
                }
            }
            return result;
        }
    }
    
    private static boolean isAccessible(final Class<?> type) {
        for (Class<?> cls = type; cls != null; cls = cls.getEnclosingClass()) {
            if (!Modifier.isPublic(cls.getModifiers())) {
                return false;
            }
        }
        return true;
    }
}
