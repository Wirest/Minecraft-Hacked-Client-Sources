/*    */ package ch.qos.logback.core.helpers;
/*    */ 
/*    */ import java.util.LinkedList;
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
/*    */ public class ThrowableToStringArray
/*    */ {
/*    */   public static String[] convert(Throwable t)
/*    */   {
/* 24 */     List<String> strList = new LinkedList();
/* 25 */     extract(strList, t, null);
/* 26 */     return (String[])strList.toArray(new String[0]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private static void extract(List<String> strList, Throwable t, StackTraceElement[] parentSTE)
/*    */   {
/* 33 */     StackTraceElement[] ste = t.getStackTrace();
/* 34 */     int numberOfcommonFrames = findNumberOfCommonFrames(ste, parentSTE);
/*    */     
/* 36 */     strList.add(formatFirstLine(t, parentSTE));
/* 37 */     for (int i = 0; i < ste.length - numberOfcommonFrames; i++) {
/* 38 */       strList.add("\tat " + ste[i].toString());
/*    */     }
/*    */     
/* 41 */     if (numberOfcommonFrames != 0) {
/* 42 */       strList.add("\t... " + numberOfcommonFrames + " common frames omitted");
/*    */     }
/*    */     
/* 45 */     Throwable cause = t.getCause();
/* 46 */     if (cause != null) {
/* 47 */       extract(strList, cause, ste);
/*    */     }
/*    */   }
/*    */   
/*    */   private static String formatFirstLine(Throwable t, StackTraceElement[] parentSTE)
/*    */   {
/* 53 */     String prefix = "";
/* 54 */     if (parentSTE != null) {
/* 55 */       prefix = "Caused by: ";
/*    */     }
/*    */     
/* 58 */     String result = prefix + t.getClass().getName();
/* 59 */     if (t.getMessage() != null) {
/* 60 */       result = result + ": " + t.getMessage();
/*    */     }
/* 62 */     return result;
/*    */   }
/*    */   
/*    */   private static int findNumberOfCommonFrames(StackTraceElement[] ste, StackTraceElement[] parentSTE)
/*    */   {
/* 67 */     if (parentSTE == null) {
/* 68 */       return 0;
/*    */     }
/*    */     
/* 71 */     int steIndex = ste.length - 1;
/* 72 */     int parentIndex = parentSTE.length - 1;
/* 73 */     int count = 0;
/* 74 */     while ((steIndex >= 0) && (parentIndex >= 0) && 
/* 75 */       (ste[steIndex].equals(parentSTE[parentIndex]))) {
/* 76 */       count++;
/*    */       
/*    */ 
/*    */ 
/* 80 */       steIndex--;
/* 81 */       parentIndex--;
/*    */     }
/* 83 */     return count;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\helpers\ThrowableToStringArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */