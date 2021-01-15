package io.netty.util;

public abstract interface Attribute<T>
{
  public abstract AttributeKey<T> key();
  
  public abstract T get();
  
  public abstract void set(T paramT);
  
  public abstract T getAndSet(T paramT);
  
  public abstract T setIfAbsent(T paramT);
  
  public abstract T getAndRemove();
  
  public abstract boolean compareAndSet(T paramT1, T paramT2);
  
  public abstract void remove();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\Attribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */