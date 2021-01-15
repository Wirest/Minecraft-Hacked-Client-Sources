package io.netty.util.internal;

import io.netty.util.concurrent.EventExecutor;

public abstract interface RunnableEventExecutorAdapter
  extends Runnable
{
  public abstract EventExecutor executor();
  
  public abstract Runnable unwrap();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\internal\RunnableEventExecutorAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */