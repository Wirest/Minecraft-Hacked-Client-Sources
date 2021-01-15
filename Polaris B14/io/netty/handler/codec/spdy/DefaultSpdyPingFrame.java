/*    */ package io.netty.handler.codec.spdy;
/*    */ 
/*    */ import io.netty.util.internal.StringUtil;
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
/*    */ public class DefaultSpdyPingFrame
/*    */   implements SpdyPingFrame
/*    */ {
/*    */   private int id;
/*    */   
/*    */   public DefaultSpdyPingFrame(int id)
/*    */   {
/* 33 */     setId(id);
/*    */   }
/*    */   
/*    */   public int id()
/*    */   {
/* 38 */     return this.id;
/*    */   }
/*    */   
/*    */   public SpdyPingFrame setId(int id)
/*    */   {
/* 43 */     this.id = id;
/* 44 */     return this;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 49 */     return StringUtil.simpleClassName(this) + StringUtil.NEWLINE + "--> ID = " + id();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\spdy\DefaultSpdyPingFrame.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */