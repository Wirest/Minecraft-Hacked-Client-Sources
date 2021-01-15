package io.netty.channel.group;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.util.Iterator;

public abstract interface ChannelGroupFuture
  extends Future<Void>, Iterable<ChannelFuture>
{
  public abstract ChannelGroup group();
  
  public abstract ChannelFuture find(Channel paramChannel);
  
  public abstract boolean isSuccess();
  
  public abstract ChannelGroupException cause();
  
  public abstract boolean isPartialSuccess();
  
  public abstract boolean isPartialFailure();
  
  public abstract ChannelGroupFuture addListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener);
  
  public abstract ChannelGroupFuture addListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs);
  
  public abstract ChannelGroupFuture removeListener(GenericFutureListener<? extends Future<? super Void>> paramGenericFutureListener);
  
  public abstract ChannelGroupFuture removeListeners(GenericFutureListener<? extends Future<? super Void>>... paramVarArgs);
  
  public abstract ChannelGroupFuture await()
    throws InterruptedException;
  
  public abstract ChannelGroupFuture awaitUninterruptibly();
  
  public abstract ChannelGroupFuture syncUninterruptibly();
  
  public abstract ChannelGroupFuture sync()
    throws InterruptedException;
  
  public abstract Iterator<ChannelFuture> iterator();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\group\ChannelGroupFuture.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */