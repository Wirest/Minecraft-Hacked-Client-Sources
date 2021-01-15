package net.minecraft.block.properties;

import com.google.common.collect.ImmutableSet;
import java.util.Collection;

public class PropertyBool extends PropertyHelper {
   private final ImmutableSet allowedValues = ImmutableSet.of(true, false);
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

   public String getName0(Boolean value) {
      return value.toString();
   }

   public String getName(Comparable value) {
      return this.getName0((Boolean)value);
   }
}
