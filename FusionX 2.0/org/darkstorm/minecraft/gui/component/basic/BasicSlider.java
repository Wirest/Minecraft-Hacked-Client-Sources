// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component.basic;

import org.darkstorm.minecraft.gui.listener.ComponentListener;
import org.darkstorm.minecraft.gui.listener.SliderListener;
import org.darkstorm.minecraft.gui.component.BoundedRangeComponent;
import org.darkstorm.minecraft.gui.component.Slider;
import org.darkstorm.minecraft.gui.component.AbstractComponent;

public class BasicSlider extends AbstractComponent implements Slider
{
    private String text;
    private String suffix;
    private double value;
    private double minimum;
    private double maximum;
    private double increment;
    private BoundedRangeComponent.ValueDisplay display;
    private boolean changing;
    private double startValue;
    
    public BasicSlider() {
        this("");
    }
    
    public BasicSlider(final String text) {
        this(text, 0.0);
    }
    
    public BasicSlider(final String text, final double value) {
        this(text, value, 0.0, 1000.0);
    }
    
    public BasicSlider(final String text, final double value, final double minimum, final double maximum) {
        this(text, value, minimum, maximum, 1);
    }
    
    public BasicSlider(final String text, final double value, final double minimum, final double maximum, final int increment) {
        this(text, value, minimum, maximum, increment, BoundedRangeComponent.ValueDisplay.DECIMAL);
    }
    
    public BasicSlider(final String text, double value, final double minimum, final double maximum, final double increment, final BoundedRangeComponent.ValueDisplay display) {
        this.changing = false;
        this.text = ((text != null) ? text : "");
        this.minimum = Math.max(0.0, Math.min(minimum, maximum));
        this.maximum = Math.max(0.0, Math.max(minimum, maximum));
        value = Math.max(minimum, Math.min(maximum, value));
        this.value = value - Math.round(value % increment / increment) * increment;
        this.increment = Math.min(maximum, Math.max(5.0E-4, increment));
        this.display = ((display != null) ? display : BoundedRangeComponent.ValueDisplay.DECIMAL);
    }
    
    @Override
    public String getText() {
        return this.text;
    }
    
    @Override
    public void setText(final String text) {
        this.text = ((text != null) ? text : "");
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
    public boolean isValueChanging() {
        return this.changing;
    }
    
    @Override
    public String getContentSuffix() {
        return this.suffix;
    }
    
    @Override
    public void setValue(double value) {
        final double oldValue = this.value;
        value = Math.max(this.minimum, Math.min(this.maximum, value));
        this.value = value - Math.round(value % this.increment / this.increment) * this.increment;
        if (!this.changing && oldValue != this.value) {
            this.fireChange();
        }
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
        this.increment = Math.min(this.maximum, Math.max(0.0, increment));
        this.setValue(this.value);
    }
    
    @Override
    public void setValueDisplay(final BoundedRangeComponent.ValueDisplay display) {
        this.display = ((display != null) ? display : BoundedRangeComponent.ValueDisplay.DECIMAL);
    }
    
    @Override
    public void setValueChanging(final boolean changing) {
        if (changing != this.changing) {
            this.changing = changing;
            if (changing) {
                this.startValue = this.value;
            }
            else if (this.startValue != this.value) {
                this.fireChange();
            }
        }
    }
    
    @Override
    public void setContentSuffix(final String suffix) {
        this.suffix = suffix;
    }
    
    @Override
    public void addSliderListener(final SliderListener listener) {
        this.addListener(listener);
    }
    
    @Override
    public void removeSliderListener(final SliderListener listener) {
        this.removeListener(listener);
    }
    
    private void fireChange() {
        ComponentListener[] listeners;
        for (int length = (listeners = this.getListeners()).length, i = 0; i < length; ++i) {
            final ComponentListener listener = listeners[i];
            if (listener instanceof SliderListener) {
                ((SliderListener)listener).onSliderValueChanged(this);
            }
        }
    }
}
