/*    */ package io.netty.channel.udt;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.DefaultByteBufHolder;
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
/*    */ public final class UdtMessage
/*    */   extends DefaultByteBufHolder
/*    */ {
/*    */   public UdtMessage(ByteBuf data)
/*    */   {
/* 31 */     super(data);
/*    */   }
/*    */   
/*    */   public UdtMessage copy()
/*    */   {
/* 36 */     return new UdtMessage(content().copy());
/*    */   }
/*    */   
/*    */   public UdtMessage duplicate()
/*    */   {
/* 41 */     return new UdtMessage(content().duplicate());
/*    */   }
/*    */   
/*    */   public UdtMessage retain()
/*    */   {
/* 46 */     super.retain();
/* 47 */     return this;
/*    */   }
/*    */   
/*    */   public UdtMessage retain(int increment)
/*    */   {
/* 52 */     super.retain(increment);
/* 53 */     return this;
/*    */   }
/*    */   
/*    */   public UdtMessage touch()
/*    */   {
/* 58 */     super.touch();
/* 59 */     return this;
/*    */   }
/*    */   
/*    */   public UdtMessage touch(Object hint)
/*    */   {
/* 64 */     super.touch(hint);
/* 65 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\channel\udt\UdtMessage.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */