// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component;

import org.darkstorm.minecraft.gui.listener.ComboBoxListener;

public interface ComboBox extends Component, SelectableComponent
{
    String[] getElements();
    
    void setElements(final String... p0);
    
    int getSelectedIndex();
    
    void setSelectedIndex(final int p0);
    
    String getSelectedElement();
    
    void addComboBoxListener(final ComboBoxListener p0);
    
    void removeComboBoxListener(final ComboBoxListener p0);
}
