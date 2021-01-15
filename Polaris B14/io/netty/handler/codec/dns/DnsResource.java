/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufHolder;
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
/*     */ public final class DnsResource
/*     */   extends DnsEntry
/*     */   implements ByteBufHolder
/*     */ {
/*     */   private final long ttl;
/*     */   private final ByteBuf content;
/*     */   
/*     */   public DnsResource(String name, DnsType type, DnsClass aClass, long ttl, ByteBuf content)
/*     */   {
/*  45 */     super(name, type, aClass);
/*  46 */     this.ttl = ttl;
/*  47 */     this.content = content;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long timeToLive()
/*     */   {
/*  54 */     return this.ttl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteBuf content()
/*     */   {
/*  62 */     return this.content;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsResource copy()
/*     */   {
/*  70 */     return new DnsResource(name(), type(), dnsClass(), this.ttl, this.content.copy());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DnsResource duplicate()
/*     */   {
/*  78 */     return new DnsResource(name(), type(), dnsClass(), this.ttl, this.content.duplicate());
/*     */   }
/*     */   
/*     */   public int refCnt()
/*     */   {
/*  83 */     return this.content.refCnt();
/*     */   }
/*     */   
/*     */   public DnsResource retain()
/*     */   {
/*  88 */     this.content.retain();
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public DnsResource retain(int increment)
/*     */   {
/*  94 */     this.content.retain(increment);
/*  95 */     return this;
/*     */   }
/*     */   
/*     */   public boolean release()
/*     */   {
/* 100 */     return this.content.release();
/*     */   }
/*     */   
/*     */   public boolean release(int decrement)
/*     */   {
/* 105 */     return this.content.release(decrement);
/*     */   }
/*     */   
/*     */   public DnsResource touch()
/*     */   {
/* 110 */     this.content.touch();
/* 111 */     return this;
/*     */   }
/*     */   
/*     */   public DnsResource touch(Object hint)
/*     */   {
/* 116 */     this.content.touch(hint);
/* 117 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\dns\DnsResource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */