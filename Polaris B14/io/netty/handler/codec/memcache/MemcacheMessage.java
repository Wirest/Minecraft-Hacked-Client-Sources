package io.netty.handler.codec.memcache;

import io.netty.util.ReferenceCounted;

public abstract interface MemcacheMessage
  extends MemcacheObject, ReferenceCounted
{
  public abstract MemcacheMessage retain();
  
  public abstract MemcacheMessage retain(int paramInt);
  
  public abstract MemcacheMessage touch();
  
  public abstract MemcacheMessage touch(Object paramObject);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\MemcacheMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */