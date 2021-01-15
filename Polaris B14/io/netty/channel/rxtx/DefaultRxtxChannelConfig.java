/*     */ package io.netty.channel.rxtx;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class DefaultRxtxChannelConfig
/*     */   extends DefaultChannelConfig
/*     */   implements RxtxChannelConfig
/*     */ {
/*  33 */   private volatile int baudrate = 115200;
/*     */   private volatile boolean dtr;
/*     */   private volatile boolean rts;
/*  36 */   private volatile RxtxChannelConfig.Stopbits stopbits = RxtxChannelConfig.Stopbits.STOPBITS_1;
/*  37 */   private volatile RxtxChannelConfig.Databits databits = RxtxChannelConfig.Databits.DATABITS_8;
/*  38 */   private volatile RxtxChannelConfig.Paritybit paritybit = RxtxChannelConfig.Paritybit.NONE;
/*     */   private volatile int waitTime;
/*  40 */   private volatile int readTimeout = 1000;
/*     */   
/*     */   DefaultRxtxChannelConfig(RxtxChannel channel) {
/*  43 */     super(channel);
/*     */   }
/*     */   
/*     */   public Map<ChannelOption<?>, Object> getOptions()
/*     */   {
/*  48 */     return getOptions(super.getOptions(), new ChannelOption[] { RxtxChannelOption.BAUD_RATE, RxtxChannelOption.DTR, RxtxChannelOption.RTS, RxtxChannelOption.STOP_BITS, RxtxChannelOption.DATA_BITS, RxtxChannelOption.PARITY_BIT, RxtxChannelOption.WAIT_TIME });
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> T getOption(ChannelOption<T> option)
/*     */   {
/*  54 */     if (option == RxtxChannelOption.BAUD_RATE) {
/*  55 */       return Integer.valueOf(getBaudrate());
/*     */     }
/*  57 */     if (option == RxtxChannelOption.DTR) {
/*  58 */       return Boolean.valueOf(isDtr());
/*     */     }
/*  60 */     if (option == RxtxChannelOption.RTS) {
/*  61 */       return Boolean.valueOf(isRts());
/*     */     }
/*  63 */     if (option == RxtxChannelOption.STOP_BITS) {
/*  64 */       return getStopbits();
/*     */     }
/*  66 */     if (option == RxtxChannelOption.DATA_BITS) {
/*  67 */       return getDatabits();
/*     */     }
/*  69 */     if (option == RxtxChannelOption.PARITY_BIT) {
/*  70 */       return getParitybit();
/*     */     }
/*  72 */     if (option == RxtxChannelOption.WAIT_TIME) {
/*  73 */       return Integer.valueOf(getWaitTimeMillis());
/*     */     }
/*  75 */     if (option == RxtxChannelOption.READ_TIMEOUT) {
/*  76 */       return Integer.valueOf(getReadTimeout());
/*     */     }
/*  78 */     return (T)super.getOption(option);
/*     */   }
/*     */   
/*     */   public <T> boolean setOption(ChannelOption<T> option, T value)
/*     */   {
/*  83 */     validate(option, value);
/*     */     
/*  85 */     if (option == RxtxChannelOption.BAUD_RATE) {
/*  86 */       setBaudrate(((Integer)value).intValue());
/*  87 */     } else if (option == RxtxChannelOption.DTR) {
/*  88 */       setDtr(((Boolean)value).booleanValue());
/*  89 */     } else if (option == RxtxChannelOption.RTS) {
/*  90 */       setRts(((Boolean)value).booleanValue());
/*  91 */     } else if (option == RxtxChannelOption.STOP_BITS) {
/*  92 */       setStopbits((RxtxChannelConfig.Stopbits)value);
/*  93 */     } else if (option == RxtxChannelOption.DATA_BITS) {
/*  94 */       setDatabits((RxtxChannelConfig.Databits)value);
/*  95 */     } else if (option == RxtxChannelOption.PARITY_BIT) {
/*  96 */       setParitybit((RxtxChannelConfig.Paritybit)value);
/*  97 */     } else if (option == RxtxChannelOption.WAIT_TIME) {
/*  98 */       setWaitTimeMillis(((Integer)value).intValue());
/*  99 */     } else if (option == RxtxChannelOption.READ_TIMEOUT) {
/* 100 */       setReadTimeout(((Integer)value).intValue());
/*     */     } else {
/* 102 */       return super.setOption(option, value);
/*     */     }
/* 104 */     return true;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig setBaudrate(int baudrate)
/*     */   {
/* 109 */     this.baudrate = baudrate;
/* 110 */     return this;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig setStopbits(RxtxChannelConfig.Stopbits stopbits)
/*     */   {
/* 115 */     this.stopbits = stopbits;
/* 116 */     return this;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig setDatabits(RxtxChannelConfig.Databits databits)
/*     */   {
/* 121 */     this.databits = databits;
/* 122 */     return this;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig setParitybit(RxtxChannelConfig.Paritybit paritybit)
/*     */   {
/* 127 */     this.paritybit = paritybit;
/* 128 */     return this;
/*     */   }
/*     */   
/*     */   public int getBaudrate()
/*     */   {
/* 133 */     return this.baudrate;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig.Stopbits getStopbits()
/*     */   {
/* 138 */     return this.stopbits;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig.Databits getDatabits()
/*     */   {
/* 143 */     return this.databits;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig.Paritybit getParitybit()
/*     */   {
/* 148 */     return this.paritybit;
/*     */   }
/*     */   
/*     */   public boolean isDtr()
/*     */   {
/* 153 */     return this.dtr;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig setDtr(boolean dtr)
/*     */   {
/* 158 */     this.dtr = dtr;
/* 159 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isRts()
/*     */   {
/* 164 */     return this.rts;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig setRts(boolean rts)
/*     */   {
/* 169 */     this.rts = rts;
/* 170 */     return this;
/*     */   }
/*     */   
/*     */   public int getWaitTimeMillis()
/*     */   {
/* 175 */     return this.waitTime;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig setWaitTimeMillis(int waitTimeMillis)
/*     */   {
/* 180 */     if (waitTimeMillis < 0) {
/* 181 */       throw new IllegalArgumentException("Wait time must be >= 0");
/*     */     }
/* 183 */     this.waitTime = waitTimeMillis;
/* 184 */     return this;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig setReadTimeout(int readTimeout)
/*     */   {
/* 189 */     if (readTimeout < 0) {
/* 190 */       throw new IllegalArgumentException("readTime must be >= 0");
/*     */     }
/* 192 */     this.readTimeout = readTimeout;
/* 193 */     return this;
/*     */   }
/*     */   
/*     */   public int getReadTimeout()
/*     */   {
/* 198 */     return this.readTimeout;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis)
/*     */   {
/* 203 */     super.setConnectTimeoutMillis(connectTimeoutMillis);
/* 204 */     return this;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead)
/*     */   {
/* 209 */     super.setMaxMessagesPerRead(maxMessagesPerRead);
/* 210 */     return this;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig setWriteSpinCount(int writeSpinCount)
/*     */   {
/* 215 */     super.setWriteSpinCount(writeSpinCount);
/* 216 */     return this;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig setAllocator(ByteBufAllocator allocator)
/*     */   {
/* 221 */     super.setAllocator(allocator);
/* 222 */     return this;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator)
/*     */   {
/* 227 */     super.setRecvByteBufAllocator(allocator);
/* 228 */     return this;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig setAutoRead(boolean autoRead)
/*     */   {
/* 233 */     super.setAutoRead(autoRead);
/* 234 */     return this;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark)
/*     */   {
/* 239 */     super.setWriteBufferHighWaterMark(writeBufferHighWaterMark);
/* 240 */     return this;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark)
/*     */   {
/* 245 */     super.setWriteBufferLowWaterMark(writeBufferLowWaterMark);
/* 246 */     return this;
/*     */   }
/*     */   
/*     */   public RxtxChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator)
/*     */   {
/* 251 */     super.setMessageSizeEstimator(estimator);
/* 252 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\rxtx\DefaultRxtxChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */