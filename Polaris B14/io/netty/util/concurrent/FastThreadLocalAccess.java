package io.netty.util.concurrent;

import io.netty.util.internal.InternalThreadLocalMap;

public abstract interface FastThreadLocalAccess
{
  public abstract InternalThreadLocalMap threadLocalMap();
  
  public abstract void setThreadLocalMap(InternalThreadLocalMap paramInternalThreadLocalMap);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\FastThreadLocalAccess.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */