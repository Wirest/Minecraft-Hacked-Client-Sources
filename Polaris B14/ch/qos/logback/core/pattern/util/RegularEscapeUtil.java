/*    */ package ch.qos.logback.core.pattern.util;
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
/*    */ public class RegularEscapeUtil
/*    */   implements IEscapeUtil
/*    */ {
/*    */   public void escape(String escapeChars, StringBuffer buf, char next, int pointer)
/*    */   {
/* 25 */     if (escapeChars.indexOf(next) >= 0) {
/* 26 */       buf.append(next);
/*    */     } else {
/* 28 */       switch (next)
/*    */       {
/*    */       case '_': 
/*    */         break;
/*    */       case '\\': 
/* 33 */         buf.append(next);
/* 34 */         break;
/*    */       case 't': 
/* 36 */         buf.append('\t');
/* 37 */         break;
/*    */       case 'r': 
/* 39 */         buf.append('\r');
/* 40 */         break;
/*    */       case 'n': 
/* 42 */         buf.append('\n');
/* 43 */         break;
/*    */       default: 
/* 45 */         String commaSeperatedEscapeChars = formatEscapeCharsForListing(escapeChars);
/* 46 */         throw new IllegalArgumentException("Illegal char '" + next + " at column " + pointer + ". Only \\\\, \\_" + commaSeperatedEscapeChars + ", \\t, \\n, \\r combinations are allowed as escape characters.");
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   String formatEscapeCharsForListing(String escapeChars)
/*    */   {
/* 53 */     StringBuilder commaSeperatedEscapeChars = new StringBuilder();
/* 54 */     for (int i = 0; i < escapeChars.length(); i++) {
/* 55 */       commaSeperatedEscapeChars.append(", \\").append(escapeChars.charAt(i));
/*    */     }
/* 57 */     return commaSeperatedEscapeChars.toString();
/*    */   }
/*    */   
/*    */   public static String basicEscape(String s)
/*    */   {
/* 62 */     int len = s.length();
/* 63 */     StringBuilder sbuf = new StringBuilder(len);
/*    */     
/* 65 */     int i = 0;
/* 66 */     while (i < len) {
/* 67 */       char c = s.charAt(i++);
/* 68 */       if (c == '\\') {
/* 69 */         c = s.charAt(i++);
/* 70 */         if (c == 'n') {
/* 71 */           c = '\n';
/* 72 */         } else if (c == 'r') {
/* 73 */           c = '\r';
/* 74 */         } else if (c == 't') {
/* 75 */           c = '\t';
/* 76 */         } else if (c == 'f') {
/* 77 */           c = '\f';
/* 78 */         } else if (c == '\b') {
/* 79 */           c = '\b';
/* 80 */         } else if (c == '"') {
/* 81 */           c = '"';
/* 82 */         } else if (c == '\'') {
/* 83 */           c = '\'';
/* 84 */         } else if (c == '\\') {
/* 85 */           c = '\\';
/*    */         }
/*    */       }
/* 88 */       sbuf.append(c);
/*    */     }
/* 90 */     return sbuf.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\util\RegularEscapeUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */