package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public abstract interface Http2FrameListener
{
  public abstract int onDataRead(ChannelHandlerContext paramChannelHandlerContext, int paramInt1, ByteBuf paramByteBuf, int paramInt2, boolean paramBoolean)
    throws Http2Exception;
  
  public abstract void onHeadersRead(ChannelHandlerContext paramChannelHandlerContext, int paramInt1, Http2Headers paramHttp2Headers, int paramInt2, boolean paramBoolean)
    throws Http2Exception;
  
  public abstract void onHeadersRead(ChannelHandlerContext paramChannelHandlerContext, int paramInt1, Http2Headers paramHttp2Headers, int paramInt2, short paramShort, boolean paramBoolean1, int paramInt3, boolean paramBoolean2)
    throws Http2Exception;
  
  public abstract void onPriorityRead(ChannelHandlerContext paramChannelHandlerContext, int paramInt1, int paramInt2, short paramShort, boolean paramBoolean)
    throws Http2Exception;
  
  public abstract void onRstStreamRead(ChannelHandlerContext paramChannelHandlerContext, int paramInt, long paramLong)
    throws Http2Exception;
  
  public abstract void onSettingsAckRead(ChannelHandlerContext paramChannelHandlerContext)
    throws Http2Exception;
  
  public abstract void onSettingsRead(ChannelHandlerContext paramChannelHandlerContext, Http2Settings paramHttp2Settings)
    throws Http2Exception;
  
  public abstract void onPingRead(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf)
    throws Http2Exception;
  
  public abstract void onPingAckRead(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf)
    throws Http2Exception;
  
  public abstract void onPushPromiseRead(ChannelHandlerContext paramChannelHandlerContext, int paramInt1, int paramInt2, Http2Headers paramHttp2Headers, int paramInt3)
    throws Http2Exception;
  
  public abstract void onGoAwayRead(ChannelHandlerContext paramChannelHandlerContext, int paramInt, long paramLong, ByteBuf paramByteBuf)
    throws Http2Exception;
  
  public abstract void onWindowUpdateRead(ChannelHandlerContext paramChannelHandlerContext, int paramInt1, int paramInt2)
    throws Http2Exception;
  
  public abstract void onUnknownFrame(ChannelHandlerContext paramChannelHandlerContext, byte paramByte, int paramInt, Http2Flags paramHttp2Flags, ByteBuf paramByteBuf);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2FrameListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */