package io.netty.handler.codec.spdy;

public abstract interface SpdySynReplyFrame
  extends SpdyHeadersFrame
{
  public abstract SpdySynReplyFrame setStreamId(int paramInt);
  
  public abstract SpdySynReplyFrame setLast(boolean paramBoolean);
  
  public abstract SpdySynReplyFrame setInvalid();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdySynReplyFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */