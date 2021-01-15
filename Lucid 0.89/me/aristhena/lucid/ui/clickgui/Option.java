/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.ui.clickgui;

import java.util.ArrayList;
import java.util.List;
import me.aristhena.lucid.management.module.Module;

public class Option
{
    public Module parent;
    public Type type;
    public String title;
    public Object value;
    public double[] limit;
    public double inc;
    public List<Option> options;
    
    public Option(final Module parent, final Type type, final String title, final Object value, final double[] limit, final double inc) {
        this.options = new ArrayList<Option>();
        this.parent = parent;
        this.type = type;
        this.title = title;
        this.value = value;
        this.limit = limit;
        this.inc = inc;
    }
    
    public Option(final Module parent, final Type type, final String title, final Object value, final double[] limit, final float inc, final List<Option> options) {
        this(parent, type, title, value, limit, inc);
        this.options = options;
    }
    
    public boolean valBool() {
        try {
            return Boolean.parseBoolean(this.value.toString());
        }
        catch (Exception e) {
            return false;
        }
    }
    
    public float valFloat() {
        try {
            return Float.parseFloat(this.value.toString());
        }
        catch (Exception e) {
            return -1.0f;
        }
    }
    
    public int valInt() {
        try {
            return Integer.parseInt(this.value.toString());
        }
        catch (Exception e) {
            return -1;
        }
    }
    
    public String valString() {
        try {
            return this.value.toString();
        }
        catch (Exception e) {
            return "";
        }
    }
    
    public enum Type
    {
        bool("bool", 0, "<true|false>"), 
        floa("floa", 1, "<value>"), 
        inte("inte", 2, "<value>"), 
        keyb("keyb", 3, "<key>");
        
        public String usage;
        
        private Type(final String s, final int n, final String usage) {
            this.usage = usage;
        }
    }
}
