/*     */ package io.netty.handler.codec.dns;
/*     */ 
/*     */ import java.net.InetSocketAddress;
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
/*     */ public class DnsQuery
/*     */   extends DnsMessage
/*     */ {
/*     */   private final InetSocketAddress recipient;
/*     */   
/*     */   public DnsQuery(int id, InetSocketAddress recipient)
/*     */   {
/*  32 */     super(id);
/*  33 */     if (recipient == null) {
/*  34 */       throw new NullPointerException("recipient");
/*     */     }
/*  36 */     this.recipient = recipient;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public InetSocketAddress recipient()
/*     */   {
/*  43 */     return this.recipient;
/*     */   }
/*     */   
/*     */   public DnsQuery addAnswer(DnsResource answer)
/*     */   {
/*  48 */     super.addAnswer(answer);
/*  49 */     return this;
/*     */   }
/*     */   
/*     */   public DnsQuery addQuestion(DnsQuestion question)
/*     */   {
/*  54 */     super.addQuestion(question);
/*  55 */     return this;
/*     */   }
/*     */   
/*     */   public DnsQuery addAuthorityResource(DnsResource resource)
/*     */   {
/*  60 */     super.addAuthorityResource(resource);
/*  61 */     return this;
/*     */   }
/*     */   
/*     */   public DnsQuery addAdditionalResource(DnsResource resource)
/*     */   {
/*  66 */     super.addAdditionalResource(resource);
/*  67 */     return this;
/*     */   }
/*     */   
/*     */   public DnsQuery touch(Object hint)
/*     */   {
/*  72 */     super.touch(hint);
/*  73 */     return this;
/*     */   }
/*     */   
/*     */   public DnsQuery retain()
/*     */   {
/*  78 */     super.retain();
/*  79 */     return this;
/*     */   }
/*     */   
/*     */   public DnsQuery retain(int increment)
/*     */   {
/*  84 */     super.retain(increment);
/*  85 */     return this;
/*     */   }
/*     */   
/*     */   public DnsQuery touch()
/*     */   {
/*  90 */     super.touch();
/*  91 */     return this;
/*     */   }
/*     */   
/*     */   public DnsQueryHeader header()
/*     */   {
/*  96 */     return (DnsQueryHeader)super.header();
/*     */   }
/*     */   
/*     */   protected DnsQueryHeader newHeader(int id)
/*     */   {
/* 101 */     return new DnsQueryHeader(this, id);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\dns\DnsQuery.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */