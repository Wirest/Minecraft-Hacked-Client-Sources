/*     */ package io.netty.channel.udt;
/*     */ 
/*     */ import com.barchart.udt.OptionUDT;
/*     */ import com.barchart.udt.SocketUDT;
/*     */ import com.barchart.udt.nio.ChannelUDT;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.DefaultChannelConfig;
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
/*     */ 
/*     */ 
/*     */ public class DefaultUdtChannelConfig
/*     */   extends DefaultChannelConfig
/*     */   implements UdtChannelConfig
/*     */ {
/*     */   private static final int K = 1024;
/*     */   private static final int M = 1048576;
/*  42 */   private volatile int protocolReceiveBufferSize = 10485760;
/*  43 */   private volatile int protocolSendBufferSize = 10485760;
/*     */   
/*  45 */   private volatile int systemReceiveBufferSize = 1048576;
/*  46 */   private volatile int systemSendBufferSize = 1048576;
/*     */   
/*  48 */   private volatile int allocatorReceiveBufferSize = 131072;
/*  49 */   private volatile int allocatorSendBufferSize = 131072;
/*     */   
/*     */   private volatile int soLinger;
/*     */   
/*  53 */   private volatile boolean reuseAddress = true;
/*     */   
/*     */   public DefaultUdtChannelConfig(UdtChannel channel, ChannelUDT channelUDT, boolean apply)
/*     */     throws IOException
/*     */   {
/*  58 */     super(channel);
/*  59 */     if (apply) {
/*  60 */       apply(channelUDT);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void apply(ChannelUDT channelUDT) throws IOException {
/*  65 */     SocketUDT socketUDT = channelUDT.socketUDT();
/*  66 */     socketUDT.setReuseAddress(isReuseAddress());
/*  67 */     socketUDT.setSendBufferSize(getSendBufferSize());
/*  68 */     if (getSoLinger() <= 0) {
/*  69 */       socketUDT.setSoLinger(false, 0);
/*     */     } else {
/*  71 */       socketUDT.setSoLinger(true, getSoLinger());
/*     */     }
/*  73 */     socketUDT.setOption(OptionUDT.Protocol_Receive_Buffer_Size, Integer.valueOf(getProtocolReceiveBufferSize()));
/*     */     
/*  75 */     socketUDT.setOption(OptionUDT.Protocol_Send_Buffer_Size, Integer.valueOf(getProtocolSendBufferSize()));
/*     */     
/*  77 */     socketUDT.setOption(OptionUDT.System_Receive_Buffer_Size, Integer.valueOf(getSystemReceiveBufferSize()));
/*     */     
/*  79 */     socketUDT.setOption(OptionUDT.System_Send_Buffer_Size, Integer.valueOf(getSystemSendBufferSize()));
/*     */   }
/*     */   
/*     */ 
/*     */   public int getProtocolReceiveBufferSize()
/*     */   {
/*  85 */     return this.protocolReceiveBufferSize;
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T getOption(ChannelOption<T> option)
/*     */   {
/*  91 */     if (option == UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE) {
/*  92 */       return Integer.valueOf(getProtocolReceiveBufferSize());
/*     */     }
/*  94 */     if (option == UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE) {
/*  95 */       return Integer.valueOf(getProtocolSendBufferSize());
/*     */     }
/*  97 */     if (option == UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE) {
/*  98 */       return Integer.valueOf(getSystemReceiveBufferSize());
/*     */     }
/* 100 */     if (option == UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE) {
/* 101 */       return Integer.valueOf(getSystemSendBufferSize());
/*     */     }
/* 103 */     if (option == ChannelOption.SO_RCVBUF) {
/* 104 */       return Integer.valueOf(getReceiveBufferSize());
/*     */     }
/* 106 */     if (option == ChannelOption.SO_SNDBUF) {
/* 107 */       return Integer.valueOf(getSendBufferSize());
/*     */     }
/* 109 */     if (option == ChannelOption.SO_REUSEADDR) {
/* 110 */       return Boolean.valueOf(isReuseAddress());
/*     */     }
/* 112 */     if (option == ChannelOption.SO_LINGER) {
/* 113 */       return Integer.valueOf(getSoLinger());
/*     */     }
/* 115 */     return (T)super.getOption(option);
/*     */   }
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions()
/*     */   {
/* 120 */     return getOptions(super.getOptions(), new ChannelOption[] { UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE, UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE, UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE, UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE, ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, ChannelOption.SO_REUSEADDR, ChannelOption.SO_LINGER });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getReceiveBufferSize()
/*     */   {
/* 128 */     return this.allocatorReceiveBufferSize;
/*     */   }
/*     */   
/*     */   public int getSendBufferSize()
/*     */   {
/* 133 */     return this.allocatorSendBufferSize;
/*     */   }
/*     */   
/*     */   public int getSoLinger()
/*     */   {
/* 138 */     return this.soLinger;
/*     */   }
/*     */   
/*     */   public boolean isReuseAddress()
/*     */   {
/* 143 */     return this.reuseAddress;
/*     */   }
/*     */   
/*     */   public UdtChannelConfig setProtocolReceiveBufferSize(int protocolReceiveBufferSize)
/*     */   {
/* 148 */     this.protocolReceiveBufferSize = protocolReceiveBufferSize;
/* 149 */     return this;
/*     */   }
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */   {
/* 154 */     validate(option, value);
/* 155 */     if (option == UdtChannelOption.PROTOCOL_RECEIVE_BUFFER_SIZE) {
/* 156 */       setProtocolReceiveBufferSize(((Integer)value).intValue());
/* 157 */     } else if (option == UdtChannelOption.PROTOCOL_SEND_BUFFER_SIZE) {
/* 158 */       setProtocolSendBufferSize(((Integer)value).intValue());
/* 159 */     } else if (option == UdtChannelOption.SYSTEM_RECEIVE_BUFFER_SIZE) {
/* 160 */       setSystemReceiveBufferSize(((Integer)value).intValue());
/* 161 */     } else if (option == UdtChannelOption.SYSTEM_SEND_BUFFER_SIZE) {
/* 162 */       setSystemSendBufferSize(((Integer)value).intValue());
/* 163 */     } else if (option == ChannelOption.SO_RCVBUF) {
/* 164 */       setReceiveBufferSize(((Integer)value).intValue());
/* 165 */     } else if (option == ChannelOption.SO_SNDBUF) {
/* 166 */       setSendBufferSize(((Integer)value).intValue());
/* 167 */     } else if (option == ChannelOption.SO_REUSEADDR) {
/* 168 */       setReuseAddress(((Boolean)value).booleanValue());
/* 169 */     } else if (option == ChannelOption.SO_LINGER) {
/* 170 */       setSoLinger(((Integer)value).intValue());
/*     */     } else {
/* 172 */       return super.setOption(option, value);
/*     */     }
/* 174 */     return true;
/*     */   }
/*     */   
/*     */   public UdtChannelConfig setReceiveBufferSize(int receiveBufferSize)
/*     */   {
/* 179 */     this.allocatorReceiveBufferSize = receiveBufferSize;
/* 180 */     return this;
/*     */   }
/*     */   
/*     */   public UdtChannelConfig setReuseAddress(boolean reuseAddress)
/*     */   {
/* 185 */     this.reuseAddress = reuseAddress;
/* 186 */     return this;
/*     */   }
/*     */   
/*     */   public UdtChannelConfig setSendBufferSize(int sendBufferSize)
/*     */   {
/* 191 */     this.allocatorSendBufferSize = sendBufferSize;
/* 192 */     return this;
/*     */   }
/*     */   
/*     */   public UdtChannelConfig setSoLinger(int soLinger)
/*     */   {
/* 197 */     this.soLinger = soLinger;
/* 198 */     return this;
/*     */   }
/*     */   
/*     */   public int getSystemReceiveBufferSize()
/*     */   {
/* 203 */     return this.systemReceiveBufferSize;
/*     */   }
/*     */   
/*     */ 
/*     */   public UdtChannelConfig setSystemSendBufferSize(int systemReceiveBufferSize)
/*     */   {
/* 209 */     this.systemReceiveBufferSize = systemReceiveBufferSize;
/* 210 */     return this;
/*     */   }
/*     */   
/*     */   public int getProtocolSendBufferSize()
/*     */   {
/* 215 */     return this.protocolSendBufferSize;
/*     */   }
/*     */   
/*     */ 
/*     */   public UdtChannelConfig setProtocolSendBufferSize(int protocolSendBufferSize)
/*     */   {
/* 221 */     this.protocolSendBufferSize = protocolSendBufferSize;
/* 222 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public UdtChannelConfig setSystemReceiveBufferSize(int systemSendBufferSize)
/*     */   {
/* 228 */     this.systemSendBufferSize = systemSendBufferSize;
/* 229 */     return this;
/*     */   }
/*     */   
/*     */   public int getSystemSendBufferSize()
/*     */   {
/* 234 */     return this.systemSendBufferSize;
/*     */   }
/*     */   
/*     */   public UdtChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis)
/*     */   {
/* 239 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 240 */     return this;
/*     */   }
/*     */   
/*     */   public UdtChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead)
/*     */   {
/* 245 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 246 */     return this;
/*     */   }
/*     */   
/*     */   public UdtChannelConfig setWriteSpinCount(int writeSpinCount)
/*     */   {
/* 251 */     super.setWriteSpinCount(writeSpinCount);
/* 252 */     return this;
/*     */   }
/*     */   
/*     */   public UdtChannelConfig setAllocator(ByteBufAllocator allocator)
/*     */   {
/* 257 */     super.setAllocator(allocator);
/* 258 */     return this;
/*     */   }
/*     */   
/*     */   public UdtChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator)
/*     */   {
/* 263 */     super.setRecvByteBufAllocator(allocator);
/* 264 */     return this;
/*     */   }
/*     */   
/*     */   public UdtChannelConfig setAutoRead(boolean autoRead)
/*     */   {
/* 269 */     super.setAutoRead(autoRead);
/* 270 */     return this;
/*     */   }
/*     */   
/*     */   public UdtChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark)
/*     */   {
/* 275 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 276 */     return this;
/*     */   }
/*     */   
/*     */   public UdtChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark)
/*     */   {
/* 281 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 282 */     return this;
/*     */   }
/*     */   
/*     */   public UdtChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator)
/*     */   {
/* 287 */     super.setMessageSizeEstimator(estimator);
/* 288 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\udt\DefaultUdtChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */