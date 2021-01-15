/*    */ package org.json;
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
/*    */ public class HTTPTokener
/*    */   extends JSONTokener
/*    */ {
/*    */   public HTTPTokener(String string)
/*    */   {
/* 40 */     super(string);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String nextToken()
/*    */     throws JSONException
/*    */   {
/* 52 */     StringBuilder sb = new StringBuilder();
/*    */     char c;
/* 54 */     do { c = next();
/* 55 */     } while (Character.isWhitespace(c));
/* 56 */     if ((c == '"') || (c == '\'')) {
/* 57 */       char q = c;
/*    */       for (;;) {
/* 59 */         c = next();
/* 60 */         if (c < ' ') {
/* 61 */           throw syntaxError("Unterminated string.");
/*    */         }
/* 63 */         if (c == q) {
/* 64 */           return sb.toString();
/*    */         }
/* 66 */         sb.append(c);
/*    */       }
/*    */     }
/*    */     for (;;) {
/* 70 */       if ((c == 0) || (Character.isWhitespace(c))) {
/* 71 */         return sb.toString();
/*    */       }
/* 73 */       sb.append(c);
/* 74 */       c = next();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\json\HTTPTokener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */