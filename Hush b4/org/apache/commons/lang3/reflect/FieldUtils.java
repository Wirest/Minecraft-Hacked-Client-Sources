// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.reflect;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Member;
import java.util.Iterator;
import org.apache.commons.lang3.ClassUtils;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

public class FieldUtils
{
    public static Field getField(final Class<?> cls, final String fieldName) {
        final Field field = getField(cls, fieldName, false);
        MemberUtils.setAccessibleWorkaround(field);
        return field;
    }
    
    public static Field getField(final Class<?> cls, final String fieldName, final boolean forceAccess) {
        Validate.isTrue(cls != null, "The class must not be null", new Object[0]);
        Validate.isTrue(StringUtils.isNotBlank(fieldName), "The field name must not be blank/empty", new Object[0]);
        for (Class<?> acls = cls; acls != null; acls = acls.getSuperclass()) {
            try {
                final Field field = acls.getDeclaredField(fieldName);
                if (!Modifier.isPublic(field.getModifiers())) {
                    if (!forceAccess) {
                        continue;
                    }
                    field.setAccessible(true);
                }
                return field;
            }
            catch (NoSuchFieldException ex) {}
        }
        Field match = null;
        for (final Class<?> class1 : ClassUtils.getAllInterfaces(cls)) {
            try {
                final Field test = class1.getField(fieldName);
                Validate.isTrue(match == null, "Reference to field %s is ambiguous relative to %s; a matching field exists on two or more implemented interfaces.", fieldName, cls);
                match = test;
            }
            catch (NoSuchFieldException ex2) {}
        }
        return match;
    }
    
    public static Field getDeclaredField(final Class<?> cls, final String fieldName) {
        return getDeclaredField(cls, fieldName, false);
    }
    
    public static Field getDeclaredField(final Class<?> cls, final String fieldName, final boolean forceAccess) {
        Validate.isTrue(cls != null, "The class must not be null", new Object[0]);
        Validate.isTrue(StringUtils.isNotBlank(fieldName), "The field name must not be blank/empty", new Object[0]);
        try {
            final Field field = cls.getDeclaredField(fieldName);
            if (!MemberUtils.isAccessible(field)) {
                if (!forceAccess) {
                    return null;
                }
                field.setAccessible(true);
            }
            return field;
        }
        catch (NoSuchFieldException e) {
            return null;
        }
    }
    
    public static Field[] getAllFields(final Class<?> cls) {
        final List<Field> allFieldsList = getAllFieldsList(cls);
        return allFieldsList.toArray(new Field[allFieldsList.size()]);
    }
    
    public static List<Field> getAllFieldsList(final Class<?> cls) {
        Validate.isTrue(cls != null, "The class must not be null", new Object[0]);
        final List<Field> allFields = new ArrayList<Field>();
        for (Class<?> currentClass = cls; currentClass != null; currentClass = currentClass.getSuperclass()) {
            final Field[] arr$;
            final Field[] declaredFields = arr$ = currentClass.getDeclaredFields();
            for (final Field field : arr$) {
                allFields.add(field);
            }
        }
        return allFields;
    }
    
    public static Object readStaticField(final Field field) throws IllegalAccessException {
        return readStaticField(field, false);
    }
    
    public static Object readStaticField(final Field field, final boolean forceAccess) throws IllegalAccessException {
        Validate.isTrue(field != null, "The field must not be null", new Object[0]);
        Validate.isTrue(Modifier.isStatic(field.getModifiers()), "The field '%s' is not static", field.getName());
        return readField(field, (Object)null, forceAccess);
    }
    
    public static Object readStaticField(final Class<?> cls, final String fieldName) throws IllegalAccessException {
        return readStaticField(cls, fieldName, false);
    }
    
    public static Object readStaticField(final Class<?> cls, final String fieldName, final boolean forceAccess) throws IllegalAccessException {
        final Field field = getField(cls, fieldName, forceAccess);
        Validate.isTrue(field != null, "Cannot locate field '%s' on %s", fieldName, cls);
        return readStaticField(field, false);
    }
    
    public static Object readDeclaredStaticField(final Class<?> cls, final String fieldName) throws IllegalAccessException {
        return readDeclaredStaticField(cls, fieldName, false);
    }
    
    public static Object readDeclaredStaticField(final Class<?> cls, final String fieldName, final boolean forceAccess) throws IllegalAccessException {
        final Field field = getDeclaredField(cls, fieldName, forceAccess);
        Validate.isTrue(field != null, "Cannot locate declared field %s.%s", cls.getName(), fieldName);
        return readStaticField(field, false);
    }
    
    public static Object readField(final Field field, final Object target) throws IllegalAccessException {
        return readField(field, target, false);
    }
    
    public static Object readField(final Field field, final Object target, final boolean forceAccess) throws IllegalAccessException {
        Validate.isTrue(field != null, "The field must not be null", new Object[0]);
        if (forceAccess && !field.isAccessible()) {
            field.setAccessible(true);
        }
        else {
            MemberUtils.setAccessibleWorkaround(field);
        }
        return field.get(target);
    }
    
    public static Object readField(final Object target, final String fieldName) throws IllegalAccessException {
        return readField(target, fieldName, false);
    }
    
