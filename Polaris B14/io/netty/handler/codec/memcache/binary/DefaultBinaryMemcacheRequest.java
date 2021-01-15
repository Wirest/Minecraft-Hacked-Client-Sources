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
/*     */ public class DefaultBinaryMemcacheRequest
/*     */   extends AbstractBinaryMemcacheMessage
/*     */   implements BinaryMemcacheRequest
/*     */ {
/*     */   public static final byte REQUEST_MAGIC_BYTE = -128;
/*     */   private short reserved;
/*     */   
/*     */   public DefaultBinaryMemcacheRequest()
/*     */   {
/*  36 */     this(null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultBinaryMemcacheRequest(String key)
/*     */   {
/*  45 */     this(key, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultBinaryMemcacheRequest(ByteBuf extras)
/*     */   {
/*  54 */     this(null, extras);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultBinaryMemcacheRequest(String key, ByteBuf extras)
/*     */   {
/*  64 */     super(key, extras);
/*  65 */     setMagic((byte)Byte.MIN_VALUE);
/*     */   }
/*     */   
/*     */   public short reserved()
/*     */   {
/*  70 */     return this.reserved;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheRequest setReserved(short reserved)
/*     */   {
/*  75 */     this.reserved = reserved;
/*  76 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheRequest retain()
/*     */   {
/*  81 */     super.retain();
/*  82 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheRequest retain(int increment)
/*     */   {
/*  87 */     super.retain(increment);
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheRequest touch()
/*     */   {
/*  93 */     super.touch();
/*  94 */     return this;
/*     */   }
/*     */   
/*     */   public BinaryMemcacheRequest touch(Object hint)
/*     */   {
/*  99 */     super.touch(hint);
/* 100 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\memcache\binary\DefaultBinaryMemcacheRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */