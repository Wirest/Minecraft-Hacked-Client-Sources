package io.netty.channel;

import io.netty.util.concurrent.EventExecutor;
import java.net.SocketAddress;

public abstract interface ChannelHandlerInvoker
{
  public abstract EventExecutor executor();
  
  public abstract void invokeChannelRegistered(ChannelHandlerContext paramChannelHandlerContext);
  
  public abstract void invokeChannelUnregistered(ChannelHandlerContext paramChannelHandlerContext);
  
  public abstract void invokeChannelActive(ChannelHandlerContext paramChannelHandlerContext);
  
  public abstract void invokeChannelInactive(ChannelHandlerContext paramChannelHandlerContext);
  
  public abstract void invokeExceptionCaught(ChannelHandlerContext paramChannelHandlerContext, Throwable paramThrowable);
  
  public abstract void invokeUserEventTriggered(ChannelHandlerContext paramChannelHandlerContext, Object paramObject);
  
  public abstract void invokeChannelRead(ChannelHandlerContext paramChannelHandlerContext, Object paramObject);
  
  public abstract void invokeChannelReadComplete(ChannelHandlerContext paramChannelHandlerContext);
  
  public abstract void invokeChannelWritabilityChanged(ChannelHandlerContext paramChannelHandlerContext);
  
  public abstract void invokeBind(ChannelHandlerContext paramChannelHandlerContext, SocketAddress paramSocketAddress, ChannelPromise paramChannelPromise);
  
  public abstract void invokeConnect(ChannelHandlerContext paramChannelHandlerContext, SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, ChannelPromise paramChannelPromise);
  
  public abstract void invokeDisconnect(ChannelHandlerContext paramChannelHandlerContext, ChannelPromise paramChannelPromise);
  
  public abstract void invokeClose(ChannelHandlerContext paramChannelHandlerContext, ChannelPromise paramChannelPromise);
  
  public abstract void invokeDeregister(ChannelHandlerContext paramChannelHandlerContext, ChannelPromise paramChannelPromise);
  
  public abstract void invokeRead(ChannelHandlerContext paramChannelHandlerContext);
  
  public abstract void invokeWrite(ChannelHandlerContext paramChannelHandlerContext, Object paramObject, ChannelPromise paramChannelPromise);
  
  public abstract void invokeFlush(ChannelHandlerContext paramChannelHandlerContext);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ChannelHandlerInvoker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */