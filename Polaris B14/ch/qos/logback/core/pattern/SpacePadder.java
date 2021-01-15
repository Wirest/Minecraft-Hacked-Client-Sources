/*    */ package ch.qos.logback.core.pattern;
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
/*    */ public class SpacePadder
/*    */ {
/* 18 */   static final String[] SPACES = { " ", "  ", "    ", "        ", "                ", "                                " };
/*    */   
/*    */ 
/*    */ 
/*    */   public static final void leftPad(StringBuilder buf, String s, int desiredLength)
/*    */   {
/* 24 */     int actualLen = 0;
/* 25 */     if (s != null) {
/* 26 */       actualLen = s.length();
/*    */     }
/* 28 */     if (actualLen < desiredLength) {
/* 29 */       spacePad(buf, desiredLength - actualLen);
/*    */     }
/* 31 */     if (s != null) {
/* 32 */       buf.append(s);
/*    */     }
/*    */   }
/*    */   
/*    */   public static final void rightPad(StringBuilder buf, String s, int desiredLength) {
/* 37 */     int actualLen = 0;
/* 38 */     if (s != null) {
/* 39 */       actualLen = s.length();
/*    */     }
/* 41 */     if (s != null) {
/* 42 */       buf.append(s);
/*    */     }
/* 44 */     if (actualLen < desiredLength) {
/* 45 */       spacePad(buf, desiredLength - actualLen);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static final void spacePad(StringBuilder sbuf, int length)
/*    */   {
/* 53 */     while (length >= 32) {
/* 54 */       sbuf.append(SPACES[5]);
/* 55 */       length -= 32;
/*    */     }
/*    */     
/* 58 */     for (int i = 4; i >= 0; i--) {
/* 59 */       if ((length & 1 << i) != 0) {
/* 60 */         sbuf.append(SPACES[i]);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\SpacePadder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */