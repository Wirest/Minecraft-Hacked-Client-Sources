/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.management.option.options.speed;

import java.util.List;
import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.option.Option;
import me.aristhena.lucid.management.option.OptionManager;
import me.aristhena.lucid.management.option.options.speed.SpeedOption;

public class Boost
extends SpeedOption {
    public Boost(String name, boolean value, Module mod) {
        super(name, value, mod);
    }

    @Override
    public void setValue(boolean value) {
        if (value) {
            for (Option option : OptionManager.optionList) {
                if (!(option instanceof SpeedOption) || option == this) continue;
                option.setValueHard(false);
            }
            super.setValue(value);
        }
    }
}

