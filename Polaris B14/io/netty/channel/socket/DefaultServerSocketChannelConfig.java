/*     */ package io.netty.channel.socket;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.DefaultChannelConfig;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.util.NetUtil;
/*     */ import java.net.ServerSocket;
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
/*     */ public class DefaultServerSocketChannelConfig
/*     */   extends DefaultChannelConfig
/*     */   implements ServerSocketChannelConfig
/*     */ {
/*     */   protected final ServerSocket javaSocket;
/*  39 */   private volatile int backlog = NetUtil.SOMAXCONN;
/*     */   
/*     */ 
/*     */ 
/*     */   public DefaultServerSocketChannelConfig(ServerSocketChannel channel, ServerSocket javaSocket)
/*     */   {
/*  45 */     super(channel);
/*  46 */     if (javaSocket == null) {
/*  47 */       throw new NullPointerException("javaSocket");
/*     */     }
/*  49 */     this.javaSocket = javaSocket;
/*     */   }
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions()
/*     */   {
/*  54 */     return getOptions(super.getOptions(), new ChannelOption[] { ChannelOption.SO_RCVBUF, ChannelOption.SO_REUSEADDR, ChannelOption.SO_BACKLOG });
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T getOption(ChannelOption<T> option)
/*     */   {
/*  60 */     if (option == ChannelOption.SO_RCVBUF) {
/*  61 */       return Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  63 */     if (option == ChannelOption.SO_REUSEADDR) {
/*  64 */       return Boolean.valueOf(isReuseAddress());
/*     */     }
/*  66 */     if (option == ChannelOption.SO_BACKLOG) {
/*  67 */       return Integer.valueOf(getBacklog());
/*     */     }
/*     */     
/*  70 */     return (T)super.getOption(option);
/*     */   }
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */   {
/*  75 */     validate(option, value);
/*     */     
/*  77 */     if (option == ChannelOption.SO_RCVBUF) {
/*  78 */       setReceiveBufferSize(((Integer)value).intValue());
/*  79 */     } else if (option == ChannelOption.SO_REUSEADDR) {
/*  80 */       setReuseAddress(((Boolean)value).booleanValue());
/*  81 */     } else if (option == ChannelOption.SO_BACKLOG) {
/*  82 */       setBacklog(((Integer)value).intValue());
/*     */     } else {
/*  84 */       return super.setOption(option, value);
/*     */     }
/*     */     
/*  87 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isReuseAddress()
/*     */   {
/*     */     try {
/*  93 */       return this.javaSocket.getReuseAddress();
/*     */     } catch (SocketException e) {
/*  95 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public ServerSocketChannelConfig setReuseAddress(boolean reuseAddress)
/*     */   {
/*     */     try {
/* 102 */       this.javaSocket.setReuseAddress(reuseAddress);
/*     */     } catch (SocketException e) {
/* 104 */       throw new ChannelException(e);
/*     */     }
/* 106 */     return this;
/*     */   }
/*     */   
/*     */   public int getReceiveBufferSize()
/*     */   {
/*     */     try {
/* 112 */       return this.javaSocket.getReceiveBufferSize();
/*     */     } catch (SocketException e) {
/* 114 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public ServerSocketChannelConfig setReceiveBufferSize(int receiveBufferSize)
/*     */   {
/*     */     try {
/* 121 */       this.javaSocket.setReceiveBufferSize(receiveBufferSize);
/*     */     } catch (SocketException e) {
/* 123 */       throw new ChannelException(e);
/*     */     }
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   public ServerSocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth)
/*     */   {
/* 130 */     this.javaSocket.setPerformancePreferences(connectionTime, latency, bandwidth);
/* 131 */     return this;
/*     */   }
/*     */   
/*     */   public int getBacklog()
/*     */   {
/* 136 */     return this.backlog;
/*     */   }
/*     */   
/*     */   public ServerSocketChannelConfig setBacklog(int backlog)
/*     */   {
/* 141 */     if (backlog < 0) {
/* 142 */       throw new IllegalArgumentException("backlog: " + backlog);
/*     */     }
/* 144 */     this.backlog = backlog;
/* 145 */     return this;
/*     */   }
/*     */   
/*     */   public ServerSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis)
/*     */   {
/* 150 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 151 */     return this;
/*     */   }
/*     */   
/*     */   public ServerSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead)
/*     */   {
/* 156 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 157 */     return this;
/*     */   }
/*     */   
/*     */   public ServerSocketChannelConfig setWriteSpinCount(int writeSpinCount)
/*     */   {
/* 162 */     super.setWriteSpinCount(writeSpinCount);
/* 163 */     return this;
/*     */   }
/*     */   
/*     */   public ServerSocketChannelConfig setAllocator(ByteBufAllocator allocator)
/*     */   {
/* 168 */     super.setAllocator(allocator);
/* 169 */     return this;
/*     */   }
/*     */   
/*     */   public ServerSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator)
/*     */   {
/* 174 */     super.setRecvByteBufAllocator(allocator);
/* 175 */     return this;
/*     */   }
/*     */   
/*     */   public ServerSocketChannelConfig setAutoRead(boolean autoRead)
/*     */   {
/* 180 */     super.setAutoRead(autoRead);
/* 181 */     return this;
/*     */   }
/*     */   
/*     */   public ServerSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark)
/*     */   {
/* 186 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 187 */     return this;
/*     */   }
/*     */   
/*     */   public ServerSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark)
/*     */   {
/* 192 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 193 */     return this;
/*     */   }
/*     */   
/*     */   public ServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator)
/*     */   {
/* 198 */     super.setMessageSizeEstimator(estimator);
/* 199 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\socket\DefaultServerSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */