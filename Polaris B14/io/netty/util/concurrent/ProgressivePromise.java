package io.netty.util.concurrent;

public abstract interface ProgressivePromise<V>
  extends Promise<V>, ProgressiveFuture<V>
{
  public abstract ProgressivePromise<V> setProgress(long paramLong1, long paramLong2);
  
  public abstract boolean tryProgress(long paramLong1, long paramLong2);
  
  public abstract ProgressivePromise<V> setSuccess(V paramV);
  
  public abstract ProgressivePromise<V> setFailure(Throwable paramThrowable);
  
  public abstract ProgressivePromise<V> addListener(GenericFutureListener<? extends Future<? super V>> paramGenericFutureListener);
  
  public abstract ProgressivePromise<V> addListeners(GenericFutureListener<? extends Future<? super V>>... paramVarArgs);
  
  public abstract ProgressivePromise<V> removeListener(GenericFutureListener<? extends Future<? super V>> paramGenericFutureListener);
  
  public abstract ProgressivePromise<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... paramVarArgs);
  
  public abstract ProgressivePromise<V> await()
    throws InterruptedException;
  
  public abstract ProgressivePromise<V> awaitUninterruptibly();
  
  public abstract ProgressivePromise<V> sync()
    throws InterruptedException;
  
  public abstract ProgressivePromise<V> syncUninterruptibly();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\ProgressivePromise.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */