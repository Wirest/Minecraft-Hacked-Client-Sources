package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public abstract interface Http2LifecycleManager
{
  public abstract void closeLocalSide(Http2Stream paramHttp2Stream, ChannelFuture paramChannelFuture);
  
  public abstract void closeRemoteSide(Http2Stream paramHttp2Stream, ChannelFuture paramChannelFuture);
  
  public abstract void closeStream(Http2Stream paramHttp2Stream, ChannelFuture paramChannelFuture);
  
  public abstract ChannelFuture writeRstStream(ChannelHandlerContext paramChannelHandlerContext, int paramInt, long paramLong, ChannelPromise paramChannelPromise);
  
  public abstract ChannelFuture writeGoAway(ChannelHandlerContext paramChannelHandlerContext, int paramInt, long paramLong, ByteBuf paramByteBuf, ChannelPromise paramChannelPromise);
  
  public abstract void onException(ChannelHandlerContext paramChannelHandlerContext, Throwable paramThrowable);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2LifecycleManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */