package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.io.Closeable;
import java.util.List;

public abstract interface Http2ConnectionDecoder
  extends Closeable
{
  public abstract Http2Connection connection();
  
  public abstract Http2LocalFlowController flowController();
  
  public abstract Http2FrameListener listener();
  
  public abstract void decodeFrame(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf, List<Object> paramList)
    throws Http2Exception;
  
  public abstract Http2Settings localSettings();
  
  public abstract void localSettings(Http2Settings paramHttp2Settings)
    throws Http2Exception;
  
  public abstract boolean prefaceReceived();
  
  public abstract void close();
  
  public static abstract interface Builder
  {
    public abstract Builder connection(Http2Connection paramHttp2Connection);
    
    public abstract Builder lifecycleManager(Http2LifecycleManager paramHttp2LifecycleManager);
    
    public abstract Http2LifecycleManager lifecycleManager();
    
    public abstract Builder frameReader(Http2FrameReader paramHttp2FrameReader);
    
    public abstract Builder listener(Http2FrameListener paramHttp2FrameListener);
    
    public abstract Builder encoder(Http2ConnectionEncoder paramHttp2ConnectionEncoder);
    
    public abstract Http2ConnectionDecoder build();
  }
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2ConnectionDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */