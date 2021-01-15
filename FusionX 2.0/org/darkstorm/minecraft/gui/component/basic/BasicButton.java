// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component.basic;

import org.darkstorm.minecraft.gui.listener.ComponentListener;
import org.darkstorm.minecraft.gui.listener.ButtonListener;
import org.darkstorm.minecraft.gui.component.ButtonGroup;
import org.darkstorm.minecraft.gui.component.Button;
import org.darkstorm.minecraft.gui.component.AbstractComponent;

public class BasicButton extends AbstractComponent implements Button
{
    protected String text;
    protected ButtonGroup group;
    
    public BasicButton() {
        this.text = "";
    }
    
    public BasicButton(final String text) {
        this.text = "";
        this.text = text;
    }
    
    @Override
    public String getText() {
        return this.text;
    }
    
    @Override
    public void setText(final String text) {
        this.text = text;
    }
    
    @Override
    public void press() {
        ComponentListener[] listeners;
        for (int length = (listeners = this.getListeners()).length, i = 0; i < length; ++i) {
            final ComponentListener listener = listeners[i];
            ((ButtonListener)listener).onButtonPress(this);
        }
    }
    
    @Override
    public void addButtonListener(final ButtonListener listener) {
        this.addListener(listener);
    }
    
    @Override
    public void removeButtonListener(final ButtonListener listener) {
        this.removeListener(listener);
    }
    
    @Override
    public ButtonGroup getGroup() {
        return this.group;
    }
    
    @Override
    public void setGroup(final ButtonGroup group) {
        this.group = group;
    }
}
