// 
// Decompiled by Procyon v0.5.36
// 

package joptsimple.internal;

import java.util.HashMap;
import java.util.Map;

public final class Classes
{
    private static final Map<Class<?>, Class<?>> WRAPPERS;
    
    private Classes() {
        throw new UnsupportedOperationException();
    }
    
    public static String shortNameOf(final String className) {
        return className.substring(className.lastIndexOf(46) + 1);
    }
    
    public static <T> Class<T> wrapperOf(final Class<T> clazz) {
        return (Class<T>)(clazz.isPrimitive() ? Classes.WRAPPERS.get(clazz) : clazz);
    }
    
    static {
        (WRAPPERS = new HashMap<Class<?>, Class<?>>(13)).put(Boolean.TYPE, Boolean.class);
        Classes.WRAPPERS.put(Byte.TYPE, Byte.class);
        Classes.WRAPPERS.put(Character.TYPE, Character.class);
        Classes.WRAPPERS.put(Double.TYPE, Double.class);
        Classes.WRAPPERS.put(Float.TYPE, Float.class);
        Classes.WRAPPERS.put(Integer.TYPE, Integer.class);
        Classes.WRAPPERS.put(Long.TYPE, Long.class);
        Classes.WRAPPERS.put(Short.TYPE, Short.class);
        Classes.WRAPPERS.put(Void.TYPE, Void.class);
    }
}
