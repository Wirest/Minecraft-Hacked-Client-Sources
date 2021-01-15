/*     */ package io.netty.handler.codec.memcache.binary;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.Unpooled;
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
/*     */ public class DefaultFullBinaryMemcacheResponse
/*     */   extends DefaultBinaryMemcacheResponse
/*     */   implements FullBinaryMemcacheResponse
/*     */ {
/*     */   private final ByteBuf content;
/*     */   
/*     */   public DefaultFullBinaryMemcacheResponse(String key, ByteBuf extras)
/*     */   {
/*  36 */     this(key, extras, Unpooled.buffer(0));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultFullBinaryMemcacheResponse(String key, ByteBuf extras, ByteBuf content)
/*     */   {
/*  48 */     super(key, extras);
/*  49 */     if (content == null) {
/*  50 */       throw new NullPointerException("Supplied content is null.");
/*     */     }
/*     */     
/*  53 */     this.content = content;
/*     */   }
/*     */   
/*     */   public ByteBuf content()
/*     */   {
/*  58 */     return this.content;
/*     */   }
/*     */   
/*     */   public int refCnt()
/*     */   {
/*  63 */     return this.content.refCnt();
/*     */   }
/*     */   
/*     */   public FullBinaryMemcacheResponse retain()
/*     */   {
/*  68 */     super.retain();
/*  69 */     this.content.retain();
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public FullBinaryMemcacheResponse retain(int increment)
/*     */   {
/*  75 */     super.retain(increment);
/*  76 */     this.content.retain(increment);
/*  77 */     return this;
/*     */   }
/*     */   
/*     */   public FullBinaryMemcacheResponse touch()
/*     */   {
/*  82 */     super.touch();
/*  83 */     this.content.touch();
/*  84 */     return this;
/*     */   }
/*     */   
/*     */   public FullBinaryMemcacheResponse touch(Object hint)
/*     */   {
/*  89 */     super.touch(hint);
/*  90 */     this.content.touch(hint);
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public boolean release()
/*     */   {
/*  96 */     super.release();
/*  97 */     return this.content.release();
/*     */   }
/*     */   
/*     */   public boolean release(int decrement)
/*     */   {
/* 102 */     super.release(decrement);
/* 103 */     return this.content.release(decrement);
/*     */   }
/*     */   
/*     */   public FullBinaryMemcacheResponse copy()
/*     */   {
/* 108 */     ByteBuf extras = extras();
/* 109 */     if (extras != null) {
/* 110 */       extras = extras.copy();
/*     */     }
/* 112 */     return new DefaultFullBinaryMemcacheResponse(key(), extras, content().copy());
/*     */   }
/*     */   
/*     */   public FullBinaryMemcacheResponse duplicate()
/*     */   {
/* 117 */     ByteBuf extras = extras();
/* 118 */     if (extras != null) {
/* 119 */       extras = extras.duplicate();
/*     */     }
/* 121 */     return new DefaultFullBinaryMemcacheResponse(key(), extras, content().duplicate());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\binary\DefaultFullBinaryMemcacheResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */