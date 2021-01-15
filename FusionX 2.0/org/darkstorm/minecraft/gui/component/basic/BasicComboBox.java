// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component.basic;

import org.darkstorm.minecraft.gui.component.SelectableComponent;
import org.darkstorm.minecraft.gui.listener.SelectableComponentListener;
import org.darkstorm.minecraft.gui.listener.ComponentListener;
import org.darkstorm.minecraft.gui.listener.ComboBoxListener;
import org.darkstorm.minecraft.gui.component.ComboBox;
import org.darkstorm.minecraft.gui.component.AbstractComponent;

public class BasicComboBox extends AbstractComponent implements ComboBox
{
    private String[] elements;
    private int selectedIndex;
    private boolean selected;
    
    public BasicComboBox() {
        this.elements = new String[0];
    }
    
    public BasicComboBox(final String... elements) {
        this.elements = elements;
    }
    
    @Override
    public String[] getElements() {
        return this.elements;
    }
    
    @Override
    public void setElements(final String... elements) {
        this.selectedIndex = 0;
        this.elements = elements;
    }
    
    @Override
    public int getSelectedIndex() {
        return this.selectedIndex;
    }
    
    @Override
    public void setSelectedIndex(final int selectedIndex) {
        this.selectedIndex = selectedIndex;
        ComponentListener[] listeners;
        for (int length = (listeners = this.getListeners()).length, i = 0; i < length; ++i) {
            final ComponentListener listener = listeners[i];
            if (listener instanceof ComboBoxListener) {
                try {
                    ((ComboBoxListener)listener).onComboBoxSelectionChanged(this);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public String getSelectedElement() {
        return this.elements[this.selectedIndex];
    }
    
    @Override
    public boolean isSelected() {
        return this.selected;
    }
    
    @Override
    public void setSelected(final boolean selected) {
        this.selected = selected;
        ComponentListener[] listeners;
        for (int length = (listeners = this.getListeners()).length, i = 0; i < length; ++i) {
            final ComponentListener listener = listeners[i];
            if (listener instanceof SelectableComponentListener) {
                try {
                    ((SelectableComponentListener)listener).onSelectedStateChanged(this);
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public void addComboBoxListener(final ComboBoxListener listener) {
        this.addListener(listener);
    }
    
    @Override
    public void removeComboBoxListener(final ComboBoxListener listener) {
        this.removeListener(listener);
    }
    
    @Override
    public void addSelectableComponentListener(final SelectableComponentListener listener) {
        this.addListener(listener);
    }
    
    @Override
    public void removeSelectableComponentListener(final SelectableComponentListener listener) {
        this.removeListener(listener);
    }
}
