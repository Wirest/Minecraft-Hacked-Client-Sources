/*     */ package io.netty.channel.udt;
/*     */ 
/*     */ import com.barchart.udt.nio.ChannelUDT;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import java.io.IOException;
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
/*     */ public class DefaultUdtServerChannelConfig
/*     */   extends DefaultUdtChannelConfig
/*     */   implements UdtServerChannelConfig
/*     */ {
/*  35 */   private volatile int backlog = 64;
/*     */   
/*     */   public DefaultUdtServerChannelConfig(UdtChannel channel, ChannelUDT channelUDT, boolean apply) throws IOException
/*     */   {
/*  39 */     super(channel, channelUDT, apply);
/*  40 */     if (apply) {
/*  41 */       apply(channelUDT);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void apply(ChannelUDT channelUDT)
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */   public int getBacklog()
/*     */   {
/*  52 */     return this.backlog;
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T getOption(ChannelOption<T> option)
/*     */   {
/*  58 */     if (option == ChannelOption.SO_BACKLOG) {
/*  59 */       return Integer.valueOf(getBacklog());
/*     */     }
/*  61 */     return (T)super.getOption(option);
/*     */   }
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions()
/*     */   {
/*  66 */     return getOptions(super.getOptions(), new ChannelOption[] { ChannelOption.SO_BACKLOG });
/*     */   }
/*     */   
/*     */   public UdtServerChannelConfig setBacklog(int backlog)
/*     */   {
/*  71 */     this.backlog = backlog;
/*  72 */     return this;
/*     */   }
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */   {
/*  77 */     validate(option, value);
/*  78 */     if (option == ChannelOption.SO_BACKLOG) {
/*  79 */       setBacklog(((Integer)value).intValue());
/*     */     } else {
/*  81 */       return super.setOption(option, value);
/*     */     }
/*  83 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public UdtServerChannelConfig setProtocolReceiveBufferSize(int protocolReceiveBufferSize)
/*     */   {
/*  89 */     super.setProtocolReceiveBufferSize(protocolReceiveBufferSize);
/*  90 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public UdtServerChannelConfig setProtocolSendBufferSize(int protocolSendBufferSize)
/*     */   {
/*  96 */     super.setProtocolSendBufferSize(protocolSendBufferSize);
/*  97 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public UdtServerChannelConfig setReceiveBufferSize(int receiveBufferSize)
/*     */   {
/* 103 */     super.setReceiveBufferSize(receiveBufferSize);
/* 104 */     return this;
/*     */   }
/*     */   
/*     */   public UdtServerChannelConfig setReuseAddress(boolean reuseAddress)
/*     */   {
/* 109 */     super.setReuseAddress(reuseAddress);
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public UdtServerChannelConfig setSendBufferSize(int sendBufferSize)
/*     */   {
/* 115 */     super.setSendBufferSize(sendBufferSize);
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public UdtServerChannelConfig setSoLinger(int soLinger)
/*     */   {
/* 121 */     super.setSoLinger(soLinger);
/* 122 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public UdtServerChannelConfig setSystemReceiveBufferSize(int systemSendBufferSize)
/*     */   {
/* 128 */     super.setSystemReceiveBufferSize(systemSendBufferSize);
/* 129 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public UdtServerChannelConfig setSystemSendBufferSize(int systemReceiveBufferSize)
/*     */   {
/* 135 */     super.setSystemSendBufferSize(systemReceiveBufferSize);
/* 136 */     return this;
/*     */   }
/*     */   
/*     */   public UdtServerChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis)
/*     */   {
/* 141 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 142 */     return this;
/*     */   }
/*     */   
/*     */   public UdtServerChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead)
/*     */   {
/* 147 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 148 */     return this;
/*     */   }
/*     */   
/*     */   public UdtServerChannelConfig setWriteSpinCount(int writeSpinCount)
/*     */   {
/* 153 */     super.setWriteSpinCount(writeSpinCount);
/* 154 */     return this;
/*     */   }
/*     */   
/*     */   public UdtServerChannelConfig setAllocator(ByteBufAllocator allocator)
/*     */   {
/* 159 */     super.setAllocator(allocator);
/* 160 */     return this;
/*     */   }
/*     */   
/*     */   public UdtServerChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator)
/*     */   {
/* 165 */     super.setRecvByteBufAllocator(allocator);
/* 166 */     return this;
/*     */   }
/*     */   
/*     */   public UdtServerChannelConfig setAutoRead(boolean autoRead)
/*     */   {
/* 171 */     super.setAutoRead(autoRead);
/* 172 */     return this;
/*     */   }
/*     */   
/*     */   public UdtServerChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark)
/*     */   {
/* 177 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 178 */     return this;
/*     */   }
/*     */   
/*     */   public UdtServerChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark)
/*     */   {
/* 183 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 184 */     return this;
/*     */   }
/*     */   
/*     */   public UdtServerChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator)
/*     */   {
/* 189 */     super.setMessageSizeEstimator(estimator);
/* 190 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\udt\DefaultUdtServerChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */