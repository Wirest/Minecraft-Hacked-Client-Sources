/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.DefaultChannelConfig;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
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
/*     */ public class EpollChannelConfig
/*     */   extends DefaultChannelConfig
/*     */ {
/*     */   final AbstractEpollChannel channel;
/*     */   
/*     */   EpollChannelConfig(AbstractEpollChannel channel)
/*     */   {
/*  30 */     super(channel);
/*  31 */     this.channel = channel;
/*     */   }
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions()
/*     */   {
/*  36 */     return getOptions(super.getOptions(), new ChannelOption[] { EpollChannelOption.EPOLL_MODE });
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T getOption(ChannelOption<T> option)
/*     */   {
/*  42 */     if (option == EpollChannelOption.EPOLL_MODE) {
/*  43 */       return getEpollMode();
/*     */     }
/*  45 */     return (T)super.getOption(option);
/*     */   }
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */   {
/*  50 */     validate(option, value);
/*  51 */     if (option == EpollChannelOption.EPOLL_MODE) {
/*  52 */       setEpollMode((EpollMode)value);
/*     */     } else {
/*  54 */       return super.setOption(option, value);
/*     */     }
/*  56 */     return true;
/*     */   }
/*     */   
/*     */   public EpollChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis)
/*     */   {
/*  61 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/*  62 */     return this;
/*     */   }
/*     */   
/*     */   public EpollChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead)
/*     */   {
/*  67 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/*  68 */     return this;
/*     */   }
/*     */   
/*     */   public EpollChannelConfig setWriteSpinCount(int writeSpinCount)
/*     */   {
/*  73 */     super.setWriteSpinCount(writeSpinCount);
/*  74 */     return this;
/*     */   }
/*     */   
/*     */   public EpollChannelConfig setAllocator(ByteBufAllocator allocator)
/*     */   {
/*  79 */     super.setAllocator(allocator);
/*  80 */     return this;
/*     */   }
/*     */   
/*     */   public EpollChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator)
/*     */   {
/*  85 */     super.setRecvByteBufAllocator(allocator);
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public EpollChannelConfig setAutoRead(boolean autoRead)
/*     */   {
/*  91 */     super.setAutoRead(autoRead);
/*  92 */     return this;
/*     */   }
/*     */   
/*     */   public EpollChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark)
/*     */   {
/*  97 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/*  98 */     return this;
/*     */   }
/*     */   
/*     */   public EpollChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark)
/*     */   {
/* 103 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public EpollChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator)
/*     */   {
/* 109 */     super.setMessageSizeEstimator(estimator);
/* 110 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EpollMode getEpollMode()
/*     */   {
/* 120 */     return this.channel.isFlagSet(Native.EPOLLET) ? EpollMode.EDGE_TRIGGERED : EpollMode.LEVEL_TRIGGERED;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EpollChannelConfig setEpollMode(EpollMode mode)
/*     */   {
/* 133 */     if (mode == null) {
/* 134 */       throw new NullPointerException("mode");
/*     */     }
/* 136 */     switch (mode) {
/*     */     case EDGE_TRIGGERED: 
/* 138 */       checkChannelNotRegistered();
/* 139 */       this.channel.setFlag(Native.EPOLLET);
/* 140 */       break;
/*     */     case LEVEL_TRIGGERED: 
/* 142 */       checkChannelNotRegistered();
/* 143 */       this.channel.clearFlag(Native.EPOLLET);
/* 144 */       break;
/*     */     default: 
/* 146 */       throw new Error();
/*     */     }
/* 148 */     return this;
/*     */   }
/*     */   
/*     */   private void checkChannelNotRegistered() {
/* 152 */     if (this.channel.isRegistered()) {
/* 153 */       throw new IllegalStateException("EpollMode can only be changed before channel is registered");
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void autoReadCleared()
/*     */   {
/* 159 */     this.channel.clearEpollIn();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\EpollChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */