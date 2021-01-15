// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.render.hud.tabgui;

import me.aristhena.event.events.KeyPressEvent;
import me.aristhena.event.events.Render2DEvent;
import java.util.Iterator;
import me.aristhena.client.option.Option;
import me.aristhena.client.option.OptionManager;
import me.aristhena.client.module.Module;
import me.aristhena.client.option.types.BooleanOption;

public class TabGui extends BooleanOption
{
    public TabGui(final String name, final boolean value, final Module module) {
        super(name, name, value, module, true);
    }
    
    @Override
    public void setValue(final Boolean value) {
        if (value) {
            for (final Option option : OptionManager.getOptionList()) {
                if (option.getModule().equals(this.getModule()) && option instanceof TabGui) {
                    ((BooleanOption)option).setValueHard(false);
                }
            }
        }
        else {
            for (final Option option : OptionManager.getOptionList()) {
                if (option.getModule().equals(this.getModule()) && option instanceof TabGui && option != this) {
                    ((BooleanOption)option).setValueHard(true);
                    break;
                }
            }
        }
        super.setValue(value);
    }
    
    public boolean setupSizes() {
        return this.getValue();
    }
    
    public boolean onRender(final Render2DEvent event) {
        return this.getValue();
    }
    
    public boolean onKeypress(final KeyPressEvent event) {
        return this.getValue();
    }
}
