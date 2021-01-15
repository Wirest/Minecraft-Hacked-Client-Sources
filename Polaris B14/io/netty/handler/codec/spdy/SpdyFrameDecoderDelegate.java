package io.netty.handler.codec.spdy;

import io.netty.buffer.ByteBuf;

public abstract interface SpdyFrameDecoderDelegate
{
  public abstract void readDataFrame(int paramInt, boolean paramBoolean, ByteBuf paramByteBuf);
  
  public abstract void readSynStreamFrame(int paramInt1, int paramInt2, byte paramByte, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract void readSynReplyFrame(int paramInt, boolean paramBoolean);
  
  public abstract void readRstStreamFrame(int paramInt1, int paramInt2);
  
  public abstract void readSettingsFrame(boolean paramBoolean);
  
  public abstract void readSetting(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract void readSettingsEnd();
  
  public abstract void readPingFrame(int paramInt);
  
  public abstract void readGoAwayFrame(int paramInt1, int paramInt2);
  
  public abstract void readHeadersFrame(int paramInt, boolean paramBoolean);
  
  public abstract void readWindowUpdateFrame(int paramInt1, int paramInt2);
  
  public abstract void readHeaderBlock(ByteBuf paramByteBuf);
  
  public abstract void readHeaderBlockEnd();
  
  public abstract void readFrameError(String paramString);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\SpdyFrameDecoderDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */