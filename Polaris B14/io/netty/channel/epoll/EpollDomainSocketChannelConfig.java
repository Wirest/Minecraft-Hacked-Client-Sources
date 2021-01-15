/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.unix.DomainSocketChannelConfig;
/*     */ import io.netty.channel.unix.DomainSocketReadMode;
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
/*     */ public final class EpollDomainSocketChannelConfig
/*     */   extends EpollChannelConfig
/*     */   implements DomainSocketChannelConfig
/*     */ {
/*  31 */   private volatile DomainSocketReadMode mode = DomainSocketReadMode.BYTES;
/*     */   
/*     */   EpollDomainSocketChannelConfig(AbstractEpollChannel channel)
/*     */   {
/*  35 */     super(channel);
/*     */   }
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions()
/*     */   {
/*  40 */     return getOptions(super.getOptions(), new ChannelOption[] { EpollChannelOption.DOMAIN_SOCKET_READ_MODE });
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T getOption(ChannelOption<T> option)
/*     */   {
/*  46 */     if (option == EpollChannelOption.DOMAIN_SOCKET_READ_MODE) {
/*  47 */       return getReadMode();
/*     */     }
/*  49 */     return (T)super.getOption(option);
/*     */   }
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */   {
/*  54 */     validate(option, value);
/*     */     
/*  56 */     if (option == EpollChannelOption.DOMAIN_SOCKET_READ_MODE) {
/*  57 */       setReadMode((DomainSocketReadMode)value);
/*     */     } else {
/*  59 */       return super.setOption(option, value);
/*     */     }
/*     */     
/*  62 */     return true;
/*     */   }
/*     */   
/*     */   public EpollDomainSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead) {
/*  66 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/*  67 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDomainSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis)
/*     */   {
/*  72 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/*  73 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDomainSocketChannelConfig setWriteSpinCount(int writeSpinCount)
/*     */   {
/*  78 */     super.setWriteSpinCount(writeSpinCount);
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDomainSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator)
/*     */   {
/*  84 */     super.setRecvByteBufAllocator(allocator);
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDomainSocketChannelConfig setAllocator(ByteBufAllocator allocator)
/*     */   {
/*  90 */     super.setAllocator(allocator);
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDomainSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator)
/*     */   {
/*  96 */     super.setMessageSizeEstimator(estimator);
/*  97 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDomainSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark)
/*     */   {
/* 102 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 103 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDomainSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark)
/*     */   {
/* 108 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDomainSocketChannelConfig setAutoRead(boolean autoRead)
/*     */   {
/* 114 */     super.setAutoRead(autoRead);
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDomainSocketChannelConfig setEpollMode(EpollMode mode)
/*     */   {
/* 120 */     super.setEpollMode(mode);
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public EpollDomainSocketChannelConfig setReadMode(DomainSocketReadMode mode)
/*     */   {
/* 126 */     if (mode == null) {
/* 127 */       throw new NullPointerException("mode");
/*     */     }
/* 129 */     this.mode = mode;
/* 130 */     return this;
/*     */   }
/*     */   
/*     */   public DomainSocketReadMode getReadMode()
/*     */   {
/* 135 */     return this.mode;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\EpollDomainSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */