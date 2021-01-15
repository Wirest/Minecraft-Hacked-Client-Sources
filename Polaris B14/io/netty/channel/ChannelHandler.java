package io.netty.channel;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.SocketAddress;

public abstract interface ChannelHandler
{
  public abstract void handlerAdded(ChannelHandlerContext paramChannelHandlerContext)
    throws Exception;
  
  public abstract void handlerRemoved(ChannelHandlerContext paramChannelHandlerContext)
    throws Exception;
  
  public abstract void exceptionCaught(ChannelHandlerContext paramChannelHandlerContext, Throwable paramThrowable)
    throws Exception;
  
  public abstract void channelRegistered(ChannelHandlerContext paramChannelHandlerContext)
    throws Exception;
  
  public abstract void channelUnregistered(ChannelHandlerContext paramChannelHandlerContext)
    throws Exception;
  
  public abstract void channelActive(ChannelHandlerContext paramChannelHandlerContext)
    throws Exception;
  
  public abstract void channelInactive(ChannelHandlerContext paramChannelHandlerContext)
    throws Exception;
  
  public abstract void channelRead(ChannelHandlerContext paramChannelHandlerContext, Object paramObject)
    throws Exception;
  
  public abstract void channelReadComplete(ChannelHandlerContext paramChannelHandlerContext)
    throws Exception;
  
  public abstract void userEventTriggered(ChannelHandlerContext paramChannelHandlerContext, Object paramObject)
    throws Exception;
  
  public abstract void channelWritabilityChanged(ChannelHandlerContext paramChannelHandlerContext)
    throws Exception;
  
  public abstract void bind(ChannelHandlerContext paramChannelHandlerContext, SocketAddress paramSocketAddress, ChannelPromise paramChannelPromise)
    throws Exception;
  
  public abstract void connect(ChannelHandlerContext paramChannelHandlerContext, SocketAddress paramSocketAddress1, SocketAddress paramSocketAddress2, ChannelPromise paramChannelPromise)
    throws Exception;
  
  public abstract void disconnect(ChannelHandlerContext paramChannelHandlerContext, ChannelPromise paramChannelPromise)
    throws Exception;
  
  public abstract void close(ChannelHandlerContext paramChannelHandlerContext, ChannelPromise paramChannelPromise)
    throws Exception;
  
  public abstract void deregister(ChannelHandlerContext paramChannelHandlerContext, ChannelPromise paramChannelPromise)
    throws Exception;
  
  public abstract void read(ChannelHandlerContext paramChannelHandlerContext)
    throws Exception;
  
  public abstract void write(ChannelHandlerContext paramChannelHandlerContext, Object paramObject, ChannelPromise paramChannelPromise)
    throws Exception;
  
  public abstract void flush(ChannelHandlerContext paramChannelHandlerContext)
    throws Exception;
  
  @Target({java.lang.annotation.ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface Skip {}
  
  @Inherited
  @Documented
  @Target({java.lang.annotation.ElementType.TYPE})
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface Sharable {}
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ChannelHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */