/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.socket.ServerSocketChannelConfig;
/*     */ import io.netty.channel.unix.FileDescriptor;
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
/*     */ public final class EpollServerSocketChannelConfig
/*     */   extends EpollServerChannelConfig
/*     */   implements ServerSocketChannelConfig
/*     */ {
/*     */   EpollServerSocketChannelConfig(EpollServerSocketChannel channel)
/*     */   {
/*  30 */     super(channel);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  35 */     setReuseAddress(true);
/*     */   }
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions()
/*     */   {
/*  40 */     return getOptions(super.getOptions(), new ChannelOption[] { EpollChannelOption.SO_REUSEPORT });
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T getOption(ChannelOption<T> option)
/*     */   {
/*  46 */     if (option == EpollChannelOption.SO_REUSEPORT) {
/*  47 */       return Boolean.valueOf(isReusePort());
/*     */     }
/*  49 */     return (T)super.getOption(option);
/*     */   }
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */   {
/*  54 */     validate(option, value);
/*     */     
/*  56 */     if (option == EpollChannelOption.SO_REUSEPORT) {
/*  57 */       setReusePort(((Boolean)value).booleanValue());
/*     */     } else {
/*  59 */       return super.setOption(option, value);
/*     */     }
/*     */     
/*  62 */     return true;
/*     */   }
/*     */   
/*     */   public EpollServerSocketChannelConfig setReuseAddress(boolean reuseAddress)
/*     */   {
/*  67 */     super.setReuseAddress(reuseAddress);
/*  68 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerSocketChannelConfig setReceiveBufferSize(int receiveBufferSize)
/*     */   {
/*  73 */     super.setReceiveBufferSize(receiveBufferSize);
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerSocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth)
/*     */   {
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerSocketChannelConfig setBacklog(int backlog)
/*     */   {
/*  84 */     super.setBacklog(backlog);
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis)
/*     */   {
/*  90 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead)
/*     */   {
/*  96 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/*  97 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerSocketChannelConfig setWriteSpinCount(int writeSpinCount)
/*     */   {
/* 102 */     super.setWriteSpinCount(writeSpinCount);
/* 103 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerSocketChannelConfig setAllocator(ByteBufAllocator allocator)
/*     */   {
/* 108 */     super.setAllocator(allocator);
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator)
/*     */   {
/* 114 */     super.setRecvByteBufAllocator(allocator);
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerSocketChannelConfig setAutoRead(boolean autoRead)
/*     */   {
/* 120 */     super.setAutoRead(autoRead);
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark)
/*     */   {
/* 126 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 127 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark)
/*     */   {
/* 132 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 133 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator)
/*     */   {
/* 138 */     super.setMessageSizeEstimator(estimator);
/* 139 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isReusePort()
/*     */   {
/* 146 */     return Native.isReusePort(this.channel.fd().intValue()) == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EpollServerSocketChannelConfig setReusePort(boolean reusePort)
/*     */   {
/* 157 */     Native.setReusePort(this.channel.fd().intValue(), reusePort ? 1 : 0);
/* 158 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\EpollServerSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */