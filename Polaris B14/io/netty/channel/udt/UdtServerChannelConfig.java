package io.netty.channel.udt;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;

public abstract interface UdtServerChannelConfig
  extends UdtChannelConfig
{
  public abstract int getBacklog();
  
  public abstract UdtServerChannelConfig setBacklog(int paramInt);
  
  public abstract UdtServerChannelConfig setConnectTimeoutMillis(int paramInt);
  
  public abstract UdtServerChannelConfig setMaxMessagesPerRead(int paramInt);
  
  public abstract UdtServerChannelConfig setWriteSpinCount(int paramInt);
  
  public abstract UdtServerChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator);
  
  public abstract UdtServerChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator paramRecvByteBufAllocator);
  
  public abstract UdtServerChannelConfig setAutoRead(boolean paramBoolean);
  
  public abstract UdtServerChannelConfig setProtocolReceiveBufferSize(int paramInt);
  
  public abstract UdtServerChannelConfig setProtocolSendBufferSize(int paramInt);
  
  public abstract UdtServerChannelConfig setReceiveBufferSize(int paramInt);
  
  public abstract UdtServerChannelConfig setReuseAddress(boolean paramBoolean);
  
  public abstract UdtServerChannelConfig setSendBufferSize(int paramInt);
  
  public abstract UdtServerChannelConfig setSoLinger(int paramInt);
  
  public abstract UdtServerChannelConfig setSystemReceiveBufferSize(int paramInt);
  
  public abstract UdtServerChannelConfig setSystemSendBufferSize(int paramInt);
  
  public abstract UdtServerChannelConfig setWriteBufferHighWaterMark(int paramInt);
  
  public abstract UdtServerChannelConfig setWriteBufferLowWaterMark(int paramInt);
  
  public abstract UdtServerChannelConfig setMessageSizeEstimator(MessageSizeEstimator paramMessageSizeEstimator);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\udt\UdtServerChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */