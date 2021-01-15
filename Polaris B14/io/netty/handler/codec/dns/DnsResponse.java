/*    */ package io.netty.handler.codec.dns;
/*    */ 
/*    */ import java.net.InetSocketAddress;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class DnsResponse
/*    */   extends DnsMessage
/*    */ {
/*    */   private final InetSocketAddress sender;
/*    */   
/*    */   public DnsResponse(int id, InetSocketAddress sender)
/*    */   {
/* 29 */     super(id);
/* 30 */     if (sender == null) {
/* 31 */       throw new NullPointerException("sender");
/*    */     }
/* 33 */     this.sender = sender;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public InetSocketAddress sender()
/*    */   {
/* 40 */     return this.sender;
/*    */   }
/*    */   
/*    */   public DnsResponse addAnswer(DnsResource answer)
/*    */   {
/* 45 */     super.addAnswer(answer);
/* 46 */     return this;
/*    */   }
/*    */   
/*    */   public DnsResponse addQuestion(DnsQuestion question)
/*    */   {
/* 51 */     super.addQuestion(question);
/* 52 */     return this;
/*    */   }
/*    */   
/*    */   public DnsResponse addAuthorityResource(DnsResource resource)
/*    */   {
/* 57 */     super.addAuthorityResource(resource);
/* 58 */     return this;
/*    */   }
/*    */   
/*    */   public DnsResponse addAdditionalResource(DnsResource resource)
/*    */   {
/* 63 */     super.addAdditionalResource(resource);
/* 64 */     return this;
/*    */   }
/*    */   
/*    */   public DnsResponse touch(Object hint)
/*    */   {
/* 69 */     super.touch(hint);
/* 70 */     return this;
/*    */   }
/*    */   
/*    */   public DnsResponse retain()
/*    */   {
/* 75 */     super.retain();
/* 76 */     return this;
/*    */   }
/*    */   
/*    */   public DnsResponse retain(int increment)
/*    */   {
/* 81 */     super.retain(increment);
/* 82 */     return this;
/*    */   }
/*    */   
/*    */   public DnsResponse touch()
/*    */   {
/* 87 */     super.touch();
/* 88 */     return this;
/*    */   }
/*    */   
/*    */   public DnsResponseHeader header()
/*    */   {
/* 93 */     return (DnsResponseHeader)super.header();
/*    */   }
/*    */   
/*    */   protected DnsResponseHeader newHeader(int id)
/*    */   {
/* 98 */     return new DnsResponseHeader(this, id);
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\dns\DnsResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */