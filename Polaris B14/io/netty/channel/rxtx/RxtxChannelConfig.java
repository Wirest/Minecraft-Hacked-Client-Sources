/*     */ package io.netty.channel.rxtx;
/*     */ 
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ 
/*     */ public abstract interface RxtxChannelConfig extends io.netty.channel.ChannelConfig {
/*     */   public abstract RxtxChannelConfig setBaudrate(int paramInt);
/*     */   
/*     */   public abstract RxtxChannelConfig setStopbits(Stopbits paramStopbits);
/*     */   
/*     */   public abstract RxtxChannelConfig setDatabits(Databits paramDatabits);
/*     */   
/*     */   public abstract RxtxChannelConfig setParitybit(Paritybit paramParitybit);
/*     */   
/*     */   public abstract int getBaudrate();
/*     */   
/*     */   public abstract Stopbits getStopbits();
/*     */   
/*     */   public abstract Databits getDatabits();
/*     */   
/*     */   public abstract Paritybit getParitybit();
/*     */   
/*     */   public abstract boolean isDtr();
/*     */   
/*     */   public abstract RxtxChannelConfig setDtr(boolean paramBoolean);
/*     */   
/*     */   public abstract boolean isRts();
/*     */   
/*     */   public abstract RxtxChannelConfig setRts(boolean paramBoolean);
/*     */   
/*     */   public abstract int getWaitTimeMillis();
/*     */   
/*     */   public abstract RxtxChannelConfig setWaitTimeMillis(int paramInt);
/*     */   
/*     */   public abstract RxtxChannelConfig setReadTimeout(int paramInt);
/*     */   
/*     */   public abstract int getReadTimeout();
/*     */   
/*     */   public abstract RxtxChannelConfig setConnectTimeoutMillis(int paramInt);
/*     */   
/*     */   public abstract RxtxChannelConfig setMaxMessagesPerRead(int paramInt);
/*     */   
/*     */   public abstract RxtxChannelConfig setWriteSpinCount(int paramInt);
/*     */   
/*     */   public abstract RxtxChannelConfig setAllocator(ByteBufAllocator paramByteBufAllocator);
/*     */   
/*     */   public abstract RxtxChannelConfig setRecvByteBufAllocator(io.netty.channel.RecvByteBufAllocator paramRecvByteBufAllocator);
/*     */   
/*     */   public abstract RxtxChannelConfig setAutoRead(boolean paramBoolean);
/*     */   
/*     */   public abstract RxtxChannelConfig setWriteBufferHighWaterMark(int paramInt);
/*     */   
/*     */   public abstract RxtxChannelConfig setWriteBufferLowWaterMark(int paramInt);
/*     */   
/*     */   public abstract RxtxChannelConfig setMessageSizeEstimator(io.netty.channel.MessageSizeEstimator paramMessageSizeEstimator);
/*     */   
/*     */   public static enum Stopbits {
/*  57 */     STOPBITS_1(1), 
/*     */     
/*     */ 
/*     */ 
/*  61 */     STOPBITS_2(2), 
/*     */     
/*     */ 
/*     */ 
/*  65 */     STOPBITS_1_5(3);
/*     */     
/*     */     private final int value;
/*     */     
/*     */     private Stopbits(int value) {
/*  70 */       this.value = value;
/*     */     }
/*     */     
/*     */     public int value() {
/*  74 */       return this.value;
/*     */     }
/*     */     
/*     */     public static Stopbits valueOf(int value) {
/*  78 */       for (Stopbits stopbit : ) {
/*  79 */         if (stopbit.value == value) {
/*  80 */           return stopbit;
/*     */         }
/*     */       }
/*  83 */       throw new IllegalArgumentException("unknown " + Stopbits.class.getSimpleName() + " value: " + value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum Databits
/*     */   {
/*  91 */     DATABITS_5(5), 
/*     */     
/*     */ 
/*     */ 
/*  95 */     DATABITS_6(6), 
/*     */     
/*     */ 
/*     */ 
/*  99 */     DATABITS_7(7), 
/*     */     
/*     */ 
/*     */ 
/* 103 */     DATABITS_8(8);
/*     */     
/*     */     private final int value;
/*     */     
/*     */     private Databits(int value) {
/* 108 */       this.value = value;
/*     */     }
/*     */     
/*     */     public int value() {
/* 112 */       return this.value;
/*     */     }
/*     */     
/*     */     public static Databits valueOf(int value) {
/* 116 */       for (Databits databit : ) {
/* 117 */         if (databit.value == value) {
/* 118 */           return databit;
/*     */         }
/*     */       }
/* 121 */       throw new IllegalArgumentException("unknown " + Databits.class.getSimpleName() + " value: " + value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static enum Paritybit
/*     */   {
/* 129 */     NONE(0), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 134 */     ODD(1), 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 139 */     EVEN(2), 
/*     */     
/*     */ 
/*     */ 
/* 143 */     MARK(3), 
/*     */     
/*     */ 
/*     */ 
/* 147 */     SPACE(4);
/*     */     
/*     */     private final int value;
/*     */     
/*     */     private Paritybit(int value) {
/* 152 */       this.value = value;
/*     */     }
/*     */     
/*     */     public int value() {
/* 156 */       return this.value;
/*     */     }
/*     */     
/*     */     public static Paritybit valueOf(int value) {
/* 160 */       for (Paritybit paritybit : ) {
/* 161 */         if (paritybit.value == value) {
/* 162 */           return paritybit;
/*     */         }
/*     */       }
/* 165 */       throw new IllegalArgumentException("unknown " + Paritybit.class.getSimpleName() + " value: " + value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\rxtx\RxtxChannelConfig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */