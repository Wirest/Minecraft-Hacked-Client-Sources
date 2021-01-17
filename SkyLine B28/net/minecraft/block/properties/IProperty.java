package net.minecraft.block.properties;

import java.util.Collection;

public interface IProperty
{
    String getName();

    Collection getAllowedValues();

    /**
     * The class of the values of this property
     */
    Class getValueClass();

    /**
     * Get the name for the given value.
     */
    String getName(Comparable var1);
}
