package io.netty.handler.codec.http;

import io.netty.buffer.ByteBufHolder;

public abstract interface HttpContent
  extends HttpObject, ByteBufHolder
{
  public abstract HttpContent copy();
  
  public abstract HttpContent duplicate();
  
  public abstract HttpContent retain();
  
  public abstract HttpContent retain(int paramInt);
  
  public abstract HttpContent touch();
  
  public abstract HttpContent touch(Object paramObject);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\HttpContent.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */