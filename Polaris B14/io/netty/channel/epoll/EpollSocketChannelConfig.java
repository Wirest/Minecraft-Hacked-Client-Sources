/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.socket.SocketChannelConfig;
/*     */ import io.netty.channel.unix.FileDescriptor;
/*     */ import io.netty.util.internal.PlatformDependent;
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
/*     */ public final class EpollSocketChannelConfig
/*     */   extends EpollChannelConfig
/*     */   implements SocketChannelConfig
/*     */ {
/*     */   private final EpollSocketChannel channel;
/*     */   private volatile boolean allowHalfClosure;
/*     */   
/*     */   EpollSocketChannelConfig(EpollSocketChannel channel)
/*     */   {
/*  38 */     super(channel);
/*     */     
/*  40 */     this.channel = channel;
/*  41 */     if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
/*  42 */       setTcpNoDelay(true);
/*     */     }
/*     */   }
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions()
/*     */   {
/*  48 */     return getOptions(super.getOptions(), new ChannelOption[] { ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.TCP_NODELAY, ChannelOption.SO_KEEPALIVE, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER, ChannelOption.IP_TOS, ChannelOption.ALLOW_HALF_CLOSURE, EpollChannelOption.TCP_CORK, EpollChannelOption.TCP_KEEPCNT, EpollChannelOption.TCP_KEEPIDLE, EpollChannelOption.TCP_KEEPINTVL });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T> T getOption(ChannelOption<T> option)
/*     */   {
/*  58 */     if (option == ChannelOption.SO_RCVBUF) {
/*  59 */       return Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  61 */     if (option == ChannelOption.SO_SNDBUF) {
/*  62 */       return Integer.valueOf(getSendBufferSize());
/*     */     }
/*  64 */     if (option == ChannelOption.TCP_NODELAY) {
/*  65 */       return Boolean.valueOf(isTcpNoDelay());
/*     */     }
/*  67 */     if (option == ChannelOption.SO_KEEPALIVE) {
/*  68 */       return Boolean.valueOf(isKeepAlive());
/*     */     }
/*  70 */     if (option == ChannelOption.SO_REUSEADDR) {
/*  71 */       return Boolean.valueOf(isReuseAddress());
/*     */     }
/*  73 */     if (option == ChannelOption.SO_LINGER) {
/*  74 */       return Integer.valueOf(getSoLinger());
/*     */     }
/*  76 */     if (option == ChannelOption.IP_TOS) {
/*  77 */       return Integer.valueOf(getTrafficClass());
/*     */     }
/*  79 */     if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
/*  80 */       return Boolean.valueOf(isAllowHalfClosure());
/*     */     }
/*  82 */     if (option == EpollChannelOption.TCP_CORK) {
/*  83 */       return Boolean.valueOf(isTcpCork());
/*     */     }
/*  85 */     if (option == EpollChannelOption.TCP_KEEPIDLE) {
/*  86 */       return Integer.valueOf(getTcpKeepIdle());
/*     */     }
/*  88 */     if (option == EpollChannelOption.TCP_KEEPINTVL) {
/*  89 */       return Integer.valueOf(getTcpKeepIntvl());
/*     */     }
/*  91 */     if (option == EpollChannelOption.TCP_KEEPCNT) {
/*  92 */       return Integer.valueOf(getTcpKeepCnt());
/*     */     }
/*  94 */     return (T)super.getOption(option);
/*     */   }
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */   {
/*  99 */     validate(option, value);
/*     */     
/* 101 */     if (option == ChannelOption.SO_RCVBUF) {
/* 102 */       setReceiveBufferSize(((Integer)value).intValue());
/* 103 */     } else if (option == ChannelOption.SO_SNDBUF) {
/* 104 */       setSendBufferSize(((Integer)value).intValue());
/* 105 */     } else if (option == ChannelOption.TCP_NODELAY) {
/* 106 */       setTcpNoDelay(((Boolean)value).booleanValue());
/* 107 */     } else if (option == ChannelOption.SO_KEEPALIVE) {
/* 108 */       setKeepAlive(((Boolean)value).booleanValue());
/* 109 */     } else if (option == ChannelOption.SO_REUSEADDR) {
/* 110 */       setReuseAddress(((Boolean)value).booleanValue());
/* 111 */     } else if (option == ChannelOption.SO_LINGER) {
/* 112 */       setSoLinger(((Integer)value).intValue());
/* 113 */     } else if (option == ChannelOption.IP_TOS) {
/* 114 */       setTrafficClass(((Integer)value).intValue());
/* 115 */     } else if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
/* 116 */       setAllowHalfClosure(((Boolean)value).booleanValue());
/* 117 */     } else if (option == EpollChannelOption.TCP_CORK) {
/* 118 */       setTcpCork(((Boolean)value).booleanValue());
/* 119 */     } else if (option == EpollChannelOption.TCP_KEEPIDLE) {
/* 120 */       setTcpKeepIdle(((Integer)value).intValue());
/* 121 */     } else if (option == EpollChannelOption.TCP_KEEPCNT) {
/* 122 */       setTcpKeepCntl(((Integer)value).intValue());
/* 123 */     } else if (option == EpollChannelOption.TCP_KEEPINTVL) {
/* 124 */       setTcpKeepIntvl(((Integer)value).intValue());
/*     */     } else {
/* 126 */       return super.setOption(option, value);
/*     */     }
/*     */     
/* 129 */     return true;
/*     */   }
/*     */   
/*     */   public int getReceiveBufferSize()
/*     */   {
/* 134 */     return Native.getReceiveBufferSize(this.channel.fd().intValue());
/*     */   }
/*     */   
/*     */   public int getSendBufferSize()
/*     */   {
/* 139 */     return Native.getSendBufferSize(this.channel.fd().intValue());
/*     */   }
/*     */   
/*     */   public int getSoLinger()
/*     */   {
/* 144 */     return Native.getSoLinger(this.channel.fd().intValue());
/*     */   }
/*     */   
/*     */   public int getTrafficClass()
/*     */   {
/* 149 */     return Native.getTrafficClass(this.channel.fd().intValue());
/*     */   }
/*     */   
/*     */   public boolean isKeepAlive()
/*     */   {
/* 154 */     return Native.isKeepAlive(this.channel.fd().intValue()) == 1;
/*     */   }
/*     */   
/*     */   public boolean isReuseAddress()
/*     */   {
/* 159 */     return Native.isReuseAddress(this.channel.fd().intValue()) == 1;
/*     */   }
/*     */   
/*     */   public boolean isTcpNoDelay()
/*     */   {
/* 164 */     return Native.isTcpNoDelay(this.channel.fd().intValue()) == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isTcpCork()
/*     */   {
/* 171 */     return Native.isTcpCork(this.channel.fd().intValue()) == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getTcpKeepIdle()
/*     */   {
/* 178 */     return Native.getTcpKeepIdle(this.channel.fd().intValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getTcpKeepIntvl()
/*     */   {
/* 185 */     return Native.getTcpKeepIntvl(this.channel.fd().intValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getTcpKeepCnt()
/*     */   {
/* 192 */     return Native.getTcpKeepCnt(this.channel.fd().intValue());
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setKeepAlive(boolean keepAlive)
/*     */   {
/* 197 */     Native.setKeepAlive(this.channel.fd().intValue(), keepAlive ? 1 : 0);
/* 198 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public EpollSocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth)
/*     */   {
/* 204 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setReceiveBufferSize(int receiveBufferSize)
/*     */   {
/* 209 */     Native.setReceiveBufferSize(this.channel.fd().intValue(), receiveBufferSize);
/* 210 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setReuseAddress(boolean reuseAddress)
/*     */   {
/* 215 */     Native.setReuseAddress(this.channel.fd().intValue(), reuseAddress ? 1 : 0);
/* 216 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setSendBufferSize(int sendBufferSize)
/*     */   {
/* 221 */     Native.setSendBufferSize(this.channel.fd().intValue(), sendBufferSize);
/* 222 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setSoLinger(int soLinger)
/*     */   {
/* 227 */     Native.setSoLinger(this.channel.fd().intValue(), soLinger);
/* 228 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setTcpNoDelay(boolean tcpNoDelay)
/*     */   {
/* 233 */     Native.setTcpNoDelay(this.channel.fd().intValue(), tcpNoDelay ? 1 : 0);
/* 234 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EpollSocketChannelConfig setTcpCork(boolean tcpCork)
/*     */   {
/* 241 */     Native.setTcpCork(this.channel.fd().intValue(), tcpCork ? 1 : 0);
/* 242 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setTrafficClass(int trafficClass)
/*     */   {
/* 247 */     Native.setTrafficClass(this.channel.fd().intValue(), trafficClass);
/* 248 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EpollSocketChannelConfig setTcpKeepIdle(int seconds)
/*     */   {
/* 255 */     Native.setTcpKeepIdle(this.channel.fd().intValue(), seconds);
/* 256 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EpollSocketChannelConfig setTcpKeepIntvl(int seconds)
/*     */   {
/* 263 */     Native.setTcpKeepIntvl(this.channel.fd().intValue(), seconds);
/* 264 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EpollSocketChannelConfig setTcpKeepCntl(int probes)
/*     */   {
/* 271 */     Native.setTcpKeepCnt(this.channel.fd().intValue(), probes);
/* 272 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isAllowHalfClosure()
/*     */   {
/* 277 */     return this.allowHalfClosure;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setAllowHalfClosure(boolean allowHalfClosure)
/*     */   {
/* 282 */     this.allowHalfClosure = allowHalfClosure;
/* 283 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis)
/*     */   {
/* 288 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 289 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead)
/*     */   {
/* 294 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 295 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setWriteSpinCount(int writeSpinCount)
/*     */   {
/* 300 */     super.setWriteSpinCount(writeSpinCount);
/* 301 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setAllocator(ByteBufAllocator allocator)
/*     */   {
/* 306 */     super.setAllocator(allocator);
/* 307 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator)
/*     */   {
/* 312 */     super.setRecvByteBufAllocator(allocator);
/* 313 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setAutoRead(boolean autoRead)
/*     */   {
/* 318 */     super.setAutoRead(autoRead);
/* 319 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark)
/*     */   {
/* 324 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 325 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark)
/*     */   {
/* 330 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 331 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator)
/*     */   {
/* 336 */     super.setMessageSizeEstimator(estimator);
/* 337 */     return this;
/*     */   }
/*     */   
/*     */   public EpollSocketChannelConfig setEpollMode(EpollMode mode)
/*     */   {
/* 342 */     super.setEpollMode(mode);
/* 343 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\EpollSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */