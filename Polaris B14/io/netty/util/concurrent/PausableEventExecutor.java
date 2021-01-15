package io.netty.util.concurrent;

public abstract interface PausableEventExecutor
  extends EventExecutor, WrappedEventExecutor
{
  public abstract void rejectNewTasks();
  
  public abstract void acceptNewTasks();
  
  public abstract boolean isAcceptingNewTasks();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\PausableEventExecutor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */