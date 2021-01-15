/*     */ package com.jagrosh.discordipc.entities;
/*     */ 
/*     */ import java.util.function.Consumer;
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
/*     */ public class Callback
/*     */ {
/*     */   private final Runnable success;
/*     */   private final Consumer<String> failure;
/*     */   
/*     */   public Callback()
/*     */   {
/*  38 */     this(null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Callback(Runnable success)
/*     */   {
/*  50 */     this(success, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Callback(Consumer<String> failure)
/*     */   {
/*  62 */     this(null, failure);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Callback(Runnable success, Consumer<String> failure)
/*     */   {
/*  75 */     this.success = success;
/*  76 */     this.failure = failure;
/*     */   }
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
/*     */   public boolean isEmpty()
/*     */   {
/*  91 */     return (this.success == null) && (this.failure == null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void succeed()
/*     */   {
/*  99 */     if (this.success != null) {
/* 100 */       this.success.run();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fail(String message)
/*     */   {
/* 111 */     if (this.failure != null) {
/* 112 */       this.failure.accept(message);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\com\jagrosh\discordipc\entities\Callback.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */