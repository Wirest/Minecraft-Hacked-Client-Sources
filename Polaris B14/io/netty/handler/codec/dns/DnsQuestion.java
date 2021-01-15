/*    */ package io.netty.handler.codec.dns;
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
/*    */ public final class DnsQuestion
/*    */   extends DnsEntry
/*    */ {
/*    */   public DnsQuestion(String name, DnsType type)
/*    */   {
/* 38 */     this(name, type, DnsClass.IN);
/*    */   }
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
/*    */   public DnsQuestion(String name, DnsType type, DnsClass qClass)
/*    */   {
/* 53 */     super(name, type, qClass);
/*    */     
/* 55 */     if (name.isEmpty()) {
/* 56 */       throw new IllegalArgumentException("name must not be left blank.");
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean equals(Object other)
/*    */   {
/* 62 */     if (!(other instanceof DnsQuestion)) {
/* 63 */       return false;
/*    */     }
/* 65 */     return super.equals(other);
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 70 */     return super.hashCode();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\dns\DnsQuestion.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */