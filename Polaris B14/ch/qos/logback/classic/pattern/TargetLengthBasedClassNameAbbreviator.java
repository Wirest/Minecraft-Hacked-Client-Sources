/*     */ package ch.qos.logback.classic.pattern;
/*     */ 
/*     */ import java.io.PrintStream;
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
/*     */ public class TargetLengthBasedClassNameAbbreviator
/*     */   implements Abbreviator
/*     */ {
/*     */   final int targetLength;
/*     */   
/*     */   public TargetLengthBasedClassNameAbbreviator(int targetLength)
/*     */   {
/*  24 */     this.targetLength = targetLength;
/*     */   }
/*     */   
/*     */   public String abbreviate(String fqClassName) {
/*  28 */     StringBuilder buf = new StringBuilder(this.targetLength);
/*  29 */     if (fqClassName == null) {
/*  30 */       throw new IllegalArgumentException("Class name may not be null");
/*     */     }
/*     */     
/*  33 */     int inLen = fqClassName.length();
/*  34 */     if (inLen < this.targetLength) {
/*  35 */       return fqClassName;
/*     */     }
/*     */     
/*  38 */     int[] dotIndexesArray = new int[16];
/*     */     
/*     */ 
/*  41 */     int[] lengthArray = new int[17];
/*     */     
/*  43 */     int dotCount = computeDotIndexes(fqClassName, dotIndexesArray);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  48 */     if (dotCount == 0) {
/*  49 */       return fqClassName;
/*     */     }
/*     */     
/*  52 */     computeLengthArray(fqClassName, dotIndexesArray, lengthArray, dotCount);
/*     */     
/*  54 */     for (int i = 0; i <= dotCount; i++) {
/*  55 */       if (i == 0) {
/*  56 */         buf.append(fqClassName.substring(0, lengthArray[i] - 1));
/*     */       } else {
/*  58 */         buf.append(fqClassName.substring(dotIndexesArray[(i - 1)], dotIndexesArray[(i - 1)] + lengthArray[i]));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  64 */     return buf.toString();
/*     */   }
/*     */   
/*     */   static int computeDotIndexes(String className, int[] dotArray) {
/*  68 */     int dotCount = 0;
/*  69 */     int k = 0;
/*     */     
/*     */     for (;;)
/*     */     {
/*  73 */       k = className.indexOf('.', k);
/*  74 */       if ((k == -1) || (dotCount >= 16)) break;
/*  75 */       dotArray[dotCount] = k;
/*  76 */       dotCount++;
/*  77 */       k++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  82 */     return dotCount;
/*     */   }
/*     */   
/*     */   void computeLengthArray(String className, int[] dotArray, int[] lengthArray, int dotCount)
/*     */   {
/*  87 */     int toTrim = className.length() - this.targetLength;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */     for (int i = 0; i < dotCount; i++) {
/*  94 */       int previousDotPosition = -1;
/*  95 */       if (i > 0) {
/*  96 */         previousDotPosition = dotArray[(i - 1)];
/*     */       }
/*  98 */       int available = dotArray[i] - previousDotPosition - 1;
/*     */       
/*     */ 
/* 101 */       int len = available < 1 ? available : 1;
/*     */       
/*     */ 
/* 104 */       if (toTrim > 0) {
/* 105 */         len = available < 1 ? available : 1;
/*     */       } else {
/* 107 */         len = available;
/*     */       }
/* 109 */       toTrim -= available - len;
/* 110 */       lengthArray[i] = (len + 1);
/*     */     }
/*     */     
/* 113 */     int lastDotIndex = dotCount - 1;
/* 114 */     lengthArray[dotCount] = (className.length() - dotArray[lastDotIndex]);
/*     */   }
/*     */   
/*     */   static void printArray(String msg, int[] ia) {
/* 118 */     System.out.print(msg);
/* 119 */     for (int i = 0; i < ia.length; i++) {
/* 120 */       if (i == 0) {
/* 121 */         System.out.print(ia[i]);
/*     */       } else {
/* 123 */         System.out.print(", " + ia[i]);
/*     */       }
/*     */     }
/* 126 */     System.out.println();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\pattern\TargetLengthBasedClassNameAbbreviator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */