package io.netty.handler.codec.http;

import java.util.Set;

public abstract interface Cookie
  extends Comparable<Cookie>
{
  public abstract String name();
  
  public abstract String value();
  
  public abstract void setValue(String paramString);
  
  public abstract String rawValue();
  
  public abstract void setRawValue(String paramString);
  
  public abstract String domain();
  
  public abstract void setDomain(String paramString);
  
  public abstract String path();
  
  public abstract void setPath(String paramString);
  
  public abstract String comment();
  
  public abstract void setComment(String paramString);
  
  public abstract long maxAge();
  
  public abstract void setMaxAge(long paramLong);
  
  public abstract int version();
  
  public abstract void setVersion(int paramInt);
  
  public abstract boolean isSecure();
  
  public abstract void setSecure(boolean paramBoolean);
  
  public abstract boolean isHttpOnly();
  
  public abstract void setHttpOnly(boolean paramBoolean);
  
  public abstract String commentUrl();
  
  public abstract void setCommentUrl(String paramString);
  
  public abstract boolean isDiscard();
  
  public abstract void setDiscard(boolean paramBoolean);
  
  public abstract Set<Integer> ports();
  
  public abstract void setPorts(int... paramVarArgs);
  
  public abstract void setPorts(Iterable<Integer> paramIterable);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\Cookie.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */