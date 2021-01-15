package io.netty.handler.codec.http.multipart;

import io.netty.handler.codec.http.HttpContent;
import java.util.List;

public abstract interface InterfaceHttpPostRequestDecoder
{
  public abstract boolean isMultipart();
  
  public abstract void setDiscardThreshold(int paramInt);
  
  public abstract int getDiscardThreshold();
  
  public abstract List<InterfaceHttpData> getBodyHttpDatas();
  
  public abstract List<InterfaceHttpData> getBodyHttpDatas(String paramString);
  
  public abstract InterfaceHttpData getBodyHttpData(String paramString);
  
  public abstract InterfaceHttpPostRequestDecoder offer(HttpContent paramHttpContent);
  
  public abstract boolean hasNext();
  
  public abstract InterfaceHttpData next();
  
  public abstract void destroy();
  
  public abstract void cleanFiles();
  
  public abstract void removeHttpDataFromClean(InterfaceHttpData paramInterfaceHttpData);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\multipart\InterfaceHttpPostRequestDecoder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */