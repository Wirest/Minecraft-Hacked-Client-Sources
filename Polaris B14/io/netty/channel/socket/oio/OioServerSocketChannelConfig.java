package io.netty.channel.socket.oio;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.MessageSizeEstimator;
import io.netty.channel.RecvByteBufAllocator;
import io.netty.channel.socket.ServerSocketChannelConfig;

public abstract interface OioServerSocketChannelConfig
  extends ServerSocketChannelConfig
{
  public abstract OioServerSocketChannelConfig setSoTimeout(int paramInt);
  
  public abstract int getSoTimeout();
  
  public abstract OioServerSocketChannelConfig setBacklog(int paramInt);
  
  public abstract OioServerSocketChannelConfig setReuseAddress(boolean paramBoolean);
  
  public abstract OioServerSocketChannelConfig setReceiveBufferSize(int paramInt);
  
  public abstract OioServerSocketChannelConfig setPerformancePreferences(int paramInt1, int paramInt2, int paramInt3);
  
  public abstract OioServerSocketChannelConfig setConnectTimeoutMillis(int paramInt);
  
  public abstract OioServerSocketChannelConfig setMaxMessagesPerRead(int paramInt);
  
  public abstract OioServerSocketChannelConfig setWriteSpinCount(int paramInt);
  
  public abstract OioServerSocketChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator);
  
  public abstract OioServerSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator paramRecvByteBufAllocator);
  
  public abstract OioServerSocketChannelConfig setAutoRead(boolean paramBoolean);
  
  public abstract OioServerSocketChannelConfig setWriteBufferHighWaterMark(int paramInt);
  
  public abstract OioServerSocketChannelConfig setWriteBufferLowWaterMark(int paramInt);
  
  public abstract OioServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator paramMessageSizeEstimator);
}


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\socket\oio\OioServerSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */