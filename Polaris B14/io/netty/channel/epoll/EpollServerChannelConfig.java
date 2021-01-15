/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.util.NetUtil;
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
/*     */ public class EpollServerChannelConfig
/*     */   extends EpollChannelConfig
/*     */ {
/*     */   protected final AbstractEpollChannel channel;
/*  32 */   private volatile int backlog = NetUtil.SOMAXCONN;
/*     */   
/*     */   EpollServerChannelConfig(AbstractEpollChannel channel) {
/*  35 */     super(channel);
/*  36 */     this.channel = channel;
/*     */   }
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions()
/*     */   {
/*  41 */     return getOptions(super.getOptions(), new ChannelOption[] { ChannelOption.SO_RCVBUF, ChannelOption.SO_REUSEADDR, ChannelOption.SO_BACKLOG });
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T getOption(ChannelOption<T> option)
/*     */   {
/*  47 */     if (option == ChannelOption.SO_RCVBUF) {
/*  48 */       return Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  50 */     if (option == ChannelOption.SO_REUSEADDR) {
/*  51 */       return Boolean.valueOf(isReuseAddress());
/*     */     }
/*  53 */     if (option == ChannelOption.SO_BACKLOG) {
/*  54 */       return Integer.valueOf(getBacklog());
/*     */     }
/*  56 */     return (T)super.getOption(option);
/*     */   }
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */   {
/*  61 */     validate(option, value);
/*     */     
/*  63 */     if (option == ChannelOption.SO_RCVBUF) {
/*  64 */       setReceiveBufferSize(((Integer)value).intValue());
/*  65 */     } else if (option == ChannelOption.SO_REUSEADDR) {
/*  66 */       setReuseAddress(((Boolean)value).booleanValue());
/*  67 */     } else if (option == ChannelOption.SO_BACKLOG) {
/*  68 */       setBacklog(((Integer)value).intValue());
/*     */     } else {
/*  70 */       return super.setOption(option, value);
/*     */     }
/*     */     
/*  73 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isReuseAddress() {
/*  77 */     return Native.isReuseAddress(this.channel.fd().intValue()) == 1;
/*     */   }
/*     */   
/*     */   public EpollServerChannelConfig setReuseAddress(boolean reuseAddress) {
/*  81 */     Native.setReuseAddress(this.channel.fd().intValue(), reuseAddress ? 1 : 0);
/*  82 */     return this;
/*     */   }
/*     */   
/*     */   public int getReceiveBufferSize() {
/*  86 */     return Native.getReceiveBufferSize(this.channel.fd().intValue());
/*     */   }
/*     */   
/*     */   public EpollServerChannelConfig setReceiveBufferSize(int receiveBufferSize) {
/*  90 */     Native.setReceiveBufferSize(this.channel.fd().intValue(), receiveBufferSize);
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public int getBacklog() {
/*  95 */     return this.backlog;
/*     */   }
/*     */   
/*     */   public EpollServerChannelConfig setBacklog(int backlog) {
/*  99 */     if (backlog < 0) {
/* 100 */       throw new IllegalArgumentException("backlog: " + backlog);
/*     */     }
/* 102 */     this.backlog = backlog;
/* 103 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis)
/*     */   {
/* 108 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 109 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead)
/*     */   {
/* 114 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 115 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerChannelConfig setWriteSpinCount(int writeSpinCount)
/*     */   {
/* 120 */     super.setWriteSpinCount(writeSpinCount);
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerChannelConfig setAllocator(ByteBufAllocator allocator)
/*     */   {
/* 126 */     super.setAllocator(allocator);
/* 127 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator)
/*     */   {
/* 132 */     super.setRecvByteBufAllocator(allocator);
/* 133 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerChannelConfig setAutoRead(boolean autoRead)
/*     */   {
/* 138 */     super.setAutoRead(autoRead);
/* 139 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark)
/*     */   {
/* 144 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 145 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark)
/*     */   {
/* 150 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 151 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator)
/*     */   {
/* 156 */     super.setMessageSizeEstimator(estimator);
/* 157 */     return this;
/*     */   }
/*     */   
/*     */   public EpollServerChannelConfig setEpollMode(EpollMode mode)
/*     */   {
/* 162 */     super.setEpollMode(mode);
/* 163 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\EpollServerChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */