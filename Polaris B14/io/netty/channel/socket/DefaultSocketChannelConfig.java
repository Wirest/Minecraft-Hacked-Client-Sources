/*     */ package io.netty.channel.socket;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.DefaultChannelConfig;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultSocketChannelConfig
/*     */   extends DefaultChannelConfig
/*     */   implements SocketChannelConfig
/*     */ {
/*     */   protected final Socket javaSocket;
/*     */   private volatile boolean allowHalfClosure;
/*     */   
/*     */   public DefaultSocketChannelConfig(SocketChannel channel, Socket javaSocket)
/*     */   {
/*  45 */     super(channel);
/*  46 */     if (javaSocket == null) {
/*  47 */       throw new NullPointerException("javaSocket");
/*     */     }
/*  49 */     this.javaSocket = javaSocket;
/*     */     
/*     */ 
/*  52 */     if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
/*     */       try {
/*  54 */         setTcpNoDelay(true);
/*     */       }
/*     */       catch (Exception e) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<ChannelOption<?>, Object> getOptions()
/*     */   {
/*  63 */     return getOptions(super.getOptions(), new ChannelOption[] { ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.TCP_NODELAY, ChannelOption.SO_KEEPALIVE, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER, ChannelOption.IP_TOS, ChannelOption.ALLOW_HALF_CLOSURE });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T> T getOption(ChannelOption<T> option)
/*     */   {
/*  72 */     if (option == ChannelOption.SO_RCVBUF) {
/*  73 */       return Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  75 */     if (option == ChannelOption.SO_SNDBUF) {
/*  76 */       return Integer.valueOf(getSendBufferSize());
/*     */     }
/*  78 */     if (option == ChannelOption.TCP_NODELAY) {
/*  79 */       return Boolean.valueOf(isTcpNoDelay());
/*     */     }
/*  81 */     if (option == ChannelOption.SO_KEEPALIVE) {
/*  82 */       return Boolean.valueOf(isKeepAlive());
/*     */     }
/*  84 */     if (option == ChannelOption.SO_REUSEADDR) {
/*  85 */       return Boolean.valueOf(isReuseAddress());
/*     */     }
/*  87 */     if (option == ChannelOption.SO_LINGER) {
/*  88 */       return Integer.valueOf(getSoLinger());
/*     */     }
/*  90 */     if (option == ChannelOption.IP_TOS) {
/*  91 */       return Integer.valueOf(getTrafficClass());
/*     */     }
/*  93 */     if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
/*  94 */       return Boolean.valueOf(isAllowHalfClosure());
/*     */     }
/*     */     
/*  97 */     return (T)super.getOption(option);
/*     */   }
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */   {
/* 102 */     validate(option, value);
/*     */     
/* 104 */     if (option == ChannelOption.SO_RCVBUF) {
/* 105 */       setReceiveBufferSize(((Integer)value).intValue());
/* 106 */     } else if (option == ChannelOption.SO_SNDBUF) {
/* 107 */       setSendBufferSize(((Integer)value).intValue());
/* 108 */     } else if (option == ChannelOption.TCP_NODELAY) {
/* 109 */       setTcpNoDelay(((Boolean)value).booleanValue());
/* 110 */     } else if (option == ChannelOption.SO_KEEPALIVE) {
/* 111 */       setKeepAlive(((Boolean)value).booleanValue());
/* 112 */     } else if (option == ChannelOption.SO_REUSEADDR) {
/* 113 */       setReuseAddress(((Boolean)value).booleanValue());
/* 114 */     } else if (option == ChannelOption.SO_LINGER) {
/* 115 */       setSoLinger(((Integer)value).intValue());
/* 116 */     } else if (option == ChannelOption.IP_TOS) {
/* 117 */       setTrafficClass(((Integer)value).intValue());
/* 118 */     } else if (option == ChannelOption.ALLOW_HALF_CLOSURE) {
/* 119 */       setAllowHalfClosure(((Boolean)value).booleanValue());
/*     */     } else {
/* 121 */       return super.setOption(option, value);
/*     */     }
/*     */     
/* 124 */     return true;
/*     */   }
/*     */   
/*     */   public int getReceiveBufferSize()
/*     */   {
/*     */     try {
/* 130 */       return this.javaSocket.getReceiveBufferSize();
/*     */     } catch (SocketException e) {
/* 132 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getSendBufferSize()
/*     */   {
/*     */     try {
/* 139 */       return this.javaSocket.getSendBufferSize();
/*     */     } catch (SocketException e) {
/* 141 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getSoLinger()
/*     */   {
/*     */     try {
/* 148 */       return this.javaSocket.getSoLinger();
/*     */     } catch (SocketException e) {
/* 150 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getTrafficClass()
/*     */   {
/*     */     try {
/* 157 */       return this.javaSocket.getTrafficClass();
/*     */     } catch (SocketException e) {
/* 159 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isKeepAlive()
/*     */   {
/*     */     try {
/* 166 */       return this.javaSocket.getKeepAlive();
/*     */     } catch (SocketException e) {
/* 168 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isReuseAddress()
/*     */   {
/*     */     try {
/* 175 */       return this.javaSocket.getReuseAddress();
/*     */     } catch (SocketException e) {
/* 177 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isTcpNoDelay()
/*     */   {
/*     */     try {
/* 184 */       return this.javaSocket.getTcpNoDelay();
/*     */     } catch (SocketException e) {
/* 186 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public SocketChannelConfig setKeepAlive(boolean keepAlive)
/*     */   {
/*     */     try {
/* 193 */       this.javaSocket.setKeepAlive(keepAlive);
/*     */     } catch (SocketException e) {
/* 195 */       throw new ChannelException(e);
/*     */     }
/* 197 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public SocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth)
/*     */   {
/* 203 */     this.javaSocket.setPerformancePreferences(connectionTime, latency, bandwidth);
/* 204 */     return this;
/*     */   }
/*     */   
/*     */   public SocketChannelConfig setReceiveBufferSize(int receiveBufferSize)
/*     */   {
/*     */     try {
/* 210 */       this.javaSocket.setReceiveBufferSize(receiveBufferSize);
/*     */     } catch (SocketException e) {
/* 212 */       throw new ChannelException(e);
/*     */     }
/* 214 */     return this;
/*     */   }
/*     */   
/*     */   public SocketChannelConfig setReuseAddress(boolean reuseAddress)
/*     */   {
/*     */     try {
/* 220 */       this.javaSocket.setReuseAddress(reuseAddress);
/*     */     } catch (SocketException e) {
/* 222 */       throw new ChannelException(e);
/*     */     }
/* 224 */     return this;
/*     */   }
/*     */   
/*     */   public SocketChannelConfig setSendBufferSize(int sendBufferSize)
/*     */   {
/*     */     try {
/* 230 */       this.javaSocket.setSendBufferSize(sendBufferSize);
/*     */     } catch (SocketException e) {
/* 232 */       throw new ChannelException(e);
/*     */     }
/* 234 */     return this;
/*     */   }
/*     */   
/*     */   public SocketChannelConfig setSoLinger(int soLinger)
/*     */   {
/*     */     try {
/* 240 */       if (soLinger < 0) {
/* 241 */         this.javaSocket.setSoLinger(false, 0);
/*     */       } else {
/* 243 */         this.javaSocket.setSoLinger(true, soLinger);
/*     */       }
/*     */     } catch (SocketException e) {
/* 246 */       throw new ChannelException(e);
/*     */     }
/* 248 */     return this;
/*     */   }
/*     */   
/*     */   public SocketChannelConfig setTcpNoDelay(boolean tcpNoDelay)
/*     */   {
/*     */     try {
/* 254 */       this.javaSocket.setTcpNoDelay(tcpNoDelay);
/*     */     } catch (SocketException e) {
/* 256 */       throw new ChannelException(e);
/*     */     }
/* 258 */     return this;
/*     */   }
/*     */   
/*     */   public SocketChannelConfig setTrafficClass(int trafficClass)
/*     */   {
/*     */     try {
/* 264 */       this.javaSocket.setTrafficClass(trafficClass);
/*     */     } catch (SocketException e) {
/* 266 */       throw new ChannelException(e);
/*     */     }
/* 268 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isAllowHalfClosure()
/*     */   {
/* 273 */     return this.allowHalfClosure;
/*     */   }
/*     */   
/*     */   public SocketChannelConfig setAllowHalfClosure(boolean allowHalfClosure)
/*     */   {
/* 278 */     this.allowHalfClosure = allowHalfClosure;
/* 279 */     return this;
/*     */   }
/*     */   
/*     */   public SocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis)
/*     */   {
/* 284 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 285 */     return this;
/*     */   }
/*     */   
/*     */   public SocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead)
/*     */   {
/* 290 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 291 */     return this;
/*     */   }
/*     */   
/*     */   public SocketChannelConfig setWriteSpinCount(int writeSpinCount)
/*     */   {
/* 296 */     super.setWriteSpinCount(writeSpinCount);
/* 297 */     return this;
/*     */   }
/*     */   
/*     */   public SocketChannelConfig setAllocator(ByteBufAllocator allocator)
/*     */   {
/* 302 */     super.setAllocator(allocator);
/* 303 */     return this;
/*     */   }
/*     */   
/*     */   public SocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator)
/*     */   {
/* 308 */     super.setRecvByteBufAllocator(allocator);
/* 309 */     return this;
/*     */   }
/*     */   
/*     */   public SocketChannelConfig setAutoRead(boolean autoRead)
/*     */   {
/* 314 */     super.setAutoRead(autoRead);
/* 315 */     return this;
/*     */   }
/*     */   
/*     */   public SocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark)
/*     */   {
/* 320 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 321 */     return this;
/*     */   }
/*     */   
/*     */   public SocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark)
/*     */   {
/* 326 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 327 */     return this;
/*     */   }
/*     */   
/*     */   public SocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator)
/*     */   {
/* 332 */     super.setMessageSizeEstimator(estimator);
/* 333 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\socket\DefaultSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */