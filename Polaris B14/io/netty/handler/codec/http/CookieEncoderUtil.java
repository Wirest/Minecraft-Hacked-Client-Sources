/*    */ package io.netty.handler.codec.http;
/*    */ 
/*    */ import io.netty.util.internal.InternalThreadLocalMap;
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
/*    */ final class CookieEncoderUtil
/*    */ {
/*    */   static StringBuilder stringBuilder()
/*    */   {
/* 24 */     return InternalThreadLocalMap.get().stringBuilder();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   static String stripTrailingSeparatorOrNull(StringBuilder buf)
/*    */   {
/* 32 */     return buf.length() == 0 ? null : stripTrailingSeparator(buf);
/*    */   }
/*    */   
/*    */   static String stripTrailingSeparator(StringBuilder buf) {
/* 36 */     if (buf.length() > 0) {
/* 37 */       buf.setLength(buf.length() - 2);
/*    */     }
/* 39 */     return buf.toString();
/*    */   }
/*    */   
/*    */   static void addUnquoted(StringBuilder sb, String name, String val) {
/* 43 */     sb.append(name);
/* 44 */     sb.append('=');
/* 45 */     sb.append(val);
/* 46 */     sb.append(';');
/* 47 */     sb.append(' ');
/*    */   }
/*    */   
/*    */   static void add(StringBuilder sb, String name, long val) {
/* 51 */     sb.append(name);
/* 52 */     sb.append('=');
/* 53 */     sb.append(val);
/* 54 */     sb.append(';');
/* 55 */     sb.append(' ');
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\handler\codec\http\CookieEncoderUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */