/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.management.option;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.option.Op;
import me.aristhena.lucid.management.option.OptionManager;
import me.aristhena.lucid.ui.clickgui.Gui;

public class Option {
    public String name;
    public boolean value;
    public Module mod;

    public Option(String name, boolean value, Module mod) {
        this.name = name;
        this.value = value;
        this.mod = mod;
    }

    public void setValue(boolean value) {
        this.value = value;
        Field[] arrfield = this.mod.getClass().getDeclaredFields();
        int n = arrfield.length;
        int n2 = 0;
        while (n2 < n) {
            Field field = arrfield[n2];
            field.setAccessible(true);
            if (field.isAnnotationPresent(Op.class) && field.getName().equalsIgnoreCase(this.name)) {
                try {
                    field.setBoolean(this.mod, value);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ++n2;
        }
        if (Gui.instance != null) {
            Gui.instance.reloadOptions();
        }
        OptionManager.save();
    }

    public void setValueHard(boolean value) {
        this.value = value;
        Field[] arrfield = this.mod.getClass().getDeclaredFields();
        int n = arrfield.length;
        int n2 = 0;
        while (n2 < n) {
            Field field = arrfield[n2];
            field.setAccessible(true);
            if (field.isAnnotationPresent(Op.class) && field.getName().equalsIgnoreCase(this.name)) {
                try {
                    field.setBoolean(this.mod, value);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            ++n2;
        }
        if (Gui.instance != null) {
            Gui.instance.reloadOptions();
        }
        OptionManager.save();
    }

    public void toggle() {
        this.setValue(!this.value);
    }
}

