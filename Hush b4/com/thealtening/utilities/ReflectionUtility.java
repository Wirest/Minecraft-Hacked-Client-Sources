// 
// Decompiled by Procyon v0.5.36
// 

package com.thealtening.utilities;

import java.lang.reflect.Field;

public class ReflectionUtility
{
    private String className;
    private Class<?> clazz;
    
    public ReflectionUtility(final String className) {
        try {
            this.clazz = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    public void setStaticField(final String fieldName, final Object newValue) throws NoSuchFieldException, IllegalAccessException {
        final Field field = this.clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        final Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & 0xFFFFFFEF);
        field.set(null, newValue);
    }
}
