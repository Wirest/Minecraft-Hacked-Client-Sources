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
/*    */ public class DefaultSocks5PasswordAuthResponse
/*    */   extends AbstractSocks5Message
/*    */   implements Socks5PasswordAuthResponse
/*    */ {
/*    */   private final Socks5PasswordAuthStatus status;
/*    */   
/*    */   public DefaultSocks5PasswordAuthResponse(Socks5PasswordAuthStatus status)
/*    */   {
/* 29 */     if (status == null) {
/* 30 */       throw new NullPointerException("status");
/*    */     }
/*    */     
/* 33 */     this.status = status;
/*    */   }
/*    */   
/*    */   public Socks5PasswordAuthStatus status()
/*    */   {
/* 38 */     return this.status;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 43 */     StringBuilder buf = new StringBuilder(StringUtil.simpleClassName(this));
/*    */     
/* 45 */     DecoderResult decoderResult = decoderResult();
/* 46 */     if (!decoderResult.isSuccess()) {
/* 47 */       buf.append("(decoderResult: ");
/* 48 */       buf.append(decoderResult);
/* 49 */       buf.append(", status: ");
/*    */     } else {
/* 51 */       buf.append("(status: ");
/*    */     }
/* 53 */     buf.append(status());
/* 54 */     buf.append(')');
/*    */     
/* 56 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\DefaultSocks5PasswordAuthResponse.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */