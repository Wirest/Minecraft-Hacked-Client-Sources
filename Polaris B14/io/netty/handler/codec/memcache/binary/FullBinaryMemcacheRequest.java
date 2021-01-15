package io.netty.handler.codec.memcache.binary;

import io.netty.handler.codec.memcache.FullMemcacheMessage;

public abstract interface FullBinaryMemcacheRequest
  extends BinaryMemcacheRequest, FullMemcacheMessage
{
  public abstract FullBinaryMemcacheRequest copy();
  
  public abstract FullBinaryMemcacheRequest retain(int paramInt);
  
  public abstract FullBinaryMemcacheRequest retain();
  
  public abstract FullBinaryMemcacheRequest touch();
  
  public abstract FullBinaryMemcacheRequest touch(Object paramObject);
  
  public abstract FullBinaryMemcacheRequest duplicate();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\binary\FullBinaryMemcacheRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */