/*     */ package io.netty.channel.socket.oio;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.channel.socket.DefaultSocketChannelConfig;
/*     */ import io.netty.channel.socket.SocketChannel;
/*     */ import java.io.IOException;
/*     */ import java.net.Socket;
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
/*     */ public class DefaultOioSocketChannelConfig
/*     */   extends DefaultSocketChannelConfig
/*     */   implements OioSocketChannelConfig
/*     */ {
/*     */   @Deprecated
/*     */   public DefaultOioSocketChannelConfig(SocketChannel channel, Socket javaSocket)
/*     */   {
/*  38 */     super(channel, javaSocket);
/*     */   }
/*     */   
/*     */   DefaultOioSocketChannelConfig(OioSocketChannel channel, Socket javaSocket) {
/*  42 */     super(channel, javaSocket);
/*     */   }
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions()
/*     */   {
/*  47 */     return getOptions(super.getOptions(), new ChannelOption[] { ChannelOption.SO_TIMEOUT });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public <T> T getOption(ChannelOption<T> option)
/*     */   {
/*  54 */     if (option == ChannelOption.SO_TIMEOUT) {
/*  55 */       return Integer.valueOf(getSoTimeout());
/*     */     }
/*  57 */     return (T)super.getOption(option);
/*     */   }
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */   {
/*  62 */     validate(option, value);
/*     */     
/*  64 */     if (option == ChannelOption.SO_TIMEOUT) {
/*  65 */       setSoTimeout(((Integer)value).intValue());
/*     */     } else {
/*  67 */       return super.setOption(option, value);
/*     */     }
/*  69 */     return true;
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setSoTimeout(int timeout)
/*     */   {
/*     */     try {
/*  75 */       this.javaSocket.setSoTimeout(timeout);
/*     */     } catch (IOException e) {
/*  77 */       throw new ChannelException(e);
/*     */     }
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public int getSoTimeout()
/*     */   {
/*     */     try {
/*  85 */       return this.javaSocket.getSoTimeout();
/*     */     } catch (IOException e) {
/*  87 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setTcpNoDelay(boolean tcpNoDelay)
/*     */   {
/*  93 */     super.setTcpNoDelay(tcpNoDelay);
/*  94 */     return this;
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setSoLinger(int soLinger)
/*     */   {
/*  99 */     super.setSoLinger(soLinger);
/* 100 */     return this;
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setSendBufferSize(int sendBufferSize)
/*     */   {
/* 105 */     super.setSendBufferSize(sendBufferSize);
/* 106 */     return this;
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setReceiveBufferSize(int receiveBufferSize)
/*     */   {
/* 111 */     super.setReceiveBufferSize(receiveBufferSize);
/* 112 */     return this;
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setKeepAlive(boolean keepAlive)
/*     */   {
/* 117 */     super.setKeepAlive(keepAlive);
/* 118 */     return this;
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setTrafficClass(int trafficClass)
/*     */   {
/* 123 */     super.setTrafficClass(trafficClass);
/* 124 */     return this;
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setReuseAddress(boolean reuseAddress)
/*     */   {
/* 129 */     super.setReuseAddress(reuseAddress);
/* 130 */     return this;
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth)
/*     */   {
/* 135 */     super.setPerformancePreferences(connectionTime, latency, bandwidth);
/* 136 */     return this;
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setAllowHalfClosure(boolean allowHalfClosure)
/*     */   {
/* 141 */     super.setAllowHalfClosure(allowHalfClosure);
/* 142 */     return this;
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis)
/*     */   {
/* 147 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 148 */     return this;
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead)
/*     */   {
/* 153 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 154 */     return this;
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setWriteSpinCount(int writeSpinCount)
/*     */   {
/* 159 */     super.setWriteSpinCount(writeSpinCount);
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setAllocator(ByteBufAllocator allocator)
/*     */   {
/* 165 */     super.setAllocator(allocator);
/* 166 */     return this;
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator)
/*     */   {
/* 171 */     super.setRecvByteBufAllocator(allocator);
/* 172 */     return this;
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setAutoRead(boolean autoRead)
/*     */   {
/* 177 */     super.setAutoRead(autoRead);
/* 178 */     return this;
/*     */   }
/*     */   
/*     */   protected void autoReadCleared()
/*     */   {
/* 183 */     if ((this.channel instanceof OioSocketChannel)) {
/* 184 */       ((OioSocketChannel)this.channel).setReadPending(false);
/*     */     }
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark)
/*     */   {
/* 190 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 191 */     return this;
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark)
/*     */   {
/* 196 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 197 */     return this;
/*     */   }
/*     */   
/*     */   public OioSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator)
/*     */   {
/* 202 */     super.setMessageSizeEstimator(estimator);
/* 203 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\socket\oio\DefaultOioSocketChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */