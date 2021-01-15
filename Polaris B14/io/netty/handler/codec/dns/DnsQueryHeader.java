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
/*    */ public final class DnsQueryHeader
/*    */   extends DnsHeader
/*    */ {
/*    */   public DnsQueryHeader(DnsMessage parent, int id)
/*    */   {
/* 32 */     super(parent);
/* 33 */     setId(id);
/* 34 */     setRecursionDesired(true);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int type()
/*    */   {
/* 43 */     return 0;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public DnsQueryHeader setType(int type)
/*    */   {
/* 54 */     if (type != 0) {
/* 55 */       throw new IllegalArgumentException("type cannot be anything but TYPE_QUERY (0) for a query header.");
/*    */     }
/* 57 */     super.setType(type);
/* 58 */     return this;
/*    */   }
/*    */   
/*    */   public DnsQueryHeader setId(int id)
/*    */   {
/* 63 */     super.setId(id);
/* 64 */     return this;
/*    */   }
/*    */   
/*    */   public DnsQueryHeader setRecursionDesired(boolean recursionDesired)
/*    */   {
/* 69 */     super.setRecursionDesired(recursionDesired);
/* 70 */     return this;
/*    */   }
/*    */   
/*    */   public DnsQueryHeader setOpcode(int opcode)
/*    */   {
/* 75 */     super.setOpcode(opcode);
/* 76 */     return this;
/*    */   }
/*    */   
/*    */   public DnsQueryHeader setZ(int z)
/*    */   {
/* 81 */     super.setZ(z);
/* 82 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\dns\DnsQueryHeader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */