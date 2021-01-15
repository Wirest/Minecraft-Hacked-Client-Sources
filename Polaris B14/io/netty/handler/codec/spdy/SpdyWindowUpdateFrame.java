package io.netty.handler.codec.spdy;

public abstract interface SpdyWindowUpdateFrame
  extends SpdyFrame
{
  public abstract int streamId();
  
  public abstract SpdyWindowUpdateFrame setStreamId(int paramInt);
  
  public abstract int deltaWindowSize();
  
  public abstract SpdyWindowUpdateFrame setDeltaWindowSize(int paramInt);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyWindowUpdateFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */