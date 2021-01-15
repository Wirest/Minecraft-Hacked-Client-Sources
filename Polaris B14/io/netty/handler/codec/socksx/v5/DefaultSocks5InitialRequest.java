/*    */ package io.netty.handler.codec.socksx.v5;
/*    */ 
/*    */ import io.netty.handler.codec.DecoderResult;
/*    */ import io.netty.util.internal.StringUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
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
/*    */ public class DefaultSocks5InitialRequest
/*    */   extends AbstractSocks5Message
/*    */   implements Socks5InitialRequest
/*    */ {
/*    */   private final List<Socks5AuthMethod> authMethods;
/*    */   
/*    */   public DefaultSocks5InitialRequest(Socks5AuthMethod... authMethods)
/*    */   {
/* 33 */     if (authMethods == null) {
/* 34 */       throw new NullPointerException("authMethods");
/*    */     }
/*    */     
/* 37 */     List<Socks5AuthMethod> list = new ArrayList(authMethods.length);
/* 38 */     for (Socks5AuthMethod m : authMethods) {
/* 39 */       if (m == null) {
/*    */         break;
/*    */       }
/* 42 */       list.add(m);
/*    */     }
/*    */     
/* 45 */     if (list.isEmpty()) {
/* 46 */       throw new IllegalArgumentException("authMethods is empty");
/*    */     }
/*    */     
/* 49 */     this.authMethods = Collections.unmodifiableList(list);
/*    */   }
/*    */   
/*    */   public DefaultSocks5InitialRequest(Iterable<Socks5AuthMethod> authMethods) {
/* 53 */     if (authMethods == null) {
/* 54 */       throw new NullPointerException("authSchemes");
/*    */     }
/*    */     
/* 57 */     List<Socks5AuthMethod> list = new ArrayList();
/* 58 */     for (Socks5AuthMethod m : authMethods) {
/* 59 */       if (m == null) {
/*    */         break;
/*    */       }
/* 62 */       list.add(m);
/*    */     }
/*    */     
/* 65 */     if (list.isEmpty()) {
/* 66 */       throw new IllegalArgumentException("authMethods is empty");
/*    */     }
/*    */     
/* 69 */     this.authMethods = Collections.unmodifiableList(list);
/*    */   }
/*    */   
/*    */   public List<Socks5AuthMethod> authMethods()
/*    */   {
/* 74 */     return this.authMethods;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 79 */     StringBuilder buf = new StringBuilder(StringUtil.simpleClassName(this));
/*    */     
/* 81 */     DecoderResult decoderResult = decoderResult();
/* 82 */     if (!decoderResult.isSuccess()) {
/* 83 */       buf.append("(decoderResult: ");
/* 84 */       buf.append(decoderResult);
/* 85 */       buf.append(", authMethods: ");
/*    */     } else {
/* 87 */       buf.append("(authMethods: ");
/*    */     }
/* 89 */     buf.append(authMethods());
/* 90 */     buf.append(')');
/*    */     
/* 92 */     return buf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\socksx\v5\DefaultSocks5InitialRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */