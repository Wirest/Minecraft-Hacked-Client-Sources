package io.netty.handler.codec.spdy;

public abstract interface SpdyRstStreamFrame
  extends SpdyStreamFrame
{
  public abstract SpdyStreamStatus status();
  
  public abstract SpdyRstStreamFrame setStatus(SpdyStreamStatus paramSpdyStreamStatus);
  
  public abstract SpdyRstStreamFrame setStreamId(int paramInt);
  
  public abstract SpdyRstStreamFrame setLast(boolean paramBoolean);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyRstStreamFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */