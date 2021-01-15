/*     */ package ch.qos.logback.classic.spi;
/*     */ 
/*     */ import ch.qos.logback.core.CoreConstants;
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
/*     */ public class ThrowableProxyUtil
/*     */ {
/*     */   public static final int REGULAR_EXCEPTION_INDENT = 1;
/*     */   public static final int SUPPRESSED_EXCEPTION_INDENT = 1;
/*     */   private static final int BUILDER_CAPACITY = 2048;
/*     */   
/*     */   public static void build(ThrowableProxy nestedTP, Throwable nestedThrowable, ThrowableProxy parentTP)
/*     */   {
/*  33 */     StackTraceElement[] nestedSTE = nestedThrowable.getStackTrace();
/*     */     
/*  35 */     int commonFramesCount = -1;
/*  36 */     if (parentTP != null) {
/*  37 */       commonFramesCount = findNumberOfCommonFrames(nestedSTE, parentTP.getStackTraceElementProxyArray());
/*     */     }
/*     */     
/*     */ 
/*  41 */     nestedTP.commonFrames = commonFramesCount;
/*  42 */     nestedTP.stackTraceElementProxyArray = steArrayToStepArray(nestedSTE);
/*     */   }
/*     */   
/*     */   static StackTraceElementProxy[] steArrayToStepArray(StackTraceElement[] stea) {
/*  46 */     if (stea == null) {
/*  47 */       return new StackTraceElementProxy[0];
/*     */     }
/*  49 */     StackTraceElementProxy[] stepa = new StackTraceElementProxy[stea.length];
/*  50 */     for (int i = 0; i < stepa.length; i++) {
/*  51 */       stepa[i] = new StackTraceElementProxy(stea[i]);
/*     */     }
/*  53 */     return stepa;
/*     */   }
/*     */   
/*     */   static int findNumberOfCommonFrames(StackTraceElement[] steArray, StackTraceElementProxy[] parentSTEPArray)
/*     */   {
/*  58 */     if ((parentSTEPArray == null) || (steArray == null)) {
/*  59 */       return 0;
/*     */     }
/*     */     
/*  62 */     int steIndex = steArray.length - 1;
/*  63 */     int parentIndex = parentSTEPArray.length - 1;
/*  64 */     int count = 0;
/*  65 */     while ((steIndex >= 0) && (parentIndex >= 0)) {
/*  66 */       StackTraceElement ste = steArray[steIndex];
/*  67 */       StackTraceElement otherSte = parentSTEPArray[parentIndex].ste;
/*  68 */       if (!ste.equals(otherSte)) break;
/*  69 */       count++;
/*     */       
/*     */ 
/*     */ 
/*  73 */       steIndex--;
/*  74 */       parentIndex--;
/*     */     }
/*  76 */     return count;
/*     */   }
/*     */   
/*     */   public static String asString(IThrowableProxy tp) {
/*  80 */     StringBuilder sb = new StringBuilder(2048);
/*     */     
/*  82 */     recursiveAppend(sb, null, 1, tp);
/*     */     
/*  84 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private static void recursiveAppend(StringBuilder sb, String prefix, int indent, IThrowableProxy tp) {
/*  88 */     if (tp == null)
/*  89 */       return;
/*  90 */     subjoinFirstLine(sb, prefix, indent, tp);
/*  91 */     sb.append(CoreConstants.LINE_SEPARATOR);
/*  92 */     subjoinSTEPArray(sb, indent, tp);
/*  93 */     IThrowableProxy[] suppressed = tp.getSuppressed();
/*  94 */     if (suppressed != null) {
/*  95 */       for (IThrowableProxy current : suppressed) {
/*  96 */         recursiveAppend(sb, "Suppressed: ", indent + 1, current);
/*     */       }
/*     */     }
/*  99 */     recursiveAppend(sb, "Caused by: ", indent, tp.getCause());
/*     */   }
/*     */   
/*     */   public static void indent(StringBuilder buf, int indent) {
/* 103 */     for (int j = 0; j < indent; j++) {
/* 104 */       buf.append('\t');
/*     */     }
/*     */   }
/*     */   
/*     */   private static void subjoinFirstLine(StringBuilder buf, String prefix, int indent, IThrowableProxy tp) {
/* 109 */     indent(buf, indent - 1);
/* 110 */     if (prefix != null) {
/* 111 */       buf.append(prefix);
/*     */     }
/* 113 */     subjoinExceptionMessage(buf, tp);
/*     */   }
/*     */   
/*     */   public static void subjoinPackagingData(StringBuilder builder, StackTraceElementProxy step) {
/* 117 */     if (step != null) {
/* 118 */       ClassPackagingData cpd = step.getClassPackagingData();
/* 119 */       if (cpd != null) {
/* 120 */         if (!cpd.isExact()) {
/* 121 */           builder.append(" ~[");
/*     */         } else {
/* 123 */           builder.append(" [");
/*     */         }
/*     */         
/* 126 */         builder.append(cpd.getCodeLocation()).append(':').append(cpd.getVersion()).append(']');
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void subjoinSTEP(StringBuilder sb, StackTraceElementProxy step)
/*     */   {
/* 133 */     sb.append(step.toString());
/* 134 */     subjoinPackagingData(sb, step);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   /**
/*     */    * @deprecated
/*     */    */
/*     */   public static void subjoinSTEPArray(StringBuilder sb, IThrowableProxy tp)
/*     */   {
/* 144 */     subjoinSTEPArray(sb, 1, tp);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void subjoinSTEPArray(StringBuilder sb, int indentLevel, IThrowableProxy tp)
/*     */   {
/* 153 */     StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
/* 154 */     int commonFrames = tp.getCommonFrames();
/*     */     
/* 156 */     for (int i = 0; i < stepArray.length - commonFrames; i++) {
/* 157 */       StackTraceElementProxy step = stepArray[i];
/* 158 */       indent(sb, indentLevel);
/* 159 */       subjoinSTEP(sb, step);
/* 160 */       sb.append(CoreConstants.LINE_SEPARATOR);
/*     */     }
/*     */     
/* 163 */     if (commonFrames > 0) {
/* 164 */       indent(sb, indentLevel);
/* 165 */       sb.append("... ").append(commonFrames).append(" common frames omitted").append(CoreConstants.LINE_SEPARATOR);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void subjoinFirstLine(StringBuilder buf, IThrowableProxy tp)
/*     */   {
/* 172 */     int commonFrames = tp.getCommonFrames();
/* 173 */     if (commonFrames > 0) {
/* 174 */       buf.append("Caused by: ");
/*     */     }
/* 176 */     subjoinExceptionMessage(buf, tp);
/*     */   }
/*     */   
/*     */   public static void subjoinFirstLineRootCauseFirst(StringBuilder buf, IThrowableProxy tp) {
/* 180 */     if (tp.getCause() != null) {
/* 181 */       buf.append("Wrapped by: ");
/*     */     }
/* 183 */     subjoinExceptionMessage(buf, tp);
/*     */   }
/*     */   
/*     */   private static void subjoinExceptionMessage(StringBuilder buf, IThrowableProxy tp) {
/* 187 */     buf.append(tp.getClassName()).append(": ").append(tp.getMessage());
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\spi\ThrowableProxyUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */