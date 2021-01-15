package net.minecraft.entity.ai.attributes;

import java.util.Collection;
import java.util.UUID;

public abstract interface IAttributeInstance
{
  public abstract IAttribute getAttribute();
  
  public abstract double getBaseValue();
  
  public abstract void setBaseValue(double paramDouble);
  
  public abstract Collection<AttributeModifier> getModifiersByOperation(int paramInt);
  
  public abstract Collection<AttributeModifier> func_111122_c();
  
  public abstract boolean hasModifier(AttributeModifier paramAttributeModifier);
  
  public abstract AttributeModifier getModifier(UUID paramUUID);
  
  public abstract void applyModifier(AttributeModifier paramAttributeModifier);
  
  public abstract void removeModifier(AttributeModifier paramAttributeModifier);
  
  public abstract void removeAllModifiers();
  
  public abstract double getAttributeValue();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\entity\ai\attributes\IAttributeInstance.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */