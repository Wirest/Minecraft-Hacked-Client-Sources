/*    */ package io.netty.channel.embedded;
/*    */ 
/*    */ import io.netty.channel.ChannelId;
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
/*    */ final class EmbeddedChannelId
/*    */   implements ChannelId
/*    */ {
/*    */   private static final long serialVersionUID = -251711922203466130L;
/* 28 */   static final ChannelId INSTANCE = new EmbeddedChannelId();
/*    */   
/*    */ 
/*    */ 
/*    */   public String asShortText()
/*    */   {
/* 34 */     return toString();
/*    */   }
/*    */   
/*    */   public String asLongText()
/*    */   {
/* 39 */     return toString();
/*    */   }
/*    */   
/*    */   public int compareTo(ChannelId o)
/*    */   {
/* 44 */     if (o == INSTANCE) {
/* 45 */       return 0;
/*    */     }
/*    */     
/* 48 */     return asLongText().compareTo(o.asLongText());
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 53 */     return super.hashCode();
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 58 */     return super.equals(obj);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 63 */     return "embedded";
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\embedded\EmbeddedChannelId.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */