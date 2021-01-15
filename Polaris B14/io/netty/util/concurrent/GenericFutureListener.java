package io.netty.util.concurrent;

import java.util.EventListener;

public abstract interface GenericFutureListener<F extends Future<?>>
  extends EventListener
{
  public abstract void operationComplete(F paramF)
    throws Exception;
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\GenericFutureListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */