// 
// Decompiled by Procyon v0.5.30
// 

package cf.euphoria.euphorical.Mod;

import java.util.ArrayList;
import org.darkstorm.minecraft.gui.component.BoundedRangeComponent;

public class NumValue
{
    private double value;
    private double min;
    private double max;
    private String name;
    private BoundedRangeComponent.ValueDisplay valueDisplay;
    private static ArrayList<NumValue> vals;
    
    static {
        NumValue.vals = new ArrayList<NumValue>();
    }
    
    public static ArrayList<NumValue> getVals() {
        return NumValue.vals;
    }
    
    public NumValue(final String name, final double value, final double min, final double max, final BoundedRangeComponent.ValueDisplay valueDisplay) {
        this.setName(name);
        this.setValue(value);
        this.setMin(min);
        this.setMax(max);
        this.setValueDisplay(valueDisplay);
        getVals().add(this);
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
    
    public void setValue(final double value) {
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
