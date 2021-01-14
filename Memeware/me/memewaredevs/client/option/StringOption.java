/*
 * Decompiled with CFR 0.145.
 *
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package me.memewaredevs.client.option;

import me.memewaredevs.client.module.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringOption extends Option<String> {
    private final List<String> modes;

    public StringOption(Module module, String parentModuleMode, final String name, final String... modes) {
        super(module, parentModuleMode, name, modes[0]);
        this.modes = Arrays.asList(modes);
    }

    public StringOption(Module module, final String name, final String... modes) {
        this(module, null, name, modes);
    }

    @Override
    public void setValue(final String value) {
        if (lowercase(this.modes).contains(value.toLowerCase())) {
            this.value = value;
        }
    }

    private List<String> lowercase(List<String> input) {
        ArrayList<String> list = new ArrayList<>();
        for (String s : input)
            list.add(s.toLowerCase());
        return list;
    }

    public List<String> getValues() {
        return this.modes;
    }
}
