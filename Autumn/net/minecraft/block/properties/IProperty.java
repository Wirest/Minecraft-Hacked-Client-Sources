package net.minecraft.block.properties;

import java.util.Collection;

public interface IProperty {
   String getName();

   Collection getAllowedValues();

   Class getValueClass();

   String getName(Comparable var1);
}
