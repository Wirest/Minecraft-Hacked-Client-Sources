// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component;

public interface ProgressBar extends Component, BoundedRangeComponent
{
    boolean isIndeterminate();
    
    void setIndeterminate(final boolean p0);
}
