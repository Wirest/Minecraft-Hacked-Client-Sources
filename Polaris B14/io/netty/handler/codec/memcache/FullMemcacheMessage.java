package io.netty.handler.codec.memcache;

public abstract interface FullMemcacheMessage
  extends MemcacheMessage, LastMemcacheContent
{
  public abstract FullMemcacheMessage copy();
  
  public abstract FullMemcacheMessage retain(int paramInt);
  
  public abstract FullMemcacheMessage retain();
  
  public abstract FullMemcacheMessage touch();
  
  public abstract FullMemcacheMessage touch(Object paramObject);
  
  public abstract FullMemcacheMessage duplicate();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\FullMemcacheMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */