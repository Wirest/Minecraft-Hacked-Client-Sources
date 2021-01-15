package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public abstract interface Http2ConnectionEncoder
  extends Http2FrameWriter
{
  public abstract Http2Connection connection();
  
  public abstract Http2RemoteFlowController flowController();
  
  public abstract Http2FrameWriter frameWriter();
  
  public abstract Http2Settings pollSentSettings();
  
  public abstract void remoteSettings(Http2Settings paramHttp2Settings)
    throws Http2Exception;
  
  public abstract ChannelFuture writeFrame(ChannelHandlerContext paramChannelHandlerContext, byte paramByte, int paramInt, Http2Flags paramHttp2Flags, ByteBuf paramByteBuf, ChannelPromise paramChannelPromise);
  
  public static abstract interface Builder
  {
    public abstract Builder connection(Http2Connection paramHttp2Connection);
    
    public abstract Builder lifecycleManager(Http2LifecycleManager paramHttp2LifecycleManager);
    
    public abstract Http2LifecycleManager lifecycleManager();
    
    public abstract Builder frameWriter(Http2FrameWriter paramHttp2FrameWriter);
    
    public abstract Http2ConnectionEncoder build();
  }
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2ConnectionEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */