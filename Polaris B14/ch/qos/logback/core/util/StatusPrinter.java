/*     */ package ch.qos.logback.core.util;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.CoreConstants;
/*     */ import ch.qos.logback.core.helpers.ThrowableToStringArray;
/*     */ import ch.qos.logback.core.status.Status;
/*     */ import ch.qos.logback.core.status.StatusManager;
/*     */ import ch.qos.logback.core.status.StatusUtil;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Iterator;
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
/*     */ public class StatusPrinter
/*     */ {
/*  29 */   private static PrintStream ps = System.out;
/*     */   
/*  31 */   static CachingDateFormatter cachingDateFormat = new CachingDateFormatter("HH:mm:ss,SSS");
/*     */   
/*     */   public static void setPrintStream(PrintStream printStream)
/*     */   {
/*  35 */     ps = printStream;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void printInCaseOfErrorsOrWarnings(Context context)
/*     */   {
/*  45 */     printInCaseOfErrorsOrWarnings(context, 0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void printInCaseOfErrorsOrWarnings(Context context, long threshold)
/*     */   {
/*  55 */     if (context == null) {
/*  56 */       throw new IllegalArgumentException("Context argument cannot be null");
/*     */     }
/*     */     
/*  59 */     StatusManager sm = context.getStatusManager();
/*  60 */     if (sm == null) {
/*  61 */       ps.println("WARN: Context named \"" + context.getName() + "\" has no status manager");
/*     */     }
/*     */     else {
/*  64 */       StatusUtil statusUtil = new StatusUtil(context);
/*  65 */       if (statusUtil.getHighestLevel(threshold) >= 1) {
/*  66 */         print(sm, threshold);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void printIfErrorsOccured(Context context)
/*     */   {
/*  78 */     if (context == null) {
/*  79 */       throw new IllegalArgumentException("Context argument cannot be null");
/*     */     }
/*     */     
/*  82 */     StatusManager sm = context.getStatusManager();
/*  83 */     if (sm == null) {
/*  84 */       ps.println("WARN: Context named \"" + context.getName() + "\" has no status manager");
/*     */     }
/*     */     else {
/*  87 */       StatusUtil statusUtil = new StatusUtil(context);
/*  88 */       if (statusUtil.getHighestLevel(0L) == 2) {
/*  89 */         print(sm);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void print(Context context)
/*     */   {
/* 100 */     print(context, 0L);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void print(Context context, long threshold)
/*     */   {
/* 108 */     if (context == null) {
/* 109 */       throw new IllegalArgumentException("Context argument cannot be null");
/*     */     }
/*     */     
/* 112 */     StatusManager sm = context.getStatusManager();
/* 113 */     if (sm == null) {
/* 114 */       ps.println("WARN: Context named \"" + context.getName() + "\" has no status manager");
/*     */     }
/*     */     else {
/* 117 */       print(sm, threshold);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void print(StatusManager sm) {
/* 122 */     print(sm, 0L);
/*     */   }
/*     */   
/*     */   public static void print(StatusManager sm, long threshold) {
/* 126 */     StringBuilder sb = new StringBuilder();
/* 127 */     List<Status> filteredList = StatusUtil.filterStatusListByTimeThreshold(sm.getCopyOfStatusList(), threshold);
/* 128 */     buildStrFromStatusList(sb, filteredList);
/* 129 */     ps.println(sb.toString());
/*     */   }
/*     */   
/*     */   public static void print(List<Status> statusList)
/*     */   {
/* 134 */     StringBuilder sb = new StringBuilder();
/* 135 */     buildStrFromStatusList(sb, statusList);
/* 136 */     ps.println(sb.toString());
/*     */   }
/*     */   
/*     */   private static void buildStrFromStatusList(StringBuilder sb, List<Status> statusList)
/*     */   {
/* 141 */     if (statusList == null)
/* 142 */       return;
/* 143 */     for (Status s : statusList) {
/* 144 */       buildStr(sb, "", s);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void appendThrowable(StringBuilder sb, Throwable t)
/*     */   {
/* 153 */     String[] stringRep = ThrowableToStringArray.convert(t);
/*     */     
/* 155 */     for (String s : stringRep) {
/* 156 */       if (!s.startsWith("Caused by: "))
/*     */       {
/* 158 */         if (Character.isDigit(s.charAt(0)))
/*     */         {
/* 160 */           sb.append("\t... ");
/*     */         }
/*     */         else
/* 163 */           sb.append("\tat ");
/*     */       }
/* 165 */       sb.append(s).append(CoreConstants.LINE_SEPARATOR);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void buildStr(StringBuilder sb, String indentation, Status s) { String prefix;
/*     */     String prefix;
/* 171 */     if (s.hasChildren()) {
/* 172 */       prefix = indentation + "+ ";
/*     */     } else {
/* 174 */       prefix = indentation + "|-";
/*     */     }
/*     */     
/* 177 */     if (cachingDateFormat != null) {
/* 178 */       String dateStr = cachingDateFormat.format(s.getDate().longValue());
/* 179 */       sb.append(dateStr).append(" ");
/*     */     }
/* 181 */     sb.append(prefix).append(s).append(CoreConstants.LINE_SEPARATOR);
/*     */     
/* 183 */     if (s.getThrowable() != null) {
/* 184 */       appendThrowable(sb, s.getThrowable());
/*     */     }
/* 186 */     if (s.hasChildren()) {
/* 187 */       Iterator<Status> ite = s.iterator();
/* 188 */       while (ite.hasNext()) {
/* 189 */         Status child = (Status)ite.next();
/* 190 */         buildStr(sb, indentation + "  ", child);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\util\StatusPrinter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */