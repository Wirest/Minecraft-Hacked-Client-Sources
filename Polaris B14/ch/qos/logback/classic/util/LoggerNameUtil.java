/*    */ package ch.qos.logback.classic.util;
/*    */ 
/*    */ import java.util.ArrayList;
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
/*    */ 
/*    */ 
/*    */ public class LoggerNameUtil
/*    */ {
/*    */   public static int getFirstSeparatorIndexOf(String name)
/*    */   {
/* 28 */     return getSeparatorIndexOf(name, 0);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static int getSeparatorIndexOf(String name, int fromIndex)
/*    */   {
/* 40 */     int dotIndex = name.indexOf('.', fromIndex);
/* 41 */     int dollarIndex = name.indexOf('$', fromIndex);
/*    */     
/* 43 */     if ((dotIndex == -1) && (dollarIndex == -1)) return -1;
/* 44 */     if (dotIndex == -1) return dollarIndex;
/* 45 */     if (dollarIndex == -1) { return dotIndex;
/*    */     }
/* 47 */     return dotIndex < dollarIndex ? dotIndex : dollarIndex;
/*    */   }
/*    */   
/*    */   public static List<String> computeNameParts(String loggerName) {
/* 51 */     List<String> partList = new ArrayList();
/*    */     
/* 53 */     int fromIndex = 0;
/*    */     for (;;) {
/* 55 */       int index = getSeparatorIndexOf(loggerName, fromIndex);
/* 56 */       if (index == -1) {
/* 57 */         partList.add(loggerName.substring(fromIndex));
/* 58 */         break;
/*    */       }
/* 60 */       partList.add(loggerName.substring(fromIndex, index));
/* 61 */       fromIndex = index + 1;
/*    */     }
/* 63 */     return partList;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\util\LoggerNameUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */