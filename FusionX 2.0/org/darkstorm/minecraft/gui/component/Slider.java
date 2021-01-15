// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component;

import org.darkstorm.minecraft.gui.listener.SliderListener;

public interface Slider extends Component, TextComponent, BoundedRangeComponent
{
    String getContentSuffix();
    
    boolean isValueChanging();
    
    void setContentSuffix(final String p0);
    
    void setValueChanging(final boolean p0);
    
    void addSliderListener(final SliderListener p0);
    
    void removeSliderListener(final SliderListener p0);
}