    public static Object readField(final Object target, final String fieldName, final boolean forceAccess) throws IllegalAccessException {
        Validate.isTrue(target != null, "target object must not be null", new Object[0]);
        final Class<?> cls = target.getClass();
        final Field field = getField(cls, fieldName, forceAccess);
        Validate.isTrue(field != null, "Cannot locate field %s on %s", fieldName, cls);
        return readField(field, target, false);
    }
    
    public static Object readDeclaredField(final Object target, final String fieldName) throws IllegalAccessException {
        return readDeclaredField(target, fieldName, false);
    }
    
    public static Object readDeclaredField(final Object target, final String fieldName, final boolean forceAccess) throws IllegalAccessException {
        Validate.isTrue(target != null, "target object must not be null", new Object[0]);
        final Class<?> cls = target.getClass();
        final Field field = getDeclaredField(cls, fieldName, forceAccess);
        Validate.isTrue(field != null, "Cannot locate declared field %s.%s", cls, fieldName);
        return readField(field, target, false);
    }
    
    public static void writeStaticField(final Field field, final Object value) throws IllegalAccessException {
        writeStaticField(field, value, false);
    }
    
    public static void writeStaticField(final Field field, final Object value, final boolean forceAccess) throws IllegalAccessException {
        Validate.isTrue(field != null, "The field must not be null", new Object[0]);
        Validate.isTrue(Modifier.isStatic(field.getModifiers()), "The field %s.%s is not static", field.getDeclaringClass().getName(), field.getName());
        writeField(field, (Object)null, value, forceAccess);
    }
    
    public static void writeStaticField(final Class<?> cls, final String fieldName, final Object value) throws IllegalAccessException {
        writeStaticField(cls, fieldName, value, false);
    }
    
    public static void writeStaticField(final Class<?> cls, final String fieldName, final Object value, final boolean forceAccess) throws IllegalAccessException {
        final Field field = getField(cls, fieldName, forceAccess);
        Validate.isTrue(field != null, "Cannot locate field %s on %s", fieldName, cls);
        writeStaticField(field, value, false);
    }
    
    public static void writeDeclaredStaticField(final Class<?> cls, final String fieldName, final Object value) throws IllegalAccessException {
        writeDeclaredStaticField(cls, fieldName, value, false);
    }
    
    public static void writeDeclaredStaticField(final Class<?> cls, final String fieldName, final Object value, final boolean forceAccess) throws IllegalAccessException {
        final Field field = getDeclaredField(cls, fieldName, forceAccess);
        Validate.isTrue(field != null, "Cannot locate declared field %s.%s", cls.getName(), fieldName);
        writeField(field, (Object)null, value, false);
    }
    
    public static void writeField(final Field field, final Object target, final Object value) throws IllegalAccessException {
        writeField(field, target, value, false);
    }
    
    public static void writeField(final Field field, final Object target, final Object value, final boolean forceAccess) throws IllegalAccessException {
        Validate.isTrue(field != null, "The field must not be null", new Object[0]);
        if (forceAccess && !field.isAccessible()) {
            field.setAccessible(true);
        }
        else {
            MemberUtils.setAccessibleWorkaround(field);
        }
        field.set(target, value);
    }
    
    public static void removeFinalModifier(final Field field) {
        removeFinalModifier(field, true);
    }
    
    public static void removeFinalModifier(final Field field, final boolean forceAccess) {
        Validate.isTrue(field != null, "The field must not be null", new Object[0]);
        try {
            if (Modifier.isFinal(field.getModifiers())) {
                final Field modifiersField = Field.class.getDeclaredField("modifiers");
                final boolean doForceAccess = forceAccess && !modifiersField.isAccessible();
                if (doForceAccess) {
                    modifiersField.setAccessible(true);
                }
                try {
                    modifiersField.setInt(field, field.getModifiers() & 0xFFFFFFEF);
                }
                finally {
                    if (doForceAccess) {
                        modifiersField.setAccessible(false);
                    }
                }
            }
        }
        catch (NoSuchFieldException ignored) {}
        catch (IllegalAccessException ex) {}
    }
    
    public static void writeField(final Object target, final String fieldName, final Object value) throws IllegalAccessException {
        writeField(target, fieldName, value, false);
    }
    
    public static void writeField(final Object target, final String fieldName, final Object value, final boolean forceAccess) throws IllegalAccessException {
        Validate.isTrue(target != null, "target object must not be null", new Object[0]);
        final Class<?> cls = target.getClass();
        final Field field = getField(cls, fieldName, forceAccess);
        Validate.isTrue(field != null, "Cannot locate declared field %s.%s", cls.getName(), fieldName);
        writeField(field, target, value, false);
    }
    
    public static void writeDeclaredField(final Object target, final String fieldName, final Object value) throws IllegalAccessException {
        writeDeclaredField(target, fieldName, value, false);
    }
    
    public static void writeDeclaredField(final Object target, final String fieldName, final Object value, final boolean forceAccess) throws IllegalAccessException {
        Validate.isTrue(target != null, "target object must not be null", new Object[0]);
        final Class<?> cls = target.getClass();
        final Field field = getDeclaredField(cls, fieldName, forceAccess);
        Validate.isTrue(field != null, "Cannot locate declared field %s.%s", cls.getName(), fieldName);
        writeField(field, target, value, false);
    }
}
