// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3;

import java.util.HashMap;
import java.util.Collections;
import java.util.Set;
import org.apache.commons.lang3.mutable.MutableObject;
import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassUtils
{
    public static final char PACKAGE_SEPARATOR_CHAR = '.';
    public static final String PACKAGE_SEPARATOR;
    public static final char INNER_CLASS_SEPARATOR_CHAR = '$';
    public static final String INNER_CLASS_SEPARATOR;
    private static final Map<Class<?>, Class<?>> primitiveWrapperMap;
    private static final Map<Class<?>, Class<?>> wrapperPrimitiveMap;
    private static final Map<String, String> abbreviationMap;
    private static final Map<String, String> reverseAbbreviationMap;
    
    public static String getShortClassName(final Object object, final String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getShortClassName(object.getClass());
    }
    
    public static String getShortClassName(final Class<?> cls) {
        if (cls == null) {
            return "";
        }
        return getShortClassName(cls.getName());
    }
    
    public static String getShortClassName(String className) {
        if (StringUtils.isEmpty(className)) {
            return "";
        }
        final StringBuilder arrayPrefix = new StringBuilder();
        if (className.startsWith("[")) {
            while (className.charAt(0) == '[') {
                className = className.substring(1);
                arrayPrefix.append("[]");
            }
            if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
                className = className.substring(1, className.length() - 1);
            }
            if (ClassUtils.reverseAbbreviationMap.containsKey(className)) {
                className = ClassUtils.reverseAbbreviationMap.get(className);
            }
        }
        final int lastDotIdx = className.lastIndexOf(46);
        final int innerIdx = className.indexOf(36, (lastDotIdx == -1) ? 0 : (lastDotIdx + 1));
        String out = className.substring(lastDotIdx + 1);
        if (innerIdx != -1) {
            out = out.replace('$', '.');
        }
        return out + (Object)arrayPrefix;
    }
    
    public static String getSimpleName(final Class<?> cls) {
        if (cls == null) {
            return "";
        }
        return cls.getSimpleName();
    }
    
    public static String getSimpleName(final Object object, final String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getSimpleName(object.getClass());
    }
    
    public static String getPackageName(final Object object, final String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getPackageName(object.getClass());
    }
    
    public static String getPackageName(final Class<?> cls) {
        if (cls == null) {
            return "";
        }
        return getPackageName(cls.getName());
    }
    
    public static String getPackageName(String className) {
        if (StringUtils.isEmpty(className)) {
            return "";
        }
        while (className.charAt(0) == '[') {
            className = className.substring(1);
        }
        if (className.charAt(0) == 'L' && className.charAt(className.length() - 1) == ';') {
            className = className.substring(1);
        }
        final int i = className.lastIndexOf(46);
        if (i == -1) {
            return "";
        }
        return className.substring(0, i);
    }
    
    public static List<Class<?>> getAllSuperclasses(final Class<?> cls) {
        if (cls == null) {
            return null;
        }
        final List<Class<?>> classes = new ArrayList<Class<?>>();
        for (Class<?> superclass = cls.getSuperclass(); superclass != null; superclass = superclass.getSuperclass()) {
            classes.add(superclass);
        }
        return classes;
    }
    
    public static List<Class<?>> getAllInterfaces(final Class<?> cls) {
        if (cls == null) {
            return null;
        }
        final LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet<Class<?>>();
        getAllInterfaces(cls, interfacesFound);
        return new ArrayList<Class<?>>(interfacesFound);
    }
    
    private static void getAllInterfaces(Class<?> cls, final HashSet<Class<?>> interfacesFound) {
        while (cls != null) {
            final Class[] arr$;
            final Class<?>[] interfaces = (Class<?>[])(arr$ = cls.getInterfaces());
            for (final Class<?> i : arr$) {
                if (interfacesFound.add(i)) {
                    getAllInterfaces(i, interfacesFound);
                }
            }
            cls = cls.getSuperclass();
        }
    }
    
    public static List<Class<?>> convertClassNamesToClasses(final List<String> classNames) {
        if (classNames == null) {
            return null;
        }
        final List<Class<?>> classes = new ArrayList<Class<?>>(classNames.size());
        for (final String className : classNames) {
            try {
                classes.add(Class.forName(className));
            }
            catch (Exception ex) {
                classes.add(null);
            }
        }
        return classes;
    }
    
    public static List<String> convertClassesToClassNames(final List<Class<?>> classes) {
        if (classes == null) {
            return null;
        }
        final List<String> classNames = new ArrayList<String>(classes.size());
        for (final Class<?> cls : classes) {
            if (cls == null) {
                classNames.add(null);
            }
            else {
                classNames.add(cls.getName());
            }
        }
        return classNames;
    }
    
    public static boolean isAssignable(final Class<?>[] classArray, final Class<?>... toClassArray) {
        return isAssignable(classArray, toClassArray, SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_5));
    }
    
    public static boolean isAssignable(Class<?>[] classArray, Class<?>[] toClassArray, final boolean autoboxing) {
        if (!ArrayUtils.isSameLength(classArray, toClassArray)) {
            return false;
        }
        if (classArray == null) {
            classArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        if (toClassArray == null) {
            toClassArray = ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        for (int i = 0; i < classArray.length; ++i) {
            if (!isAssignable(classArray[i], toClassArray[i], autoboxing)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean isPrimitiveOrWrapper(final Class<?> type) {
        return type != null && (type.isPrimitive() || isPrimitiveWrapper(type));
    }
    
    public static boolean isPrimitiveWrapper(final Class<?> type) {
        return ClassUtils.wrapperPrimitiveMap.containsKey(type);
    }
    
    public static boolean isAssignable(final Class<?> cls, final Class<?> toClass) {
        return isAssignable(cls, toClass, SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_5));
    }
    
    public static boolean isAssignable(Class<?> cls, final Class<?> toClass, final boolean autoboxing) {
        if (toClass == null) {
            return false;
        }
        if (cls == null) {
            return !toClass.isPrimitive();
        }
        if (autoboxing) {
            if (cls.isPrimitive() && !toClass.isPrimitive()) {
                cls = primitiveToWrapper(cls);
                if (cls == null) {
                    return false;
                }
            }
            if (toClass.isPrimitive() && !cls.isPrimitive()) {
                cls = wrapperToPrimitive(cls);
                if (cls == null) {
                    return false;
                }
            }
        }
        if (cls.equals(toClass)) {
            return true;
        }
        if (!cls.isPrimitive()) {
            return toClass.isAssignableFrom(cls);
        }
        if (!toClass.isPrimitive()) {
            return false;
        }
        if (Integer.TYPE.equals(cls)) {
            return Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
        }
        if (Long.TYPE.equals(cls)) {
            return Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
        }
        if (Boolean.TYPE.equals(cls)) {
            return false;
        }
        if (Double.TYPE.equals(cls)) {
            return false;
        }
        if (Float.TYPE.equals(cls)) {
            return Double.TYPE.equals(toClass);
        }
        if (Character.TYPE.equals(cls)) {
            return Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
        }
        if (Short.TYPE.equals(cls)) {
            return Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass);
        }
        return Byte.TYPE.equals(cls) && (Short.TYPE.equals(toClass) || Integer.TYPE.equals(toClass) || Long.TYPE.equals(toClass) || Float.TYPE.equals(toClass) || Double.TYPE.equals(toClass));
    }
    
    public static Class<?> primitiveToWrapper(final Class<?> cls) {
        Class<?> convertedClass = cls;
        if (cls != null && cls.isPrimitive()) {
            convertedClass = ClassUtils.primitiveWrapperMap.get(cls);
        }
        return convertedClass;
    }
    
    public static Class<?>[] primitivesToWrappers(final Class<?>... classes) {
        if (classes == null) {
            return null;
        }
        if (classes.length == 0) {
            return classes;
        }
        final Class<?>[] convertedClasses = (Class<?>[])new Class[classes.length];
        for (int i = 0; i < classes.length; ++i) {
            convertedClasses[i] = primitiveToWrapper(classes[i]);
        }
        return convertedClasses;
    }
    
    public static Class<?> wrapperToPrimitive(final Class<?> cls) {
        return ClassUtils.wrapperPrimitiveMap.get(cls);
    }
    
    public static Class<?>[] wrappersToPrimitives(final Class<?>... classes) {
        if (classes == null) {
            return null;
        }
        if (classes.length == 0) {
            return classes;
        }
        final Class<?>[] convertedClasses = (Class<?>[])new Class[classes.length];
        for (int i = 0; i < classes.length; ++i) {
            convertedClasses[i] = wrapperToPrimitive(classes[i]);
        }
        return convertedClasses;
    }
    
    public static boolean isInnerClass(final Class<?> cls) {
        return cls != null && cls.getEnclosingClass() != null;
    }
    
    public static Class<?> getClass(final ClassLoader classLoader, final String className, final boolean initialize) throws ClassNotFoundException {
        try {
            Class<?> clazz;
            if (ClassUtils.abbreviationMap.containsKey(className)) {
                final String clsName = "[" + ClassUtils.abbreviationMap.get(className);
                clazz = Class.forName(clsName, initialize, classLoader).getComponentType();
            }
            else {
                clazz = Class.forName(toCanonicalName(className), initialize, classLoader);
            }
            return clazz;
        }
        catch (ClassNotFoundException ex) {
            final int lastDotIndex = className.lastIndexOf(46);
            if (lastDotIndex != -1) {
                try {
                    return getClass(classLoader, className.substring(0, lastDotIndex) + '$' + className.substring(lastDotIndex + 1), initialize);
                }
                catch (ClassNotFoundException ex2) {}
            }
            throw ex;
        }
    }
    
    public static Class<?> getClass(final ClassLoader classLoader, final String className) throws ClassNotFoundException {
        return getClass(classLoader, className, true);
    }
    
    public static Class<?> getClass(final String className) throws ClassNotFoundException {
        return getClass(className, true);
    }
    
    public static Class<?> getClass(final String className, final boolean initialize) throws ClassNotFoundException {
        final ClassLoader contextCL = Thread.currentThread().getContextClassLoader();
        final ClassLoader loader = (contextCL == null) ? ClassUtils.class.getClassLoader() : contextCL;
        return getClass(loader, className, initialize);
    }
    
    public static Method getPublicMethod(final Class<?> cls, final String methodName, final Class<?>... parameterTypes) throws SecurityException, NoSuchMethodException {
        final Method declaredMethod = cls.getMethod(methodName, parameterTypes);
        if (Modifier.isPublic(declaredMethod.getDeclaringClass().getModifiers())) {
            return declaredMethod;
        }
        final List<Class<?>> candidateClasses = new ArrayList<Class<?>>();
        candidateClasses.addAll(getAllInterfaces(cls));
        candidateClasses.addAll(getAllSuperclasses(cls));
        for (final Class<?> candidateClass : candidateClasses) {
            if (!Modifier.isPublic(candidateClass.getModifiers())) {
                continue;
            }
            Method candidateMethod;
            try {
                candidateMethod = candidateClass.getMethod(methodName, parameterTypes);
            }
            catch (NoSuchMethodException ex) {
                continue;
            }
            if (Modifier.isPublic(candidateMethod.getDeclaringClass().getModifiers())) {
                return candidateMethod;
            }
        }
        throw new NoSuchMethodException("Can't find a public method for " + methodName + " " + ArrayUtils.toString(parameterTypes));
    }
    
    private static String toCanonicalName(String className) {
        className = StringUtils.deleteWhitespace(className);
        if (className == null) {
            throw new NullPointerException("className must not be null.");
        }
        if (className.endsWith("[]")) {
            final StringBuilder classNameBuffer = new StringBuilder();
            while (className.endsWith("[]")) {
                className = className.substring(0, className.length() - 2);
                classNameBuffer.append("[");
            }
            final String abbreviation = ClassUtils.abbreviationMap.get(className);
            if (abbreviation != null) {
                classNameBuffer.append(abbreviation);
            }
            else {
                classNameBuffer.append("L").append(className).append(";");
            }
            className = classNameBuffer.toString();
        }
        return className;
    }
    
    public static Class<?>[] toClass(final Object... array) {
        if (array == null) {
            return null;
        }
        if (array.length == 0) {
            return ArrayUtils.EMPTY_CLASS_ARRAY;
        }
        final Class<?>[] classes = (Class<?>[])new Class[array.length];
        for (int i = 0; i < array.length; ++i) {
            classes[i] = ((array[i] == null) ? null : array[i].getClass());
        }
        return classes;
    }
    
    public static String getShortCanonicalName(final Object object, final String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getShortCanonicalName(object.getClass().getName());
    }
    
    public static String getShortCanonicalName(final Class<?> cls) {
        if (cls == null) {
            return "";
        }
        return getShortCanonicalName(cls.getName());
    }
    
    public static String getShortCanonicalName(final String canonicalName) {
        return getShortClassName(getCanonicalName(canonicalName));
    }
    
    public static String getPackageCanonicalName(final Object object, final String valueIfNull) {
        if (object == null) {
            return valueIfNull;
        }
        return getPackageCanonicalName(object.getClass().getName());
    }
    
    public static String getPackageCanonicalName(final Class<?> cls) {
        if (cls == null) {
            return "";
        }
        return getPackageCanonicalName(cls.getName());
    }
    
    public static String getPackageCanonicalName(final String canonicalName) {
        return getPackageName(getCanonicalName(canonicalName));
    }
    
    private static String getCanonicalName(String className) {
        className = StringUtils.deleteWhitespace(className);
        if (className == null) {
            return null;
        }
        int dim = 0;
        while (className.startsWith("[")) {
            ++dim;
            className = className.substring(1);
        }
        if (dim < 1) {
            return className;
        }
        if (className.startsWith("L")) {
            className = className.substring(1, className.endsWith(";") ? (className.length() - 1) : className.length());
        }
        else if (className.length() > 0) {
            className = ClassUtils.reverseAbbreviationMap.get(className.substring(0, 1));
        }
        final StringBuilder canonicalClassNameBuffer = new StringBuilder(className);
        for (int i = 0; i < dim; ++i) {
            canonicalClassNameBuffer.append("[]");
        }
        return canonicalClassNameBuffer.toString();
    }
    
    public static Iterable<Class<?>> hierarchy(final Class<?> type) {
        return hierarchy(type, Interfaces.EXCLUDE);
    }
    
    public static Iterable<Class<?>> hierarchy(final Class<?> type, final Interfaces interfacesBehavior) {
        final Iterable<Class<?>> classes = new Iterable<Class<?>>() {
            @Override
            public Iterator<Class<?>> iterator() {
                final MutableObject<Class<?>> next = new MutableObject<Class<?>>(type);
                return new Iterator<Class<?>>() {
                    @Override
                    public boolean hasNext() {
                        return next.getValue() != null;
                    }
                    
                    @Override
                    public Class<?> next() {
                        final Class<?> result = next.getValue();
                        next.setValue(result.getSuperclass());
                        return result;
                    }
                    
                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
        if (interfacesBehavior != Interfaces.INCLUDE) {
            return classes;
        }
        return new Iterable<Class<?>>() {
            @Override
            public Iterator<Class<?>> iterator() {
                final Set<Class<?>> seenInterfaces = new HashSet<Class<?>>();
                final Iterator<Class<?>> wrapped = classes.iterator();
                return new Iterator<Class<?>>() {
                    Iterator<Class<?>> interfaces = Collections.emptySet().iterator();
                    
                    @Override
                    public boolean hasNext() {
                        return this.interfaces.hasNext() || wrapped.hasNext();
                    }
                    
                    @Override
                    public Class<?> next() {
                        if (this.interfaces.hasNext()) {
                            final Class<?> nextInterface = this.interfaces.next();
                            seenInterfaces.add(nextInterface);
                            return nextInterface;
                        }
                        final Class<?> nextSuperclass = wrapped.next();
                        final Set<Class<?>> currentInterfaces = new LinkedHashSet<Class<?>>();
                        this.walkInterfaces(currentInterfaces, nextSuperclass);
                        this.interfaces = currentInterfaces.iterator();
                        return nextSuperclass;
                    }
                    
                    private void walkInterfaces(final Set<Class<?>> addTo, final Class<?> c) {
                        for (final Class<?> iface : c.getInterfaces()) {
                            if (!seenInterfaces.contains(iface)) {
                                addTo.add(iface);
                            }
                            this.walkInterfaces(addTo, iface);
                        }
                    }
                    
                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
    
    static {
        PACKAGE_SEPARATOR = String.valueOf('.');
        INNER_CLASS_SEPARATOR = String.valueOf('$');
        (primitiveWrapperMap = new HashMap<Class<?>, Class<?>>()).put(Boolean.TYPE, Boolean.class);
        ClassUtils.primitiveWrapperMap.put(Byte.TYPE, Byte.class);
        ClassUtils.primitiveWrapperMap.put(Character.TYPE, Character.class);
        ClassUtils.primitiveWrapperMap.put(Short.TYPE, Short.class);
        ClassUtils.primitiveWrapperMap.put(Integer.TYPE, Integer.class);
        ClassUtils.primitiveWrapperMap.put(Long.TYPE, Long.class);
        ClassUtils.primitiveWrapperMap.put(Double.TYPE, Double.class);
        ClassUtils.primitiveWrapperMap.put(Float.TYPE, Float.class);
        ClassUtils.primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
        wrapperPrimitiveMap = new HashMap<Class<?>, Class<?>>();
        for (final Class<?> primitiveClass : ClassUtils.primitiveWrapperMap.keySet()) {
            final Class<?> wrapperClass = ClassUtils.primitiveWrapperMap.get(primitiveClass);
            if (!primitiveClass.equals(wrapperClass)) {
                ClassUtils.wrapperPrimitiveMap.put(wrapperClass, primitiveClass);
            }
        }
        final Map<String, String> m = new HashMap<String, String>();
        m.put("int", "I");
        m.put("boolean", "Z");
        m.put("float", "F");
        m.put("long", "J");
        m.put("short", "S");
        m.put("byte", "B");
        m.put("double", "D");
        m.put("char", "C");
        m.put("void", "V");
        final Map<String, String> r = new HashMap<String, String>();
        for (final Map.Entry<String, String> e : m.entrySet()) {
            r.put(e.getValue(), e.getKey());
        }
        abbreviationMap = Collections.unmodifiableMap((Map<? extends String, ? extends String>)m);
        reverseAbbreviationMap = Collections.unmodifiableMap((Map<? extends String, ? extends String>)r);
    }
    
    public enum Interfaces
    {
        INCLUDE, 
        EXCLUDE;
    }
}
