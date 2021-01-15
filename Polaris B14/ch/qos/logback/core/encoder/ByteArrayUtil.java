/*    */ package ch.qos.logback.core.encoder;
/*    */ 
/*    */ import java.io.ByteArrayOutputStream;
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
/*    */ public class ByteArrayUtil
/*    */ {
/*    */   static void writeInt(byte[] byteArray, int offset, int i)
/*    */   {
/* 22 */     for (int j = 0; j < 4; j++) {
/* 23 */       int shift = 24 - j * 8;
/* 24 */       byteArray[(offset + j)] = ((byte)(i >>> shift));
/*    */     }
/*    */   }
/*    */   
/*    */   static void writeInt(ByteArrayOutputStream baos, int i) {
/* 29 */     for (int j = 0; j < 4; j++) {
/* 30 */       int shift = 24 - j * 8;
/* 31 */       baos.write((byte)(i >>> shift));
/*    */     }
/*    */   }
/*    */   
/*    */   static int readInt(byte[] byteArray, int offset)
/*    */   {
/* 37 */     int i = 0;
/* 38 */     for (int j = 0; j < 4; j++) {
/* 39 */       int shift = 24 - j * 8;
/* 40 */       i += ((byteArray[(offset + j)] & 0xFF) << shift);
/*    */     }
/* 42 */     return i;
/*    */   }
/*    */   
/*    */   public static String toHexString(byte[] ba) {
/* 46 */     StringBuilder sbuf = new StringBuilder();
/* 47 */     for (byte b : ba) {
/* 48 */       String s = Integer.toHexString(b & 0xFF);
/* 49 */       if (s.length() == 1) {
/* 50 */         sbuf.append('0');
/*    */       }
/* 52 */       sbuf.append(s);
/*    */     }
/* 54 */     return sbuf.toString();
/*    */   }
/*    */   
/*    */   public static byte[] hexStringToByteArray(String s) {
/* 58 */     int len = s.length();
/* 59 */     byte[] ba = new byte[len / 2];
/*    */     
/* 61 */     for (int i = 0; i < ba.length; i++) {
/* 62 */       int j = i * 2;
/* 63 */       int t = Integer.parseInt(s.substring(j, j + 2), 16);
/* 64 */       byte b = (byte)(t & 0xFF);
/* 65 */       ba[i] = b;
/*    */     }
/* 67 */     return ba;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\encoder\ByteArrayUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */