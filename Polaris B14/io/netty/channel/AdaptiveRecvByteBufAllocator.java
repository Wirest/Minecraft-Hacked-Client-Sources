/*     */ package io.netty.channel;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AdaptiveRecvByteBufAllocator
/*     */   implements RecvByteBufAllocator
/*     */ {
/*     */   static final int DEFAULT_MINIMUM = 64;
/*     */   static final int DEFAULT_INITIAL = 1024;
/*     */   static final int DEFAULT_MAXIMUM = 65536;
/*     */   private static final int INDEX_INCREMENT = 4;
/*     */   private static final int INDEX_DECREMENT = 1;
/*     */   private static final int[] SIZE_TABLE;
/*     */   
/*     */   static
/*     */   {
/*  46 */     List<Integer> sizeTable = new ArrayList();
/*  47 */     for (int i = 16; i < 512; i += 16) {
/*  48 */       sizeTable.add(Integer.valueOf(i));
/*     */     }
/*     */     
/*  51 */     for (int i = 512; i > 0; i <<= 1) {
/*  52 */       sizeTable.add(Integer.valueOf(i));
/*     */     }
/*     */     
/*  55 */     SIZE_TABLE = new int[sizeTable.size()];
/*  56 */     for (int i = 0; i < SIZE_TABLE.length; i++) {
/*  57 */       SIZE_TABLE[i] = ((Integer)sizeTable.get(i)).intValue();
/*     */     }
/*     */   }
/*     */   
/*  61 */   public static final AdaptiveRecvByteBufAllocator DEFAULT = new AdaptiveRecvByteBufAllocator();
/*     */   private final int minIndex;
/*     */   
/*  64 */   private static int getSizeTableIndex(int size) { int low = 0;int high = SIZE_TABLE.length - 1;
/*  65 */     for (;;) { if (high < low) {
/*  66 */         return low;
/*     */       }
/*  68 */       if (high == low) {
/*  69 */         return high;
/*     */       }
/*     */       
/*  72 */       int mid = low + high >>> 1;
/*  73 */       int a = SIZE_TABLE[mid];
/*  74 */       int b = SIZE_TABLE[(mid + 1)];
/*  75 */       if (size > b) {
/*  76 */         low = mid + 1;
/*  77 */       } else if (size < a) {
/*  78 */         high = mid - 1;
/*  79 */       } else { if (size == a) {
/*  80 */           return mid;
/*     */         }
/*  82 */         return mid + 1;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class HandleImpl implements RecvByteBufAllocator.Handle {
/*     */     private final int minIndex;
/*     */     private final int maxIndex;
/*     */     private int index;
/*     */     private int nextReceiveBufferSize;
/*     */     private boolean decreaseNow;
/*     */     
/*     */     HandleImpl(int minIndex, int maxIndex, int initial) {
/*  95 */       this.minIndex = minIndex;
/*  96 */       this.maxIndex = maxIndex;
/*     */       
/*  98 */       this.index = AdaptiveRecvByteBufAllocator.getSizeTableIndex(initial);
/*  99 */       this.nextReceiveBufferSize = AdaptiveRecvByteBufAllocator.SIZE_TABLE[this.index];
/*     */     }
/*     */     
/*     */     public ByteBuf allocate(ByteBufAllocator alloc)
/*     */     {
/* 104 */       return alloc.ioBuffer(this.nextReceiveBufferSize);
/*     */     }
/*     */     
/*     */     public int guess()
/*     */     {
/* 109 */       return this.nextReceiveBufferSize;
/*     */     }
/*     */     
/*     */     public void record(int actualReadBytes)
/*     */     {
/* 114 */       if (actualReadBytes <= AdaptiveRecvByteBufAllocator.SIZE_TABLE[Math.max(0, this.index - 1 - 1)]) {
/* 115 */         if (this.decreaseNow) {
/* 116 */           this.index = Math.max(this.index - 1, this.minIndex);
/* 117 */           this.nextReceiveBufferSize = AdaptiveRecvByteBufAllocator.SIZE_TABLE[this.index];
/* 118 */           this.decreaseNow = false;
/*     */         } else {
/* 120 */           this.decreaseNow = true;
/*     */         }
/* 122 */       } else if (actualReadBytes >= this.nextReceiveBufferSize) {
/* 123 */         this.index = Math.min(this.index + 4, this.maxIndex);
/* 124 */         this.nextReceiveBufferSize = AdaptiveRecvByteBufAllocator.SIZE_TABLE[this.index];
/* 125 */         this.decreaseNow = false;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private final int maxIndex;
/*     */   
/*     */ 
/*     */   private final int initial;
/*     */   
/*     */ 
/*     */   private AdaptiveRecvByteBufAllocator()
/*     */   {
/* 140 */     this(64, 1024, 65536);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AdaptiveRecvByteBufAllocator(int minimum, int initial, int maximum)
/*     */   {
/* 151 */     if (minimum <= 0) {
/* 152 */       throw new IllegalArgumentException("minimum: " + minimum);
/*     */     }
/* 154 */     if (initial < minimum) {
/* 155 */       throw new IllegalArgumentException("initial: " + initial);
/*     */     }
/* 157 */     if (maximum < initial) {
/* 158 */       throw new IllegalArgumentException("maximum: " + maximum);
/*     */     }
/*     */     
/* 161 */     int minIndex = getSizeTableIndex(minimum);
/* 162 */     if (SIZE_TABLE[minIndex] < minimum) {
/* 163 */       this.minIndex = (minIndex + 1);
/*     */     } else {
/* 165 */       this.minIndex = minIndex;
/*     */     }
/*     */     
/* 168 */     int maxIndex = getSizeTableIndex(maximum);
/* 169 */     if (SIZE_TABLE[maxIndex] > maximum) {
/* 170 */       this.maxIndex = (maxIndex - 1);
/*     */     } else {
/* 172 */       this.maxIndex = maxIndex;
/*     */     }
/*     */     
/* 175 */     this.initial = initial;
/*     */   }
/*     */   
/*     */   public RecvByteBufAllocator.Handle newHandle()
/*     */   {
/* 180 */     return new HandleImpl(this.minIndex, this.maxIndex, this.initial);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\AdaptiveRecvByteBufAllocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */