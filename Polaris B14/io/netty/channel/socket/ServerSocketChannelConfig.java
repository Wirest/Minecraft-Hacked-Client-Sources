package io.netty.channel.socket;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;

public abstract interface ServerSocketChannelConfig
  extends ChannelConfig
{
  public abstract int getBacklog();
  
  public abstract ServerSocketChannelConfig setBacklog(int paramInt);
  
  public abstract boolean isReuseAddress();
  
  public abstract ServerSocketChannelConfig setReuseAddress(boolean paramBoolean);
  
  public abstract int getReceiveBufferSize();
  
  public abstract ServerSocketChannelConfig setReceiveBufferSize(int paramInt);
  
  public abstract ServerSocketChannelConfig setPerformancePreferences(int paramInt1, int paramInt2, int paramInt3);
  
  public abstract ServerSocketChannelConfig setConnectTimeoutMillis(int paramInt);
  
  public abstract ServerSocketChannelConfig setMaxMessagesPerRead(int paramInt);
  
  public abstract ServerSocketChannelConfig setWriteSpinCount(int paramInt);
  
  public abstract ServerSocketChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator);
  
  public abstract ServerSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator paramRecvByteBufAllocator);
  
  public abstract ServerSocketChannelConfig setAutoRead(boolean paramBoolean);
  
  public abstract ServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator paramMessageSizeEstimator);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\socket\ServerSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */