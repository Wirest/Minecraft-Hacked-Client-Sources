/*     */ package io.netty.channel.socket.oio;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.socket.DefaultServerSocketChannelConfig;
/*     */ import io.netty.channel.socket.ServerSocketChannel;
/*     */ import java.io.IOException;
/*     */ import java.net.ServerSocket;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultOioServerSocketChannelConfig
/*     */   extends DefaultServerSocketChannelConfig
/*     */   implements OioServerSocketChannelConfig
/*     */ {
/*     */   @Deprecated
/*     */   public DefaultOioServerSocketChannelConfig(ServerSocketChannel channel, ServerSocket javaSocket)
/*     */   {
/*  40 */     super(channel, javaSocket);
/*     */   }
/*     */   
/*     */   DefaultOioServerSocketChannelConfig(OioServerSocketChannel channel, ServerSocket javaSocket) {
/*  44 */     super(channel, javaSocket);
/*     */   }
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions()
/*     */   {
/*  49 */     return getOptions(super.getOptions(), new ChannelOption[] { ChannelOption.SO_TIMEOUT });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public <T> T getOption(ChannelOption<T> option)
/*     */   {
/*  56 */     if (option == ChannelOption.SO_TIMEOUT) {
/*  57 */       return Integer.valueOf(getSoTimeout());
/*     */     }
/*  59 */     return (T)super.getOption(option);
/*     */   }
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */   {
/*  64 */     validate(option, value);
/*     */     
/*  66 */     if (option == ChannelOption.SO_TIMEOUT) {
/*  67 */       setSoTimeout(((Integer)value).intValue());
/*     */     } else {
/*  69 */       return super.setOption(option, value);
/*     */     }
/*  71 */     return true;
/*     */   }
/*     */   
/*     */   public OioServerSocketChannelConfig setSoTimeout(int timeout)
/*     */   {
/*     */     try {
/*  77 */       this.javaSocket.setSoTimeout(timeout);
/*     */     } catch (IOException e) {
/*  79 */       throw new ChannelException(e);
/*     */     }
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public int getSoTimeout()
/*     */   {
/*     */     try {
/*  87 */       return this.javaSocket.getSoTimeout();
/*     */     } catch (IOException e) {
/*  89 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public OioServerSocketChannelConfig setBacklog(int backlog)
/*     */   {
/*  95 */     super.setBacklog(backlog);
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   public OioServerSocketChannelConfig setReuseAddress(boolean reuseAddress)
/*     */   {
/* 101 */     super.setReuseAddress(reuseAddress);
/* 102 */     return this;
/*     */   }
/*     */   
/*     */   public OioServerSocketChannelConfig setReceiveBufferSize(int receiveBufferSize)
/*     */   {
/* 107 */     super.setReceiveBufferSize(receiveBufferSize);
/* 108 */     return this;
/*     */   }
/*     */   
/*     */   public OioServerSocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth)
/*     */   {
/* 113 */     super.setPerformancePreferences(connectionTime, latency, bandwidth);
/* 114 */     return this;
/*     */   }
/*     */   
/*     */   public OioServerSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis)
/*     */   {
/* 119 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 120 */     return this;
/*     */   }
/*     */   
/*     */   public OioServerSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead)
/*     */   {
/* 125 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 126 */     return this;
/*     */   }
/*     */   
/*     */   public OioServerSocketChannelConfig setWriteSpinCount(int writeSpinCount)
/*     */   {
/* 131 */     super.setWriteSpinCount(writeSpinCount);
/* 132 */     return this;
/*     */   }
/*     */   
/*     */   public OioServerSocketChannelConfig setAllocator(ByteBufAllocator allocator)
/*     */   {
/* 137 */     super.setAllocator(allocator);
/* 138 */     return this;
/*     */   }
/*     */   
/*     */   public OioServerSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator)
/*     */   {
/* 143 */     super.setRecvByteBufAllocator(allocator);
/* 144 */     return this;
/*     */   }
/*     */   
/*     */   public OioServerSocketChannelConfig setAutoRead(boolean autoRead)
/*     */   {
/* 149 */     super.setAutoRead(autoRead);
/* 150 */     return this;
/*     */   }
/*     */   
/*     */   protected void autoReadCleared()
/*     */   {
/* 155 */     if ((this.channel instanceof OioServerSocketChannel)) {
/* 156 */       ((OioServerSocketChannel)this.channel).setReadPending(false);
/*     */     }
/*     */   }
/*     */   
/*     */   public OioServerSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark)
/*     */   {
/* 162 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 163 */     return this;
/*     */   }
/*     */   
/*     */   public OioServerSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark)
/*     */   {
/* 168 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 169 */     return this;
/*     */   }
/*     */   
/*     */   public OioServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator)
/*     */   {
/* 174 */     super.setMessageSizeEstimator(estimator);
/* 175 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\socket\oio\DefaultOioServerSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */