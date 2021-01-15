package io.netty.handler.codec.memcache.binary;

public abstract interface BinaryMemcacheRequest
  extends BinaryMemcacheMessage
{
  public abstract short reserved();
  
  public abstract BinaryMemcacheRequest setReserved(short paramShort);
  
  public abstract BinaryMemcacheRequest retain();
  
  public abstract BinaryMemcacheRequest retain(int paramInt);
  
  public abstract BinaryMemcacheRequest touch();
  
  public abstract BinaryMemcacheRequest touch(Object paramObject);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */