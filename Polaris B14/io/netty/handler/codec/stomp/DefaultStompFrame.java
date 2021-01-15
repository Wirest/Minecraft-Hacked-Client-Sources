/*    */ package io.netty.handler.codec.stomp;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.buffer.Unpooled;
/*    */ import io.netty.util.CharsetUtil;
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
/*    */ public class DefaultStompFrame
/*    */   extends DefaultStompHeadersSubframe
/*    */   implements StompFrame
/*    */ {
/*    */   private final ByteBuf content;
/*    */   
/*    */   public DefaultStompFrame(StompCommand command)
/*    */   {
/* 30 */     this(command, Unpooled.buffer(0));
/*    */   }
/*    */   
/*    */   public DefaultStompFrame(StompCommand command, ByteBuf content) {
/* 34 */     super(command);
/* 35 */     if (content == null) {
/* 36 */       throw new NullPointerException("content");
/*    */     }
/* 38 */     this.content = content;
/*    */   }
/*    */   
/*    */   public ByteBuf content()
/*    */   {
/* 43 */     return this.content;
/*    */   }
/*    */   
/*    */   public StompFrame copy()
/*    */   {
/* 48 */     return new DefaultStompFrame(this.command, this.content.copy());
/*    */   }
/*    */   
/*    */   public StompFrame duplicate()
/*    */   {
/* 53 */     return new DefaultStompFrame(this.command, this.content.duplicate());
/*    */   }
/*    */   
/*    */   public int refCnt()
/*    */   {
/* 58 */     return this.content.refCnt();
/*    */   }
/*    */   
/*    */   public StompFrame retain()
/*    */   {
/* 63 */     this.content.retain();
/* 64 */     return this;
/*    */   }
/*    */   
/*    */   public StompFrame retain(int increment)
/*    */   {
/* 69 */     this.content.retain();
/* 70 */     return this;
/*    */   }
/*    */   
/*    */   public StompFrame touch()
/*    */   {
/* 75 */     this.content.touch();
/* 76 */     return this;
/*    */   }
/*    */   
/*    */   public StompFrame touch(Object hint)
/*    */   {
/* 81 */     this.content.touch(hint);
/* 82 */     return this;
/*    */   }
/*    */   
/*    */   public boolean release()
/*    */   {
/* 87 */     return this.content.release();
/*    */   }
/*    */   
/*    */   public boolean release(int decrement)
/*    */   {
/* 92 */     return this.content.release(decrement);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 97 */     return "DefaultFullStompFrame{command=" + this.command + ", headers=" + this.headers + ", content=" + this.content.toString(CharsetUtil.UTF_8) + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\stomp\DefaultStompFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */