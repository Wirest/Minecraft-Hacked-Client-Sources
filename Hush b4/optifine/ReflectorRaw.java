// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;

public class ReflectorRaw
{
    public static Field getField(final Class p_getField_0_, final Class p_getField_1_) {
        try {
            final Field[] afield = p_getField_0_.getDeclaredFields();
            for (int i = 0; i < afield.length; ++i) {
                final Field field = afield[i];
                if (field.getType() == p_getField_1_) {
                    field.setAccessible(true);
                    return field;
                }
            }
            return null;
        }
        catch (Exception var5) {
            return null;
        }
    }
    
    public static Field[] getFields(final Class p_getFields_0_, final Class p_getFields_1_) {
        try {
            final Field[] afield = p_getFields_0_.getDeclaredFields();
            return getFields(afield, p_getFields_1_);
        }
        catch (Exception var3) {
            return null;
        }
    }
    
    public static Field[] getFields(final Field[] p_getFields_0_, final Class p_getFields_1_) {
        try {
            final List list = new ArrayList();
            for (int i = 0; i < p_getFields_0_.length; ++i) {
                final Field field = p_getFields_0_[i];
                if (field.getType() == p_getFields_1_) {
                    field.setAccessible(true);
                    list.add(field);
                }
            }
            final Field[] afield = list.toArray(new Field[list.size()]);
            return afield;
        }
        catch (Exception var5) {
            return null;
        }
    }
    
    public static Field[] getFieldsAfter(final Class p_getFieldsAfter_0_, final Field p_getFieldsAfter_1_, final Class p_getFieldsAfter_2_) {
        try {
            final Field[] afield = p_getFieldsAfter_0_.getDeclaredFields();
            final List<Field> list = Arrays.asList(afield);
            final int i = list.indexOf(p_getFieldsAfter_1_);
            if (i < 0) {
                return new Field[0];
            }
            final List<Field> list2 = list.subList(i + 1, list.size());
            final Field[] afield2 = list2.toArray(new Field[list2.size()]);
            return getFields(afield2, p_getFieldsAfter_2_);
        }
        catch (Exception var8) {
            return null;
        }
    }
    
    public static Field[] getFields(final Object p_getFields_0_, final Field[] p_getFields_1_, final Class p_getFields_2_, final Object p_getFields_3_) {
        try {
            final List<Field> list = new ArrayList<Field>();
            for (int i = 0; i < p_getFields_1_.length; ++i) {
                final Field field = p_getFields_1_[i];
                if (field.getType() == p_getFields_2_) {
                    final boolean flag = Modifier.isStatic(field.getModifiers());
                    if ((p_getFields_0_ != null || flag) && (p_getFields_0_ == null || !flag)) {
                        field.setAccessible(true);
                        final Object object = field.get(p_getFields_0_);
                        if (object == p_getFields_3_) {
                            list.add(field);
                        }
                        else if (object != null && p_getFields_3_ != null && object.equals(p_getFields_3_)) {
                            list.add(field);
                        }
                    }
                }
            }
            final Field[] afield = list.toArray(new Field[list.size()]);
            return afield;
        }
        catch (Exception var9) {
            return null;
        }
    }
    
    public static Field getField(final Class p_getField_0_, final Class p_getField_1_, final int p_getField_2_) {
        final Field[] afield = getFields(p_getField_0_, p_getField_1_);
        return (p_getField_2_ >= 0 && p_getField_2_ < afield.length) ? afield[p_getField_2_] : null;
    }
    
    public static Field getFieldAfter(final Class p_getFieldAfter_0_, final Field p_getFieldAfter_1_, final Class p_getFieldAfter_2_, final int p_getFieldAfter_3_) {
        final Field[] afield = getFieldsAfter(p_getFieldAfter_0_, p_getFieldAfter_1_, p_getFieldAfter_2_);
        return (p_getFieldAfter_3_ >= 0 && p_getFieldAfter_3_ < afield.length) ? afield[p_getFieldAfter_3_] : null;
    }
    
    public static Object getFieldValue(final Object p_getFieldValue_0_, final Class p_getFieldValue_1_, final Class p_getFieldValue_2_) {
        final ReflectorField reflectorfield = getReflectorField(p_getFieldValue_1_, p_getFieldValue_2_);
        return (reflectorfield == null) ? null : (reflectorfield.exists() ? Reflector.getFieldValue(p_getFieldValue_0_, reflectorfield) : null);
    }
    
    public static Object getFieldValue(final Object p_getFieldValue_0_, final Class p_getFieldValue_1_, final Class p_getFieldValue_2_, final int p_getFieldValue_3_) {
        final ReflectorField reflectorfield = getReflectorField(p_getFieldValue_1_, p_getFieldValue_2_, p_getFieldValue_3_);
        return (reflectorfield == null) ? null : (reflectorfield.exists() ? Reflector.getFieldValue(p_getFieldValue_0_, reflectorfield) : null);
    }
    
    public static boolean setFieldValue(final Object p_setFieldValue_0_, final Class p_setFieldValue_1_, final Class p_setFieldValue_2_, final Object p_setFieldValue_3_) {
        final ReflectorField reflectorfield = getReflectorField(p_setFieldValue_1_, p_setFieldValue_2_);
        return reflectorfield != null && reflectorfield.exists() && Reflector.setFieldValue(p_setFieldValue_0_, reflectorfield, p_setFieldValue_3_);
    }
    
    public static boolean setFieldValue(final Object p_setFieldValue_0_, final Class p_setFieldValue_1_, final Class p_setFieldValue_2_, final int p_setFieldValue_3_, final Object p_setFieldValue_4_) {
        final ReflectorField reflectorfield = getReflectorField(p_setFieldValue_1_, p_setFieldValue_2_, p_setFieldValue_3_);
        return reflectorfield != null && reflectorfield.exists() && Reflector.setFieldValue(p_setFieldValue_0_, reflectorfield, p_setFieldValue_4_);
    }
    
    public static ReflectorField getReflectorField(final Class p_getReflectorField_0_, final Class p_getReflectorField_1_) {
        final Field field = getField(p_getReflectorField_0_, p_getReflectorField_1_);
        if (field == null) {
            return null;
        }
        final ReflectorClass reflectorclass = new ReflectorClass(p_getReflectorField_0_);
        return new ReflectorField(reflectorclass, field.getName());
    }
    
    public static ReflectorField getReflectorField(final Class p_getReflectorField_0_, final Class p_getReflectorField_1_, final int p_getReflectorField_2_) {
        final Field field = getField(p_getReflectorField_0_, p_getReflectorField_1_, p_getReflectorField_2_);
        if (field == null) {
            return null;
        }
        final ReflectorClass reflectorclass = new ReflectorClass(p_getReflectorField_0_);
        return new ReflectorField(reflectorclass, field.getName());
    }
}
