package io.netty.channel;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public abstract interface ChannelFuture
  extends Future<Void>
{
  public abstract Channel channel();
  
  public abstract ChannelFuture addListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener);
  
  public abstract ChannelFuture addListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs);
  
  public abstract ChannelFuture removeListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener);
  
  public abstract ChannelFuture removeListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs);
  
  public abstract ChannelFuture sync()
    throws InterruptedException;
  
  public abstract ChannelFuture syncUninterruptibly();
  
  public abstract ChannelFuture await()
    throws InterruptedException;
  
  public abstract ChannelFuture awaitUninterruptibly();
  
  public abstract boolean isVoid();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ChannelFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */