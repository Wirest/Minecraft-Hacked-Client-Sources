package io.netty.handler.codec.http;

import io.netty.buffer.ByteBuf;

public abstract interface FullHttpMessage
  extends HttpMessage, LastHttpContent
{
  public abstract FullHttpMessage copy(ByteBuf paramByteBuf);
  
  public abstract FullHttpMessage copy();
  
  public abstract FullHttpMessage retain(int paramInt);
  
  public abstract FullHttpMessage retain();
  
  public abstract FullHttpMessage touch();
  
  public abstract FullHttpMessage touch(Object paramObject);
  
  public abstract FullHttpMessage duplicate();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\FullHttpMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */