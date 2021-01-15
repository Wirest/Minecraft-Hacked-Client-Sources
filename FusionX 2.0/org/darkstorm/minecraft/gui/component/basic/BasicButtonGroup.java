// 
// Decompiled by Procyon v0.5.30
// 

package org.darkstorm.minecraft.gui.component.basic;

import java.util.ArrayList;
import org.darkstorm.minecraft.gui.component.Button;
import java.util.List;
import org.darkstorm.minecraft.gui.component.ButtonGroup;

public class BasicButtonGroup implements ButtonGroup
{
    private List<Button> buttons;
    
    public BasicButtonGroup() {
        this.buttons = new ArrayList<Button>();
    }
    
    @Override
    public void addButton(final Button button) {
        if (button == null) {
            throw new NullPointerException();
        }
        synchronized (this.buttons) {
            this.buttons.add(button);
        }
        // monitorexit(this.buttons)
    }
    
    @Override
    public void removeButton(final Button button) {
        if (button == null) {
            throw new NullPointerException();
        }
        synchronized (this.buttons) {
            this.buttons.remove(button);
        }
        // monitorexit(this.buttons)
    }
    
    @Override
    public Button[] getButtons() {
        synchronized (this.buttons) {
            // monitorexit(this.buttons)
            return this.buttons.toArray(new Button[this.buttons.size()]);
        }
    }
}
