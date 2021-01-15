// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.values;

import me.CheerioFX.FusionX.FusionX;
import java.util.ArrayList;

public class BooleanValue
{
    private boolean value;
    private String name;
    private String frameid;
    private static ArrayList<BooleanValue> vals;
    
    static {
        BooleanValue.vals = new ArrayList<BooleanValue>();
    }
    
    public static ArrayList<BooleanValue> getVals() {
        return BooleanValue.vals;
    }
    
    public BooleanValue(final String name, final boolean value, final String frameid) {
        this.setName(name);
        this.setId(frameid);
        this.setValue(value, true);
        getVals().add(this);
    }
    
    public String getId() {
        return this.frameid;
    }
    
    public void setId(final String frameid) {
        this.frameid = frameid;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public boolean getValue() {
        return this.value;
    }
    
    public void setValue(final boolean value) {
        this.value = value;
        try {
            FusionX.theClient.fileManager.saveBooleanValues();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setValue(final boolean value, final boolean setup) {
        this.value = value;
    }
}
