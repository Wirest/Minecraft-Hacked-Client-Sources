package io.netty.util.internal;

import io.netty.util.concurrent.EventExecutor;
import java.util.concurrent.Callable;

public abstract interface CallableEventExecutorAdapter<V>
  extends Callable<V>
{
  public abstract EventExecutor executor();
  
  public abstract Callable<V> unwrap();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\CallableEventExecutorAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */