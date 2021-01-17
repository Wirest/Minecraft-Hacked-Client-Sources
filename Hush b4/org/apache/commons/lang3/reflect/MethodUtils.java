// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.reflect;

import java.lang.reflect.TypeVariable;
import java.util.Map;
import java.util.Iterator;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashSet;
import org.apache.commons.lang3.Validate;
import java.util.Set;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Modifier;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.ArrayUtils;

public class MethodUtils
{
    public static Object invokeMethod(final Object object, final String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ArrayUtils.nullToEmpty(args);
        final Class<?>[] parameterTypes = ClassUtils.toClass(args);
        return invokeMethod(object, methodName, args, parameterTypes);
    }
    
    public static Object invokeMethod(final Object object, final String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        args = ArrayUtils.nullToEmpty(args);
        final Method method = getMatchingAccessibleMethod(object.getClass(), methodName, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + object.getClass().getName());
        }
        return method.invoke(object, args);
    }
    
    public static Object invokeExactMethod(final Object object, final String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ArrayUtils.nullToEmpty(args);
        final Class<?>[] parameterTypes = ClassUtils.toClass(args);
        return invokeExactMethod(object, methodName, args, parameterTypes);
    }
    
    public static Object invokeExactMethod(final Object object, final String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ArrayUtils.nullToEmpty(args);
        parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        final Method method = getAccessibleMethod(object.getClass(), methodName, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + object.getClass().getName());
        }
        return method.invoke(object, args);
    }
    
    public static Object invokeExactStaticMethod(final Class<?> cls, final String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ArrayUtils.nullToEmpty(args);
        parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        final Method method = getAccessibleMethod(cls, methodName, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + cls.getName());
        }
        return method.invoke(null, args);
    }
    
    public static Object invokeStaticMethod(final Class<?> cls, final String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ArrayUtils.nullToEmpty(args);
        final Class<?>[] parameterTypes = ClassUtils.toClass(args);
        return invokeStaticMethod(cls, methodName, args, parameterTypes);
    }
    
    public static Object invokeStaticMethod(final Class<?> cls, final String methodName, Object[] args, Class<?>[] parameterTypes) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ArrayUtils.nullToEmpty(args);
        parameterTypes = ArrayUtils.nullToEmpty(parameterTypes);
        final Method method = getMatchingAccessibleMethod(cls, methodName, parameterTypes);
        if (method == null) {
            throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + cls.getName());
        }
        return method.invoke(null, args);
    }
    
    public static Object invokeExactStaticMethod(final Class<?> cls, final String methodName, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        args = ArrayUtils.nullToEmpty(args);
        final Class<?>[] parameterTypes = ClassUtils.toClass(args);
        return invokeExactStaticMethod(cls, methodName, args, parameterTypes);
    }
    
    public static Method getAccessibleMethod(final Class<?> cls, final String methodName, final Class<?>... parameterTypes) {
        try {
            return getAccessibleMethod(cls.getMethod(methodName, parameterTypes));
        }
        catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    public static Method getAccessibleMethod(Method method) {
        if (!MemberUtils.isAccessible(method)) {
            return null;
        }
        final Class<?> cls = method.getDeclaringClass();
        if (Modifier.isPublic(cls.getModifiers())) {
            return method;
        }
        final String methodName = method.getName();
        final Class<?>[] parameterTypes = method.getParameterTypes();
        method = getAccessibleMethodFromInterfaceNest(cls, methodName, parameterTypes);
        if (method == null) {
            method = getAccessibleMethodFromSuperclass(cls, methodName, parameterTypes);
        }
        return method;
    }
    
    private static Method getAccessibleMethodFromSuperclass(final Class<?> cls, final String methodName, final Class<?>... parameterTypes) {
        for (Class<?> parentClass = cls.getSuperclass(); parentClass != null; parentClass = parentClass.getSuperclass()) {
            if (Modifier.isPublic(parentClass.getModifiers())) {
                try {
                    return parentClass.getMethod(methodName, parameterTypes);
                }
                catch (NoSuchMethodException e) {
                    return null;
                }
            }
        }
        return null;
    }
    
    private static Method getAccessibleMethodFromInterfaceNest(Class<?> cls, final String methodName, final Class<?>... parameterTypes) {
        while (cls != null) {
            final Class<?>[] interfaces = cls.getInterfaces();
            for (int i = 0; i < interfaces.length; ++i) {
                if (Modifier.isPublic(interfaces[i].getModifiers())) {
                    try {
                        return interfaces[i].getDeclaredMethod(methodName, parameterTypes);
                    }
                    catch (NoSuchMethodException e) {
                        final Method method = getAccessibleMethodFromInterfaceNest(interfaces[i], methodName, parameterTypes);
                        if (method != null) {
                            return method;
                        }
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        return null;
    }
    
    public static Method getMatchingAccessibleMethod(final Class<?> cls, final String methodName, final Class<?>... parameterTypes) {
        try {
            final Method method = cls.getMethod(methodName, parameterTypes);
            MemberUtils.setAccessibleWorkaround(method);
            return method;
        }
        catch (NoSuchMethodException e) {
            Method bestMatch = null;
            final Method[] arr$;
            final Method[] methods = arr$ = cls.getMethods();
            for (final Method method2 : arr$) {
                if (method2.getName().equals(methodName) && ClassUtils.isAssignable(parameterTypes, method2.getParameterTypes(), true)) {
                    final Method accessibleMethod = getAccessibleMethod(method2);
                    if (accessibleMethod != null && (bestMatch == null || MemberUtils.compareParameterTypes(accessibleMethod.getParameterTypes(), bestMatch.getParameterTypes(), parameterTypes) < 0)) {
                        bestMatch = accessibleMethod;
                    }
                }
            }
            if (bestMatch != null) {
                MemberUtils.setAccessibleWorkaround(bestMatch);
            }
            return bestMatch;
        }
    }
    
    public static Set<Method> getOverrideHierarchy(final Method method, final ClassUtils.Interfaces interfacesBehavior) {
        Validate.notNull(method);
        final Set<Method> result = new LinkedHashSet<Method>();
        result.add(method);
        final Class<?>[] parameterTypes = method.getParameterTypes();
        final Class<?> declaringClass = method.getDeclaringClass();
        final Iterator<Class<?>> hierarchy = ClassUtils.hierarchy(declaringClass, interfacesBehavior).iterator();
        hierarchy.next();
    Label_0053:
        while (hierarchy.hasNext()) {
            final Class<?> c = hierarchy.next();
            final Method m = getMatchingAccessibleMethod(c, method.getName(), parameterTypes);
            if (m == null) {
                continue;
            }
            if (Arrays.equals(m.getParameterTypes(), parameterTypes)) {
                result.add(m);
            }
            else {
                final Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(declaringClass, m.getDeclaringClass());
                for (int i = 0; i < parameterTypes.length; ++i) {
                    final Type childType = TypeUtils.unrollVariables(typeArguments, method.getGenericParameterTypes()[i]);
                    final Type parentType = TypeUtils.unrollVariables(typeArguments, m.getGenericParameterTypes()[i]);
                    if (!TypeUtils.equals(childType, parentType)) {
                        continue Label_0053;
                    }
                }
                result.add(m);
            }
        }
        return result;
    }
}
