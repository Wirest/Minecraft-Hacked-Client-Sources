package io.netty.util;

public abstract interface ResourceLeak
{
  public abstract void record();
  
  public abstract void record(Object paramObject);
  
  public abstract boolean close();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\ResourceLeak.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */