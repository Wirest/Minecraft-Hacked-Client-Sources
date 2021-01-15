// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component;

import org.darkstorm.minecraft.gui.listener.SelectableComponentListener;

public interface SelectableComponent extends Component
{
    boolean isSelected();
    
    void setSelected(final boolean p0);
    
    void addSelectableComponentListener(final SelectableComponentListener p0);
    
    void removeSelectableComponentListener(final SelectableComponentListener p0);
}
