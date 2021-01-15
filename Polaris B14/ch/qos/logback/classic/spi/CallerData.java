/*     */ package ch.qos.logback.classic.spi;
/*     */ 
/*     */ import ch.qos.logback.core.CoreConstants;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CallerData
/*     */ {
/*     */   public static final String NA = "?";
/*     */   private static final String LOG4J_CATEGORY = "org.apache.log4j.Category";
/*     */   private static final String SLF4J_BOUNDARY = "org.slf4j.Logger";
/*     */   public static final int LINE_NA = -1;
/*  45 */   public static final String CALLER_DATA_NA = "?#?:?" + CoreConstants.LINE_SEPARATOR;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  50 */   public static final StackTraceElement[] EMPTY_CALLER_DATA_ARRAY = new StackTraceElement[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static StackTraceElement[] extract(Throwable t, String fqnOfInvokingClass, int maxDepth, List<String> frameworkPackageList)
/*     */   {
/*  60 */     if (t == null) {
/*  61 */       return null;
/*     */     }
/*     */     
/*  64 */     StackTraceElement[] steArray = t.getStackTrace();
/*     */     
/*     */ 
/*  67 */     int found = -1;
/*  68 */     for (int i = 0; i < steArray.length; i++) {
/*  69 */       if (isInFrameworkSpace(steArray[i].getClassName(), fqnOfInvokingClass, frameworkPackageList))
/*     */       {
/*     */ 
/*  72 */         found = i + 1;
/*     */       } else {
/*  74 */         if (found != -1) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  81 */     if (found == -1) {
/*  82 */       return EMPTY_CALLER_DATA_ARRAY;
/*     */     }
/*     */     
/*  85 */     int availableDepth = steArray.length - found;
/*  86 */     int desiredDepth = maxDepth < availableDepth ? maxDepth : availableDepth;
/*     */     
/*  88 */     StackTraceElement[] callerDataArray = new StackTraceElement[desiredDepth];
/*  89 */     for (int i = 0; i < desiredDepth; i++) {
/*  90 */       callerDataArray[i] = steArray[(found + i)];
/*     */     }
/*  92 */     return callerDataArray;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static boolean isInFrameworkSpace(String currentClass, String fqnOfInvokingClass, List<String> frameworkPackageList)
/*     */   {
/*  99 */     if ((currentClass.equals(fqnOfInvokingClass)) || (currentClass.equals("org.apache.log4j.Category")) || (currentClass.startsWith("org.slf4j.Logger")) || (isInFrameworkSpaceList(currentClass, frameworkPackageList)))
/*     */     {
/* 101 */       return true;
/*     */     }
/* 103 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isInFrameworkSpaceList(String currentClass, List<String> frameworkPackageList)
/*     */   {
/* 111 */     if (frameworkPackageList == null) {
/* 112 */       return false;
/*     */     }
/* 114 */     for (String s : frameworkPackageList) {
/* 115 */       if (currentClass.startsWith(s))
/* 116 */         return true;
/*     */     }
/* 118 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static StackTraceElement naInstance()
/*     */   {
/* 128 */     return new StackTraceElement("?", "?", "?", -1);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\CallerData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */