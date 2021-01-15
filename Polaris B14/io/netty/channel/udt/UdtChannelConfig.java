package io.netty.channel.udt;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;

public abstract interface UdtChannelConfig
  extends ChannelConfig
{
  public abstract int getProtocolReceiveBufferSize();
  
  public abstract int getProtocolSendBufferSize();
  
  public abstract int getReceiveBufferSize();
  
  public abstract int getSendBufferSize();
  
  public abstract int getSoLinger();
  
  public abstract int getSystemReceiveBufferSize();
  
  public abstract int getSystemSendBufferSize();
  
  public abstract boolean isReuseAddress();
  
  public abstract UdtChannelConfig setConnectTimeoutMillis(int paramInt);
  
  public abstract UdtChannelConfig setMaxMessagesPerRead(int paramInt);
  
  public abstract UdtChannelConfig setWriteSpinCount(int paramInt);
  
  public abstract UdtChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator);
  
  public abstract UdtChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator paramRecvByteBufAllocator);
  
  public abstract UdtChannelConfig setAutoRead(boolean paramBoolean);
  
  public abstract UdtChannelConfig setWriteBufferHighWaterMark(int paramInt);
  
  public abstract UdtChannelConfig setWriteBufferLowWaterMark(int paramInt);
  
  public abstract UdtChannelConfig setMessageSizeEstimator(MessageSizeEstimator paramMessageSizeEstimator);
  
  public abstract UdtChannelConfig setProtocolReceiveBufferSize(int paramInt);
  
  public abstract UdtChannelConfig setProtocolSendBufferSize(int paramInt);
  
  public abstract UdtChannelConfig setReceiveBufferSize(int paramInt);
  
  public abstract UdtChannelConfig setReuseAddress(boolean paramBoolean);
  
  public abstract UdtChannelConfig setSendBufferSize(int paramInt);
  
  public abstract UdtChannelConfig setSoLinger(int paramInt);
  
  public abstract UdtChannelConfig setSystemReceiveBufferSize(int paramInt);
  
  public abstract UdtChannelConfig setSystemSendBufferSize(int paramInt);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\udt\UdtChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */