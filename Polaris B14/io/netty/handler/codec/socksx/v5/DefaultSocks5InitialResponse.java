/*    */ package io.netty.handler.codec.socksx.v5;
/*    */ 
/*    */ import io.netty.handler.codec.DecoderResult;
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
/*    */ public class DefaultSocks5InitialResponse
/*    */   extends AbstractSocks5Message
/*    */   implements Socks5InitialResponse
/*    */ {
/*    */   private final Socks5AuthMethod authMethod;
/*    */   
/*    */   public DefaultSocks5InitialResponse(Socks5AuthMethod authMethod)
/*    */   {
/* 29 */     if (authMethod == null) {
/* 30 */       throw new NullPointerException("authMethod");
/*    */     }
/* 32 */     this.authMethod = authMethod;
/*    */   }
/*    */   
/*    */   public Socks5AuthMethod authMethod()
/*    */   {
/* 37 */     return this.authMethod;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 42 */     StringBuilder buf = new StringBuilder(StringUtil.simpleClassName(this));
/*    */     
/* 44 */     DecoderResult decoderResult = decoderResult();
/* 45 */     if (!decoderResult.isSuccess()) {
/* 46 */       buf.append("(decoderResult: ");
/* 47 */       buf.append(decoderResult);
/* 48 */       buf.append(", authMethod: ");
/*    */     } else {
/* 50 */       buf.append("(authMethod: ");
/*    */     }
/* 52 */     buf.append(authMethod());
/* 53 */     buf.append(')');
/*    */     
/* 55 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\DefaultSocks5InitialResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */