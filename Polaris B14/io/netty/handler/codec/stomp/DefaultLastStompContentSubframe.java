/*    */ package io.netty.handler.codec.stomp;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
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
/*    */ public class DefaultLastStompContentSubframe
/*    */   extends DefaultStompContentSubframe
/*    */   implements LastStompContentSubframe
/*    */ {
/*    */   public DefaultLastStompContentSubframe(ByteBuf content)
/*    */   {
/* 26 */     super(content);
/*    */   }
/*    */   
/*    */   public DefaultLastStompContentSubframe retain()
/*    */   {
/* 31 */     super.retain();
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   public LastStompContentSubframe retain(int increment)
/*    */   {
/* 37 */     super.retain(increment);
/* 38 */     return this;
/*    */   }
/*    */   
/*    */   public LastStompContentSubframe touch()
/*    */   {
/* 43 */     super.touch();
/* 44 */     return this;
/*    */   }
/*    */   
/*    */   public LastStompContentSubframe touch(Object hint)
/*    */   {
/* 49 */     super.touch(hint);
/* 50 */     return this;
/*    */   }
/*    */   
/*    */   public LastStompContentSubframe copy()
/*    */   {
/* 55 */     return new DefaultLastStompContentSubframe(content().copy());
/*    */   }
/*    */   
/*    */   public LastStompContentSubframe duplicate()
/*    */   {
/* 60 */     return new DefaultLastStompContentSubframe(content().duplicate());
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 65 */     return "DefaultLastStompContent{decoderResult=" + decoderResult() + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\stomp\DefaultLastStompContentSubframe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */