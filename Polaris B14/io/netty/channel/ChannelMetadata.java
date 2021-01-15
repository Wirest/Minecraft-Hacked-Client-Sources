/*    */ package io.netty.channel;
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
/*    */ public final class ChannelMetadata
/*    */ {
/*    */   private final boolean hasDisconnect;
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
/*    */   public ChannelMetadata(boolean hasDisconnect)
/*    */   {
/* 35 */     this.hasDisconnect = hasDisconnect;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean hasDisconnect()
/*    */   {
/* 44 */     return this.hasDisconnect;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\ChannelMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */