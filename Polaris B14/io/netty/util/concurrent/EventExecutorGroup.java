package io.netty.util.concurrent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract interface EventExecutorGroup
  extends ScheduledExecutorService, AutoCloseable
{
  public abstract boolean isShuttingDown();
  
  public abstract Future<?> shutdownGracefully();
  
  public abstract Future<?> shutdownGracefully(long paramLong1, long paramLong2, TimeUnit paramTimeUnit);
  
  public abstract Future<?> terminationFuture();
  
  @Deprecated
  public abstract void shutdown();
  
  @Deprecated
  public abstract List<Runnable> shutdownNow();
  
  public abstract EventExecutor next();
  
  public abstract <E extends EventExecutor> Set<E> children();
  
  public abstract Future<?> submit(Runnable paramRunnable);
  
  public abstract <T> Future<T> submit(Runnable paramRunnable, T paramT);
  
  public abstract <T> Future<T> submit(Callable<T> paramCallable);
  
  public abstract ScheduledFuture<?> schedule(Runnable paramRunnable, long paramLong, TimeUnit paramTimeUnit);
  
  public abstract <V> ScheduledFuture<V> schedule(Callable<V> paramCallable, long paramLong, TimeUnit paramTimeUnit);
  
  public abstract ScheduledFuture<?> scheduleAtFixedRate(Runnable paramRunnable, long paramLong1, long paramLong2, TimeUnit paramTimeUnit);
  
  public abstract ScheduledFuture<?> scheduleWithFixedDelay(Runnable paramRunnable, long paramLong1, long paramLong2, TimeUnit paramTimeUnit);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\EventExecutorGroup.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */