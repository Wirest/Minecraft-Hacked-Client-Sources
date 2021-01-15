/*     */ package io.netty.channel.sctp;
/*     */ 
/*     */ import com.sun.nio.sctp.SctpStandardSocketOptions;
/*     */ import com.sun.nio.sctp.SctpStandardSocketOptions.InitMaxStreams;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.DefaultChannelConfig;
/*     */ import io.netty.channel.MessageSizeEstimator;
/*     */ import io.netty.channel.RecvByteBufAllocator;
/*     */ import io.netty.util.internal.PlatformDependent;
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
/*     */ public class DefaultSctpChannelConfig
/*     */   extends DefaultChannelConfig
/*     */   implements SctpChannelConfig
/*     */ {
/*     */   private final com.sun.nio.sctp.SctpChannel javaChannel;
/*     */   
/*     */   public DefaultSctpChannelConfig(SctpChannel channel, com.sun.nio.sctp.SctpChannel javaChannel)
/*     */   {
/*  43 */     super(channel);
/*  44 */     if (javaChannel == null) {
/*  45 */       throw new NullPointerException("javaChannel");
/*     */     }
/*  47 */     this.javaChannel = javaChannel;
/*     */     
/*     */ 
/*  50 */     if (PlatformDependent.canEnableTcpNoDelayByDefault()) {
/*     */       try {
/*  52 */         setSctpNoDelay(true);
/*     */       }
/*     */       catch (Exception e) {}
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<ChannelOption<?>, Object> getOptions()
/*     */   {
/*  61 */     return getOptions(super.getOptions(), new ChannelOption[] { ChannelOption.SO_RCVBUF, ChannelOption.SO_SNDBUF, SctpChannelOption.SCTP_NODELAY, SctpChannelOption.SCTP_INIT_MAXSTREAMS });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T> T getOption(ChannelOption<T> option)
/*     */   {
/*  69 */     if (option == ChannelOption.SO_RCVBUF) {
/*  70 */       return Integer.valueOf(getReceiveBufferSize());
/*     */     }
/*  72 */     if (option == ChannelOption.SO_SNDBUF) {
/*  73 */       return Integer.valueOf(getSendBufferSize());
/*     */     }
/*  75 */     if (option == SctpChannelOption.SCTP_NODELAY) {
/*  76 */       return Boolean.valueOf(isSctpNoDelay());
/*     */     }
/*  78 */     return (T)super.getOption(option);
/*     */   }
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */   {
/*  83 */     validate(option, value);
/*     */     
/*  85 */     if (option == ChannelOption.SO_RCVBUF) {
/*  86 */       setReceiveBufferSize(((Integer)value).intValue());
/*  87 */     } else if (option == ChannelOption.SO_SNDBUF) {
/*  88 */       setSendBufferSize(((Integer)value).intValue());
/*  89 */     } else if (option == SctpChannelOption.SCTP_NODELAY) {
/*  90 */       setSctpNoDelay(((Boolean)value).booleanValue());
/*  91 */     } else if (option == SctpChannelOption.SCTP_INIT_MAXSTREAMS) {
/*  92 */       setInitMaxStreams((SctpStandardSocketOptions.InitMaxStreams)value);
/*     */     } else {
/*  94 */       return super.setOption(option, value);
/*     */     }
/*     */     
/*  97 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isSctpNoDelay()
/*     */   {
/*     */     try {
/* 103 */       return ((Boolean)this.javaChannel.getOption(SctpStandardSocketOptions.SCTP_NODELAY)).booleanValue();
/*     */     } catch (IOException e) {
/* 105 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public SctpChannelConfig setSctpNoDelay(boolean sctpNoDelay)
/*     */   {
/*     */     try {
/* 112 */       this.javaChannel.setOption(SctpStandardSocketOptions.SCTP_NODELAY, Boolean.valueOf(sctpNoDelay));
/*     */     } catch (IOException e) {
/* 114 */       throw new ChannelException(e);
/*     */     }
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public int getSendBufferSize()
/*     */   {
/*     */     try {
/* 122 */       return ((Integer)this.javaChannel.getOption(SctpStandardSocketOptions.SO_SNDBUF)).intValue();
/*     */     } catch (IOException e) {
/* 124 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public SctpChannelConfig setSendBufferSize(int sendBufferSize)
/*     */   {
/*     */     try {
/* 131 */       this.javaChannel.setOption(SctpStandardSocketOptions.SO_SNDBUF, Integer.valueOf(sendBufferSize));
/*     */     } catch (IOException e) {
/* 133 */       throw new ChannelException(e);
/*     */     }
/* 135 */     return this;
/*     */   }
/*     */   
/*     */   public int getReceiveBufferSize()
/*     */   {
/*     */     try {
/* 141 */       return ((Integer)this.javaChannel.getOption(SctpStandardSocketOptions.SO_RCVBUF)).intValue();
/*     */     } catch (IOException e) {
/* 143 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public SctpChannelConfig setReceiveBufferSize(int receiveBufferSize)
/*     */   {
/*     */     try {
/* 150 */       this.javaChannel.setOption(SctpStandardSocketOptions.SO_RCVBUF, Integer.valueOf(receiveBufferSize));
/*     */     } catch (IOException e) {
/* 152 */       throw new ChannelException(e);
/*     */     }
/* 154 */     return this;
/*     */   }
/*     */   
/*     */   public SctpStandardSocketOptions.InitMaxStreams getInitMaxStreams()
/*     */   {
/*     */     try {
/* 160 */       return (SctpStandardSocketOptions.InitMaxStreams)this.javaChannel.getOption(SctpStandardSocketOptions.SCTP_INIT_MAXSTREAMS);
/*     */     } catch (IOException e) {
/* 162 */       throw new ChannelException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public SctpChannelConfig setInitMaxStreams(SctpStandardSocketOptions.InitMaxStreams initMaxStreams)
/*     */   {
/*     */     try {
/* 169 */       this.javaChannel.setOption(SctpStandardSocketOptions.SCTP_INIT_MAXSTREAMS, initMaxStreams);
/*     */     } catch (IOException e) {
/* 171 */       throw new ChannelException(e);
/*     */     }
/* 173 */     return this;
/*     */   }
/*     */   
/*     */   public SctpChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis)
/*     */   {
/* 178 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 179 */     return this;
/*     */   }
/*     */   
/*     */   public SctpChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead)
/*     */   {
/* 184 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 185 */     return this;
/*     */   }
/*     */   
/*     */   public SctpChannelConfig setWriteSpinCount(int writeSpinCount)
/*     */   {
/* 190 */     super.setWriteSpinCount(writeSpinCount);
/* 191 */     return this;
/*     */   }
/*     */   
/*     */   public SctpChannelConfig setAllocator(ByteBufAllocator allocator)
/*     */   {
/* 196 */     super.setAllocator(allocator);
/* 197 */     return this;
/*     */   }
/*     */   
/*     */   public SctpChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator)
/*     */   {
/* 202 */     super.setRecvByteBufAllocator(allocator);
/* 203 */     return this;
/*     */   }
/*     */   
/*     */   public SctpChannelConfig setAutoRead(boolean autoRead)
/*     */   {
/* 208 */     super.setAutoRead(autoRead);
/* 209 */     return this;
/*     */   }
/*     */   
/*     */   public SctpChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark)
/*     */   {
/* 214 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 215 */     return this;
/*     */   }
/*     */   
/*     */   public SctpChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark)
/*     */   {
/* 220 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 221 */     return this;
/*     */   }
/*     */   
/*     */   public SctpChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator)
/*     */   {
/* 226 */     super.setMessageSizeEstimator(estimator);
/* 227 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\sctp\DefaultSctpChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */