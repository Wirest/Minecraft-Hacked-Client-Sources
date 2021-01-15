package dev.astroclient.client.property.impl;

import dev.astroclient.client.property.Property;

/**
 * @author Zane for PublicBase
 * @since 10/23/19
 */

public class BooleanProperty extends Property {

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        if (this.value != value)
            this.value = value;
    }

    private String name;

    private boolean value, defaultValue;

    public BooleanProperty(String name, boolean dependency, boolean defaultValue) {
        super(name, dependency);
        this.value = defaultValue;
        this.defaultValue = defaultValue;
    }
}
