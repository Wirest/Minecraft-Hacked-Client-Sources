// 
// Decompiled by Procyon v0.5.30
// 

package me.aristhena.client.module.modules.combat.aura;

import me.aristhena.event.events.UpdateEvent;
import java.util.Iterator;
import me.aristhena.client.option.Option;
import me.aristhena.client.option.OptionManager;
import me.aristhena.client.module.Module;
import me.aristhena.client.option.types.BooleanOption;

public class AuraMode extends BooleanOption
{
    public AuraMode(final String name, final boolean value, final Module module) {
        super(name, name, value, module, true);
    }
    
    @Override
    public void setValue(final Boolean value) {
        if (value) {
            for (final Option option : OptionManager.getOptionList()) {
                if (option.getModule().equals(this.getModule()) && option instanceof AuraMode) {
                    ((BooleanOption)option).setValueHard(false);
                }
            }
        }
        else {
            for (final Option option : OptionManager.getOptionList()) {
                if (option.getModule().equals(this.getModule()) && option instanceof AuraMode && option != this) {
                    ((BooleanOption)option).setValueHard(true);
                    break;
                }
            }
        }
        super.setValue(value);
    }
    
    public boolean enable() {
        return this.getValue();
    }
    
    public boolean onUpdate(final UpdateEvent event) {
        return this.getValue();
    }
    
    public boolean disable() {
        return this.getValue();
    }
}
