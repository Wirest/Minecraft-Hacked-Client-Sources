// 
// Decompiled by Procyon v0.5.36
// 

package de.Hero.settings;

import java.util.ArrayList;
import me.nico.hush.modules.Module;

public class Setting
{
    private String name;
    private String display;
    private Module parent;
    private String mode;
    private String sval;
    private ArrayList<String> options;
    private boolean bval;
    private double dval;
    private double min;
    private double max;
    private boolean onlyint;
    
    public Setting(final String name, final Module parent, final String sval, final ArrayList<String> options) {
        this.onlyint = false;
        this.name = name;
        this.parent = parent;
        this.sval = sval;
        this.options = options;
        this.mode = "Combo";
    }
    
    public Setting(final String display, final String name, final Module parent, final String sval, final ArrayList<String> options) {
        this.onlyint = false;
        this.name = name;
        this.parent = parent;
        this.sval = sval;
        this.options = options;
        this.display = display;
        this.mode = "Combo";
    }
    
    public Setting(final String name, final Module parent, final boolean bval) {
        this.onlyint = false;
        this.name = name;
        this.parent = parent;
        this.bval = bval;
        this.mode = "Check";
    }
    
    public Setting(final String display, final String name, final Module parent, final boolean bval) {
        this.onlyint = false;
        this.name = name;
        this.parent = parent;
        this.bval = bval;
        this.display = display;
        this.mode = "Check";
    }
    
    public Setting(final String name, final Module parent, final double dval, final double min, final double max, final boolean onlyint) {
        this.onlyint = false;
        this.name = name;
        this.parent = parent;
        this.dval = dval;
        this.min = min;
        this.max = max;
        this.onlyint = onlyint;
        this.mode = "Slider";
    }
    
    public Setting(final String display, final String name, final Module parent, final double dval, final double min, final double max, final boolean onlyint) {
        this.onlyint = false;
        this.name = name;
        this.parent = parent;
        this.dval = dval;
        this.min = min;
        this.max = max;
        this.onlyint = onlyint;
        this.display = display;
        this.mode = "Slider";
    }
    
    public String getName() {
        return this.name;
    }
    
    public Module getParentMod() {
        return this.parent;
    }
    
    public String getValString() {
        return this.sval;
    }
    
    public void setValString(final String in) {
        this.sval = in;
    }
    
    public ArrayList<String> getOptions() {
        return this.options;
    }
    
    public boolean getValBoolean() {
        return this.bval;
    }
    
    public void setValBoolean(final boolean in) {
        this.bval = in;
    }
    
    public double getValDouble() {
        if (this.onlyint) {
            this.dval = (int)this.dval;
        }
        return this.dval;
    }
    
    public void setValDouble(final double in) {
        this.dval = in;
    }
    
    public double getMin() {
        return this.min;
    }
    
    public double getMax() {
        return this.max;
    }
    
    public boolean isCombo() {
        return this.mode.equalsIgnoreCase("Combo");
    }
    
    public boolean isCheck() {
        return this.mode.equalsIgnoreCase("Check");
    }
    
    public boolean isSlider() {
        return this.mode.equalsIgnoreCase("Slider");
    }
    
    public boolean onlyInt() {
        return this.onlyint;
    }
    
    public String getDisplay() {
        return this.display;
    }
}
