// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component.basic;

import org.darkstorm.minecraft.gui.listener.ComponentListener;
import org.darkstorm.minecraft.gui.component.SelectableComponent;
import org.darkstorm.minecraft.gui.listener.SelectableComponentListener;
import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.RadioButton;

public class BasicRadioButton extends BasicButton implements RadioButton
{
    private boolean selected;
    
    public BasicRadioButton() {
        this.selected = false;
    }
    
    public BasicRadioButton(final String text) {
        this.selected = false;
        this.text = text;
    }
    
    @Override
    public void press() {
        this.selected = true;
        Button[] buttons;
        for (int length = (buttons = this.getGroup().getButtons()).length, i = 0; i < length; ++i) {
            final Button button = buttons[i];
            if (!this.equals(button)) {
                if (button instanceof RadioButton) {
                    ((RadioButton)button).setSelected(false);
                }
            }
        }
        super.press();
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
    public void addSelectableComponentListener(final SelectableComponentListener listener) {
        this.addListener(listener);
    }
    
    @Override
    public void removeSelectableComponentListener(final SelectableComponentListener listener) {
        this.removeListener(listener);
    }
}
