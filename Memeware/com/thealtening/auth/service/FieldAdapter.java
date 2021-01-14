
package com.thealtening.auth.service;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

public final class FieldAdapter {
    private final HashMap<String, MethodHandle> fields = new HashMap();
    private static final MethodHandles.Lookup LOOKUP;
    private static Field MODIFIERS;

    static {
        MethodHandles.Lookup lookupObject;
        try {
            MODIFIERS = Field.class.getDeclaredField("modifiers");
            MODIFIERS.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        try {
            Field lookupImplField = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            lookupImplField.setAccessible(true);
            lookupObject = (MethodHandles.Lookup) lookupImplField.get(null);
        } catch (ReflectiveOperationException e) {
            lookupObject = MethodHandles.lookup();
        }
        LOOKUP = lookupObject;
    }

    public FieldAdapter(String parent) {
        try {
            Class<?> cls = Class.forName(parent);
            Field modifiers = MODIFIERS;
            for (Field field : cls.getDeclaredFields()) {
                field.setAccessible(true);
                int accessFlags = field.getModifiers();
                if (Modifier.isFinal(accessFlags)) {
                    modifiers.setInt(field, accessFlags & -17);
                }
                MethodHandle handler = LOOKUP.unreflectSetter(field);
                handler = handler.asType(handler.type().generic().changeReturnType(Void.TYPE));
                this.fields.put(field.getName(), handler);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Couldn't load/find the specified class");
        } catch (Exception e) {
            throw new RuntimeException("Couldn't create a method handler for the field");
        }
    }

    public void updateFieldIfPresent(String name, Object newValue) {
        Optional.ofNullable(this.fields.get(name)).ifPresent(setter -> {
            try {
                setter.invokeExact(newValue);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        });
    }
}

