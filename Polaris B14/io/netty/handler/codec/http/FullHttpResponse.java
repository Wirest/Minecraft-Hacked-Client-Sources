package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;

public abstract interface FullHttpResponse
  extends HttpResponse, FullHttpMessage
{
  public abstract FullHttpResponse copy(ByteBuf paramByteBuf);
  
  public abstract FullHttpResponse copy();
  
  public abstract FullHttpResponse retain(int paramInt);
  
  public abstract FullHttpResponse retain();
  
  public abstract FullHttpResponse touch();
  
  public abstract FullHttpResponse touch(Object paramObject);
  
  public abstract FullHttpResponse duplicate();
  
  public abstract FullHttpResponse setProtocolVersion(HttpVersion paramHttpVersion);
  
  public abstract FullHttpResponse setStatus(HttpResponseStatus paramHttpResponseStatus);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\FullHttpResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */