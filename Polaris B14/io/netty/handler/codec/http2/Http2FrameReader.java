package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.Closeable;

public abstract interface Http2FrameReader
  extends Closeable
{
  public abstract void readFrame(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf, Http2FrameListener paramHttp2FrameListener)
    throws Http2Exception;
  
  public abstract Configuration configuration();
  
  public abstract void close();
  
  public static abstract interface Configuration
  {
    public abstract Http2HeaderTable headerTable();
    
    public abstract Http2FrameSizePolicy frameSizePolicy();
  }
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2FrameReader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */