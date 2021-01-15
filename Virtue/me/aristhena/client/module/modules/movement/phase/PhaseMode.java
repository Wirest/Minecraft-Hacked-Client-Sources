package me.aristhena.client.module.modules.movement.phase;

import me.aristhena.client.module.Module;
import me.aristhena.client.option.Option;
import me.aristhena.client.option.OptionManager;
import me.aristhena.client.option.types.BooleanOption;
import me.aristhena.event.events.BoundingBoxEvent;
import me.aristhena.event.events.MoveEvent;
import me.aristhena.event.events.UpdateEvent;

public class PhaseMode extends BooleanOption
{
    public PhaseMode(final String name, final boolean value, final Module module) {
        super(name, name, value, module, value);
    }
    
    @Override
    public void setValue(final Boolean value) {
        if (value) {
            for (final Option option : OptionManager.getOptionList()) {
                if (option.getModule().equals(this.getModule()) && option instanceof PhaseMode) {
                    ((BooleanOption)option).setValueHard(false);
                }
            }
        }
        else {
            for (final Option option : OptionManager.getOptionList()) {
                if (option.getModule().equals(this.getModule()) && option instanceof PhaseMode && option != this) {
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
    
    public boolean onMove(final MoveEvent event) {
        return this.getValue();
    }
    
    public boolean onSetBoundingbox(final BoundingBoxEvent event) {
        return this.getValue();
    }
}
