/*     */ package io.netty.channel.epoll;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class EpollEventArray
/*     */ {
/*  40 */   private static final int EPOLL_EVENT_SIZE = ;
/*     */   
/*  42 */   private static final int EPOLL_DATA_OFFSET = Native.offsetofEpollData();
/*     */   private long memoryAddress;
/*     */   private int length;
/*     */   
/*     */   EpollEventArray(int length)
/*     */   {
/*  48 */     if (length < 1) {
/*  49 */       throw new IllegalArgumentException("length must be >= 1 but was " + length);
/*     */     }
/*  51 */     this.length = length;
/*  52 */     this.memoryAddress = allocate(length);
/*     */   }
/*     */   
/*     */   private static long allocate(int length) {
/*  56 */     return PlatformDependent.allocateMemory(length * EPOLL_EVENT_SIZE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   long memoryAddress()
/*     */   {
/*  63 */     return this.memoryAddress;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   int length()
/*     */   {
/*  71 */     return this.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void increase()
/*     */   {
/*  79 */     this.length <<= 1;
/*  80 */     free();
/*  81 */     this.memoryAddress = allocate(this.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void free()
/*     */   {
/*  88 */     PlatformDependent.freeMemory(this.memoryAddress);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   int events(int index)
/*     */   {
/*  95 */     return PlatformDependent.getInt(this.memoryAddress + index * EPOLL_EVENT_SIZE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   int fd(int index)
/*     */   {
/* 102 */     return PlatformDependent.getInt(this.memoryAddress + index * EPOLL_EVENT_SIZE + EPOLL_DATA_OFFSET);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\epoll\EpollEventArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */