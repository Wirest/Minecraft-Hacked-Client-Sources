package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;

public abstract interface EventLoop
  extends EventExecutor, EventLoopGroup
{
  public abstract EventLoopGroup parent();
  
  public abstract EventLoop unwrap();
  
  public abstract ChannelHandlerInvoker asInvoker();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\EventLoop.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */