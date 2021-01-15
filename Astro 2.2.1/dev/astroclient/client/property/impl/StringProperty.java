package dev.astroclient.client.property.impl;

import dev.astroclient.client.property.Property;

/**
 * @author Zane for PublicBase
 * @since 10/23/19
 */

public class StringProperty extends Property {
    private String value, defaultValue, values[];

    public StringProperty(String name, boolean dependency, String defaultValue, String[] values) {
        super(name, dependency);
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.values = values;
    }

    public StringProperty(String name, String defaultValue, String[] values) {
        super(name);
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.values = values;
    }

    public void setValue(String value) {
        if (isOption(value) && !this.value.equals(value))
            this.value = value;
    }

    public boolean isOption(String value) {
        for (String value1 : values) {
            if (value1.equals(value))
                return true;
        }
        return false;
    }

    public String getValue() {
        return value;
    }

    public String[] getValues() {
        return values;
    }

    public void increment() {
        String currentMode = getValue();

        for (String mode : getValues()) {
            if (!mode.equalsIgnoreCase(currentMode)) {
                continue;
            }

            String newValue;

            int ordinal = getOrdinal(mode, values);
            if (ordinal == values.length - 1) {
                newValue = values[0];
            } else {
                newValue = values[ordinal + 1];
            }

            setValue(newValue);
            return;
        }
    }


    public void decrement() {
        String currentMode = getValue();

        for (String mode : getValues()) {
            if (!mode.equalsIgnoreCase(currentMode)) {
                continue;
            }

            String newValue;

            int ordinal = getOrdinal(mode, getValues());
            if (ordinal == 0) {
                newValue = values[values.length - 1];
            } else {
                newValue = values[ordinal - 1];
            }

            setValue(newValue);
            return;
        }
    }


    private int getOrdinal(String value, String[] array) {
        for (int i = 0; i <= array.length - 1; i++) {
            String indexString = array[i];
            if (indexString.equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }

}
