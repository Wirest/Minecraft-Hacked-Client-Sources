package io.netty.channel.unix;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;

public abstract interface DomainSocketChannelConfig
  extends ChannelConfig
{
  public abstract DomainSocketChannelConfig setMaxMessagesPerRead(int paramInt);
  
  public abstract DomainSocketChannelConfig setConnectTimeoutMillis(int paramInt);
  
  public abstract DomainSocketChannelConfig setWriteSpinCount(int paramInt);
  
  public abstract DomainSocketChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator);
  
  public abstract DomainSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator paramRecvByteBufAllocator);
  
  public abstract DomainSocketChannelConfig setAutoRead(boolean paramBoolean);
  
  public abstract DomainSocketChannelConfig setWriteBufferHighWaterMark(int paramInt);
  
  public abstract DomainSocketChannelConfig setWriteBufferLowWaterMark(int paramInt);
  
  public abstract DomainSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator paramMessageSizeEstimator);
  
  public abstract DomainSocketChannelConfig setReadMode(DomainSocketReadMode paramDomainSocketReadMode);
  
  public abstract DomainSocketReadMode getReadMode();
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\unix\DomainSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */