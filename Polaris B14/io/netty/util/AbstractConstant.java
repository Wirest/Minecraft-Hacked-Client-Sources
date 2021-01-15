/*     */ package io.netty.util;
/*     */ 
/*     */ import io.netty.util.internal.PlatformDependent;
/*     */ import io.netty.util.internal.ThreadLocalRandom;
/*     */ import java.nio.ByteBuffer;
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
/*     */ public abstract class AbstractConstant<T extends AbstractConstant<T>>
/*     */   implements Constant<T>
/*     */ {
/*     */   private final int id;
/*     */   private final String name;
/*     */   private volatile long uniquifier;
/*     */   private ByteBuffer directBuffer;
/*     */   
/*     */   protected AbstractConstant(int id, String name)
/*     */   {
/*  37 */     this.id = id;
/*  38 */     this.name = name;
/*     */   }
/*     */   
/*     */   public final String name()
/*     */   {
/*  43 */     return this.name;
/*     */   }
/*     */   
/*     */   public final int id()
/*     */   {
/*  48 */     return this.id;
/*     */   }
/*     */   
/*     */   public final String toString()
/*     */   {
/*  53 */     return name();
/*     */   }
/*     */   
/*     */   public final int hashCode()
/*     */   {
/*  58 */     return super.hashCode();
/*     */   }
/*     */   
/*     */   public final boolean equals(Object obj)
/*     */   {
/*  63 */     return super.equals(obj);
/*     */   }
/*     */   
/*     */   public final int compareTo(T o)
/*     */   {
/*  68 */     if (this == o) {
/*  69 */       return 0;
/*     */     }
/*     */     
/*     */ 
/*  73 */     AbstractConstant<T> other = o;
/*     */     
/*     */ 
/*  76 */     int returnCode = hashCode() - other.hashCode();
/*  77 */     if (returnCode != 0) {
/*  78 */       return returnCode;
/*     */     }
/*     */     
/*  81 */     long thisUV = uniquifier();
/*  82 */     long otherUV = other.uniquifier();
/*  83 */     if (thisUV < otherUV) {
/*  84 */       return -1;
/*     */     }
/*  86 */     if (thisUV > otherUV) {
/*  87 */       return 1;
/*     */     }
/*     */     
/*  90 */     throw new Error("failed to compare two different constants");
/*     */   }
/*     */   
/*     */   private long uniquifier() {
/*     */     long uniquifier;
/*  95 */     if ((uniquifier = this.uniquifier) == 0L) {
/*  96 */       synchronized (this) {
/*  97 */         while ((uniquifier = this.uniquifier) == 0L) {
/*  98 */           if (PlatformDependent.hasUnsafe()) {
/*  99 */             this.directBuffer = ByteBuffer.allocateDirect(1);
/* 100 */             this.uniquifier = PlatformDependent.directBufferAddress(this.directBuffer);
/*     */           } else {
/* 102 */             this.directBuffer = null;
/* 103 */             this.uniquifier = ThreadLocalRandom.current().nextLong();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 109 */     return uniquifier;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\AbstractConstant.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */