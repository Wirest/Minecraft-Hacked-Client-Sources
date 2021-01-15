package io.netty.handler.codec.memcache.binary;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.memcache.MemcacheMessage;

public abstract interface BinaryMemcacheMessage
  extends MemcacheMessage
{
  public abstract byte magic();
  
  public abstract BinaryMemcacheMessage setMagic(byte paramByte);
  
  public abstract byte opcode();
  
  public abstract BinaryMemcacheMessage setOpcode(byte paramByte);
  
  public abstract short keyLength();
  
  public abstract BinaryMemcacheMessage setKeyLength(short paramShort);
  
  public abstract byte extrasLength();
  
  public abstract BinaryMemcacheMessage setExtrasLength(byte paramByte);
  
  public abstract byte dataType();
  
  public abstract BinaryMemcacheMessage setDataType(byte paramByte);
  
  public abstract int totalBodyLength();
  
  public abstract BinaryMemcacheMessage setTotalBodyLength(int paramInt);
  
  public abstract int opaque();
  
  public abstract BinaryMemcacheMessage setOpaque(int paramInt);
  
  public abstract long cas();
  
  public abstract BinaryMemcacheMessage setCas(long paramLong);
  
  public abstract String key();
  
  public abstract BinaryMemcacheMessage setKey(String paramString);
  
  public abstract ByteBuf extras();
  
  public abstract BinaryMemcacheMessage setExtras(ByteBuf paramByteBuf);
  
  public abstract BinaryMemcacheMessage retain();
  
  public abstract BinaryMemcacheMessage retain(int paramInt);
  
  public abstract BinaryMemcacheMessage touch();
  
  public abstract BinaryMemcacheMessage touch(Object paramObject);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\binary\BinaryMemcacheMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */