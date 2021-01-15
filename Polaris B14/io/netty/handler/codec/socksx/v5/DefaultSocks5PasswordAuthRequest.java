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
/*    */ public class DefaultSocks5PasswordAuthRequest
/*    */   extends AbstractSocks5Message
/*    */   implements Socks5PasswordAuthRequest
/*    */ {
/*    */   private final String username;
/*    */   private final String password;
/*    */   
/*    */   public DefaultSocks5PasswordAuthRequest(String username, String password)
/*    */   {
/* 30 */     if (username == null) {
/* 31 */       throw new NullPointerException("username");
/*    */     }
/* 33 */     if (password == null) {
/* 34 */       throw new NullPointerException("password");
/*    */     }
/*    */     
/* 37 */     if (username.length() > 255) {
/* 38 */       throw new IllegalArgumentException("username: **** (expected: less than 256 chars)");
/*    */     }
/* 40 */     if (password.length() > 255) {
/* 41 */       throw new IllegalArgumentException("password: **** (expected: less than 256 chars)");
/*    */     }
/*    */     
/* 44 */     this.username = username;
/* 45 */     this.password = password;
/*    */   }
/*    */   
/*    */   public String username()
/*    */   {
/* 50 */     return this.username;
/*    */   }
/*    */   
/*    */   public String password()
/*    */   {
/* 55 */     return this.password;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 60 */     StringBuilder buf = new StringBuilder(StringUtil.simpleClassName(this));
/*    */     
/* 62 */     DecoderResult decoderResult = decoderResult();
/* 63 */     if (!decoderResult.isSuccess()) {
/* 64 */       buf.append("(decoderResult: ");
/* 65 */       buf.append(decoderResult);
/* 66 */       buf.append(", username: ");
/*    */     } else {
/* 68 */       buf.append("(username: ");
/*    */     }
/* 70 */     buf.append(username());
/* 71 */     buf.append(", password: ****)");
/*    */     
/* 73 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\DefaultSocks5PasswordAuthRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */