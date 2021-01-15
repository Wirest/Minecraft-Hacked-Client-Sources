package io.netty.handler.codec.http2;

import io.netty.buffer.ByteBuf;

public abstract interface Http2HeadersEncoder
{
  public abstract void encodeHeaders(Http2Headers paramHttp2Headers, ByteBuf paramByteBuf)
    throws Http2Exception;
  
  public abstract Configuration configuration();
  
  public static abstract interface Configuration
  {
    public abstract Http2HeaderTable headerTable();
  }
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http2\Http2HeadersEncoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */