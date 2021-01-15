package net.minecraft.util;

public abstract interface IRegistry<K, V>
  extends Iterable<V>
{
  public abstract V getObject(K paramK);
  
  public abstract void putObject(K paramK, V paramV);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\util\IRegistry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */