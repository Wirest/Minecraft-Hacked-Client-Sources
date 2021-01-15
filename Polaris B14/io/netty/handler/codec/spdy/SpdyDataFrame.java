package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;

public abstract interface SpdyDataFrame
  extends ByteBufHolder, SpdyStreamFrame
{
  public abstract SpdyDataFrame setStreamId(int paramInt);
  
  public abstract SpdyDataFrame setLast(boolean paramBoolean);
  
  public abstract ByteBuf content();
  
  public abstract SpdyDataFrame copy();
  
  public abstract SpdyDataFrame duplicate();
  
  public abstract SpdyDataFrame retain();
  
  public abstract SpdyDataFrame retain(int paramInt);
  
  public abstract SpdyDataFrame touch();
  
  public abstract SpdyDataFrame touch(Object paramObject);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyDataFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */