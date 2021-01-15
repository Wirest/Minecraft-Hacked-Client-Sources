// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component.basic;

import org.darkstorm.minecraft.gui.component.BoundedRangeComponent;
import org.darkstorm.minecraft.gui.component.ProgressBar;
import org.darkstorm.minecraft.gui.component.AbstractComponent;

public class BasicProgressBar extends AbstractComponent implements ProgressBar
{
    private double value;
    private double minimum;
    private double maximum;
    private double increment;
    private BoundedRangeComponent.ValueDisplay display;
    private boolean indeterminate;
    
    public BasicProgressBar() {
        this(0.0);
    }
    
    public BasicProgressBar(final double value) {
        this(value, 0.0, 100.0);
    }
    
    public BasicProgressBar(final double value, final double minimum, final double maximum) {
        this(value, minimum, maximum, 1);
    }
    
    public BasicProgressBar(final double value, final double minimum, final double maximum, final int increment) {
        this(value, minimum, maximum, increment, BoundedRangeComponent.ValueDisplay.NONE);
    }
    
    public BasicProgressBar(double value, final double minimum, final double maximum, final double increment, final BoundedRangeComponent.ValueDisplay display) {
        this.minimum = Math.max(0.0, Math.min(minimum, maximum));
        this.maximum = Math.max(0.0, Math.max(minimum, maximum));
        value = Math.max(minimum, Math.min(maximum, value));
        this.value = value - Math.round(value % increment / increment) * increment;
        this.increment = Math.min(maximum, Math.max(5.0E-4, increment));
        this.display = ((display != null) ? display : BoundedRangeComponent.ValueDisplay.NONE);
    }
    
    @Override
    public double getValue() {
        return this.value;
    }
    
    @Override
    public double getMinimumValue() {
        return this.minimum;
    }
    
    @Override
    public double getMaximumValue() {
        return this.maximum;
    }
    
    @Override
    public double getIncrement() {
        return this.increment;
    }
    
    @Override
    public BoundedRangeComponent.ValueDisplay getValueDisplay() {
        return this.display;
    }
    
    @Override
    public boolean isIndeterminate() {
        return this.indeterminate;
    }
    
    @Override
    public void setValue(double value) {
        value = Math.max(this.minimum, Math.min(this.maximum, value));
        this.value = value - Math.round(value % this.increment / this.increment) * this.increment;
    }
    
    @Override
    public void setMinimumValue(final double minimum) {
        this.minimum = Math.max(0.0, Math.min(this.maximum, minimum));
        this.setValue(this.value);
    }
    
    @Override
    public void setMaximumValue(final double maximum) {
        this.maximum = Math.max(maximum, this.minimum);
        this.setValue(this.value);
    }
    
    @Override
    public void setIncrement(final double increment) {
        this.increment = Math.min(this.maximum, Math.max(5.0E-4, increment));
        this.setValue(this.value);
    }
    
    @Override
    public void setValueDisplay(final BoundedRangeComponent.ValueDisplay display) {
        this.display = ((display != null) ? display : BoundedRangeComponent.ValueDisplay.NONE);
    }
    
    @Override
    public void setIndeterminate(final boolean indeterminate) {
        this.indeterminate = indeterminate;
    }
}
