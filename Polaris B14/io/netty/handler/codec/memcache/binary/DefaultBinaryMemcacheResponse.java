/*     */ package io.netty.handler.codec.memcache.binary;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
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
/*     */ public class DefaultBinaryMemcacheResponse
/*     */   extends AbstractBinaryMemcacheMessage
/*     */   implements BinaryMemcacheResponse
/*     */ {
/*     */   public static final byte RESPONSE_MAGIC_BYTE = -127;
/*     */   private short status;
/*     */   
/*     */   public DefaultBinaryMemcacheResponse()
/*     */   {
/*  36 */     this(null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultBinaryMemcacheResponse(String key)
/*     */   {
/*  45 */     this(key, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultBinaryMemcacheResponse(ByteBuf extras)
/*     */   {
/*  54 */     this(null, extras);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultBinaryMemcacheResponse(String key, ByteBuf extras)
/*     */   {
/*  64 */     super(key, extras);
/*  65 */     setMagic((byte)-127);
/*     */   }
/*     */   
/*     */   public short status()
/*     */   {
/*  70 */     return this.status;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheResponse setStatus(short status)
/*     */   {
/*  75 */     this.status = status;
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheResponse retain()
/*     */   {
/*  81 */     super.retain();
/*  82 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheResponse retain(int increment)
/*     */   {
/*  87 */     super.retain(increment);
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheResponse touch()
/*     */   {
/*  93 */     super.touch();
/*  94 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheResponse touch(Object hint)
/*     */   {
/*  99 */     super.touch(hint);
/* 100 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\binary\DefaultBinaryMemcacheResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */