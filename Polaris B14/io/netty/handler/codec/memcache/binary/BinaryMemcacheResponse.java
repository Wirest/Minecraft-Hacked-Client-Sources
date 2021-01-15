package io.netty.handler.codec.memcache.binary;

public abstract interface BinaryMemcacheResponse
  extends BinaryMemcacheMessage
{
  public abstract short status();
  
  public abstract BinaryMemcacheResponse setStatus(short paramShort);
  
  public abstract BinaryMemcacheResponse retain();
  
  public abstract BinaryMemcacheResponse retain(int paramInt);
  
  public abstract BinaryMemcacheResponse touch();
  
  public abstract BinaryMemcacheResponse touch(Object paramObject);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */