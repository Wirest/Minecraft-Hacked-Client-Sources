/*    */ package ch.qos.logback.core.util;
/*    */ 
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
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
/*    */ public class FileSize
/*    */ {
/*    */   private static final String LENGTH_PART = "([0-9]+)";
/*    */   private static final int DOUBLE_GROUP = 1;
/*    */   private static final String UNIT_PART = "(|kb|mb|gb)s?";
/*    */   private static final int UNIT_GROUP = 2;
/* 40 */   private static final Pattern FILE_SIZE_PATTERN = Pattern.compile("([0-9]+)\\s*(|kb|mb|gb)s?", 2);
/*    */   
/*    */   static final long KB_COEFFICIENT = 1024L;
/*    */   
/*    */   static final long MB_COEFFICIENT = 1048576L;
/*    */   static final long GB_COEFFICIENT = 1073741824L;
/*    */   final long size;
/*    */   
/*    */   FileSize(long size)
/*    */   {
/* 50 */     this.size = size;
/*    */   }
/*    */   
/*    */   public long getSize() {
/* 54 */     return this.size;
/*    */   }
/*    */   
/*    */   public static FileSize valueOf(String fileSizeStr) {
/* 58 */     Matcher matcher = FILE_SIZE_PATTERN.matcher(fileSizeStr);
/*    */     
/*    */ 
/* 61 */     if (matcher.matches()) {
/* 62 */       String lenStr = matcher.group(1);
/* 63 */       String unitStr = matcher.group(2);
/*    */       
/* 65 */       long lenValue = Long.valueOf(lenStr).longValue();
/* 66 */       long coefficient; if (unitStr.equalsIgnoreCase("")) {
/* 67 */         coefficient = 1L; } else { long coefficient;
/* 68 */         if (unitStr.equalsIgnoreCase("kb")) {
/* 69 */           coefficient = 1024L; } else { long coefficient;
/* 70 */           if (unitStr.equalsIgnoreCase("mb")) {
/* 71 */             coefficient = 1048576L; } else { long coefficient;
/* 72 */             if (unitStr.equalsIgnoreCase("gb")) {
/* 73 */               coefficient = 1073741824L;
/*    */             } else
/* 75 */               throw new IllegalStateException("Unexpected " + unitStr); } } }
/*    */       long coefficient;
/* 77 */       return new FileSize(lenValue * coefficient);
/*    */     }
/* 79 */     throw new IllegalArgumentException("String value [" + fileSizeStr + "] is not in the expected format.");
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\util\FileSize.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */