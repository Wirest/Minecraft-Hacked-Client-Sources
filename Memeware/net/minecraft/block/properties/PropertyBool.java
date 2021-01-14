package net.minecraft.block.properties;

import com.google.common.collect.ImmutableSet;

import java.util.Collection;

public class PropertyBool extends PropertyHelper {
    private final ImmutableSet allowedValues = ImmutableSet.of(Boolean.valueOf(true), Boolean.valueOf(false));
    private static final String __OBFID = "CL_00002017";

    protected PropertyBool(String name) {
        super(name, Boolean.class);
    }

    public Collection getAllowedValues() {
        return this.allowedValues;
    }

    public static PropertyBool create(String name) {
        return new PropertyBool(name);
    }

    /**
     * Synthetic method called by getName
     */
    public String getName0(Boolean value) {
        return value.toString();
    }

    /**
     * Get the name for the given value.
     */
    public String getName(Comparable value) {
        return this.getName0((Boolean) value);
    }
}
