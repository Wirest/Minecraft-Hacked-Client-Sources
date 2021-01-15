// 
// Decompiled by Procyon v0.5.30
// 

package me.CheerioFX.FusionX.values;

import me.CheerioFX.FusionX.FusionX;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.darkstorm.minecraft.gui.component.BoundedRangeComponent;

public class Value
{
    private double value;
    private double min;
    private double max;
    private String name;
    private String frameid;
    private BoundedRangeComponent.ValueDisplay valueDisplay;
    private static ArrayList<Value> vals;
    
    static {
        Value.vals = new ArrayList<Value>();
    }
    
    public static ArrayList<Value> getVals() {
        return Value.vals;
    }
    
    public Value(final String name, final double value, final double min, final double max, final BoundedRangeComponent.ValueDisplay valueDisplay, final String frameid) {
        this.setName(name);
        this.setValue(value, true);
        this.setMin(min);
        this.setMax(max);
        this.setValueDisplay(valueDisplay);
        this.setId(frameid);
        getVals().add(this);
    }
    
    public String getId() {
        return this.frameid;
    }
    
    public void setId(final String id) {
        this.frameid = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public BoundedRangeComponent.ValueDisplay getValueDisplay() {
        return this.valueDisplay;
    }
    
    public void setValueDisplay(final BoundedRangeComponent.ValueDisplay valueDisplay) {
        this.valueDisplay = valueDisplay;
    }
    
    public double getValue() {
        return this.value;
    }
    
    public float getValueF() {
        return (float)this.value;
    }
    
    public int getValueI() {
        final DecimalFormat decimalFormat = new DecimalFormat("###");
        return Integer.parseInt(decimalFormat.format(this.value));
    }
    
    public void setValue(final double value) {
        this.value = value;
        try {
            FusionX.theClient.fileManager.saveValues();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setValue(final double value, final boolean setup) {
        final DecimalFormat decimalFormat = new DecimalFormat("####.##");
        final double o = Double.parseDouble(decimalFormat.format(value));
        this.value = value;
    }
    
    public double getMin() {
        return this.min;
    }
    
    public void setMin(final double min) {
        this.min = min;
    }
    
    public double getMax() {
        return this.max;
    }
    
    public void setMax(final double max) {
        this.max = max;
    }
}
