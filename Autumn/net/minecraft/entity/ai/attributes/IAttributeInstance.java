package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.UUID;

public interface IAttributeInstance {
   IAttribute getAttribute();

   double getBaseValue();

   void setBaseValue(double var1);

   Collection getModifiersByOperation(int var1);

   Collection func_111122_c();

   boolean hasModifier(AttributeModifier var1);

   AttributeModifier getModifier(UUID var1);

   void applyModifier(AttributeModifier var1);

   void removeModifier(AttributeModifier var1);

   void removeAllModifiers();

   double getAttributeValue();
}
