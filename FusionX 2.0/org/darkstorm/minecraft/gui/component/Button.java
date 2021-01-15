// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component;

import org.darkstorm.minecraft.gui.listener.ButtonListener;

public interface Button extends Component, TextComponent
{
    void press();
    
    void addButtonListener(final ButtonListener p0);
    
    void removeButtonListener(final ButtonListener p0);
    
    ButtonGroup getGroup();
    
    void setGroup(final ButtonGroup p0);
}
