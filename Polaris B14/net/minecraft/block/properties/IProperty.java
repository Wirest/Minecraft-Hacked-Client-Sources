package net.minecraft.block.properties;

import java.util.Collection;

public abstract interface IProperty<T extends Comparable<T>>
{
  public abstract String getName();
  
  public abstract Collection<T> getAllowedValues();
  
  public abstract Class<T> getValueClass();
  
  public abstract String getName(T paramT);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\block\properties\IProperty.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */