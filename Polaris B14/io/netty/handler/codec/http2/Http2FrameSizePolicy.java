package io.netty.handler.codec.http2;

public abstract interface Http2FrameSizePolicy
{
  public abstract void maxFrameSize(int paramInt)
    throws Http2Exception;
  
  public abstract int maxFrameSize();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2FrameSizePolicy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */