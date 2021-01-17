// 
// Decompiled by Procyon v0.5.36
// 

package com.google.gson.internal;

import java.util.Collections;
import java.util.HashMap;
import java.lang.reflect.Type;
import java.util.Map;

public final class Primitives
{
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPE;
    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPE;
    
    private Primitives() {
    }
    
    private static void add(final Map<Class<?>, Class<?>> forward, final Map<Class<?>, Class<?>> backward, final Class<?> key, final Class<?> value) {
        forward.put(key, value);
        backward.put(value, key);
    }
    
    public static boolean isPrimitive(final Type type) {
        return Primitives.PRIMITIVE_TO_WRAPPER_TYPE.containsKey(type);
    }
    
    public static boolean isWrapperType(final Type type) {
        return Primitives.WRAPPER_TO_PRIMITIVE_TYPE.containsKey($Gson$Preconditions.checkNotNull(type));
    }
    
    public static <T> Class<T> wrap(final Class<T> type) {
        final Class<T> wrapped = (Class<T>)Primitives.PRIMITIVE_TO_WRAPPER_TYPE.get($Gson$Preconditions.checkNotNull(type));
        return (wrapped == null) ? type : wrapped;
    }
    
    public static <T> Class<T> unwrap(final Class<T> type) {
        final Class<T> unwrapped = (Class<T>)Primitives.WRAPPER_TO_PRIMITIVE_TYPE.get($Gson$Preconditions.checkNotNull(type));
        return (unwrapped == null) ? type : unwrapped;
    }
    
    static {
        final Map<Class<?>, Class<?>> primToWrap = new HashMap<Class<?>, Class<?>>(16);
        final Map<Class<?>, Class<?>> wrapToPrim = new HashMap<Class<?>, Class<?>>(16);
        add(primToWrap, wrapToPrim, Boolean.TYPE, Boolean.class);
        add(primToWrap, wrapToPrim, Byte.TYPE, Byte.class);
        add(primToWrap, wrapToPrim, Character.TYPE, Character.class);
        add(primToWrap, wrapToPrim, Double.TYPE, Double.class);
        add(primToWrap, wrapToPrim, Float.TYPE, Float.class);
        add(primToWrap, wrapToPrim, Integer.TYPE, Integer.class);
        add(primToWrap, wrapToPrim, Long.TYPE, Long.class);
        add(primToWrap, wrapToPrim, Short.TYPE, Short.class);
        add(primToWrap, wrapToPrim, Void.TYPE, Void.class);
        PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap((Map<? extends Class<?>, ? extends Class<?>>)primToWrap);
        WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap((Map<? extends Class<?>, ? extends Class<?>>)wrapToPrim);
    }
}
