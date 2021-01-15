package io.netty.util.collection;

import java.util.Collection;

public abstract interface IntObjectMap<V>
{
  public abstract V get(int paramInt);
  
  public abstract V put(int paramInt, V paramV);
  
  public abstract void putAll(IntObjectMap<V> paramIntObjectMap);
  
  public abstract V remove(int paramInt);
  
  public abstract int size();
  
  public abstract boolean isEmpty();
  
  public abstract void clear();
  
  public abstract boolean containsKey(int paramInt);
  
  public abstract boolean containsValue(V paramV);
  
  public abstract Iterable<Entry<V>> entries();
  
  public abstract int[] keys();
  
  public abstract V[] values(Class<V> paramClass);
  
  public abstract Collection<V> values();
  
  public static abstract interface Entry<V>
  {
    public abstract int key();
    
    public abstract V value();
    
    public abstract void setValue(V paramV);
  }
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\collection\IntObjectMap.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */