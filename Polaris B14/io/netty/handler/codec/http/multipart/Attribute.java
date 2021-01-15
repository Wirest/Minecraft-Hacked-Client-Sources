package io.netty.handler.codec.http.multipart;

import java.io.IOException;

public abstract interface Attribute
  extends HttpData
{
  public abstract String getValue()
    throws IOException;
  
  public abstract void setValue(String paramString)
    throws IOException;
  
  public abstract Attribute copy();
  
  public abstract Attribute duplicate();
  
  public abstract Attribute retain();
  
  public abstract Attribute retain(int paramInt);
  
  public abstract Attribute touch();
  
  public abstract Attribute touch(Object paramObject);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\Attribute.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */