/*
 * Decompiled with CFR 0_114.
 */
package me.aristhena.lucid.management.option.options.aura;

import me.aristhena.lucid.management.module.Module;
import me.aristhena.lucid.management.option.Option;
import me.aristhena.lucid.management.option.OptionManager;

public class Switch
extends Option {
    public Switch(String name, boolean value, Module mod) {
        super(name, value, mod);
    }

    @Override
    public void setValue(boolean value) {
        if (value) {
            OptionManager.getOption("Tick", this.mod).setValueHard(false);
        } else {
            OptionManager.getOption("Tick", this.mod).setValueHard(true);
        }
        super.setValue(value);
    }
}

