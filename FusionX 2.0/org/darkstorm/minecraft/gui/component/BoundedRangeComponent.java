// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component;

public interface BoundedRangeComponent extends Component
{
    double getValue();
    
    double getMinimumValue();
    
    double getMaximumValue();
    
    double getIncrement();
    
    ValueDisplay getValueDisplay();
    
    void setValue(final double p0);
    
    void setMinimumValue(final double p0);
    
    void setMaximumValue(final double p0);
    
    void setIncrement(final double p0);
    
    void setValueDisplay(final ValueDisplay p0);
    
    public enum ValueDisplay
    {
        DECIMAL("DECIMAL", 0), 
        INTEGER("INTEGER", 1), 
        PERCENTAGE("PERCENTAGE", 2), 
        NONE("NONE", 3);
        
        private ValueDisplay(final String s, final int n) {
        }
    }
}
