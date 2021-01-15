/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.management.value;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.value.Val;
import me.aristhena.lucid.management.value.ValueManager;

public class Value {
    public Module mod;
    public String name;
    public double value;
    public double min;
    public double max;
    public double increment;

    public Value(Module mod, String name, double value, double[] limit, double increment) {
        this.mod = mod;
        this.name = name;
        this.value = value;
        this.increment = increment;
        this.min = limit[0];
        this.max = limit[1];
    }

    public void setValue(double value) {
        this.value = value;
        Field[] arrfield = this.mod.getClass().getDeclaredFields();
        int n = arrfield.length;
        int n2 = 0;
        while (n2 < n) {
            Field field = arrfield[n2];
            field.setAccessible(true);
            if (field.isAnnotationPresent(Val.class) && field.getName().equalsIgnoreCase(this.name)) {
                try {
                    field.setDouble(this.mod, value);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ++n2;
        }
        ValueManager.save();
    }

    public void inc() {
        this.setValue(this.value + this.increment > this.max ? this.max : this.value + this.increment);
    }

    public void deinc() {
        this.setValue(this.value - this.increment < this.min ? this.min : this.value - this.increment);
    }
}

