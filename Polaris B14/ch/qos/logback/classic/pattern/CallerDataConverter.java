/*     */ package ch.qos.logback.classic.pattern;
/*     */ 
/*     */ import ch.qos.logback.classic.spi.CallerData;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.CoreConstants;
/*     */ import ch.qos.logback.core.boolex.EvaluationException;
/*     */ import ch.qos.logback.core.boolex.EventEvaluator;
/*     */ import ch.qos.logback.core.status.ErrorStatus;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
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
/*     */ public class CallerDataConverter
/*     */   extends ClassicConverter
/*     */ {
/*     */   public static final String DEFAULT_CALLER_LINE_PREFIX = "Caller+";
/*     */   public static final String DEFAULT_RANGE_DELIMITER = "..";
/*  40 */   private int depthStart = 0;
/*  41 */   private int depthEnd = 5;
/*  42 */   List<EventEvaluator<ILoggingEvent>> evaluatorList = null;
/*     */   
/*     */ 
/*  45 */   final int MAX_ERROR_COUNT = 4;
/*  46 */   int errorCount = 0;
/*     */   
/*     */   public void start()
/*     */   {
/*  50 */     String depthStr = getFirstOption();
/*  51 */     if (depthStr == null) {
/*  52 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  56 */       if (isRange(depthStr)) {
/*  57 */         String[] numbers = splitRange(depthStr);
/*  58 */         if (numbers.length == 2) {
/*  59 */           this.depthStart = Integer.parseInt(numbers[0]);
/*  60 */           this.depthEnd = Integer.parseInt(numbers[1]);
/*  61 */           checkRange();
/*     */         } else {
/*  63 */           addError("Failed to parse depth option as range [" + depthStr + "]");
/*     */         }
/*     */       } else {
/*  66 */         this.depthEnd = Integer.parseInt(depthStr);
/*     */       }
/*     */     } catch (NumberFormatException nfe) {
/*  69 */       addError("Failed to parse depth option [" + depthStr + "]", nfe);
/*     */     }
/*     */     
/*  72 */     List optionList = getOptionList();
/*     */     
/*  74 */     if ((optionList != null) && (optionList.size() > 1)) {
/*  75 */       int optionListSize = optionList.size();
/*  76 */       for (int i = 1; i < optionListSize; i++) {
/*  77 */         String evaluatorStr = (String)optionList.get(i);
/*  78 */         Context context = getContext();
/*  79 */         if (context != null) {
/*  80 */           Map evaluatorMap = (Map)context.getObject("EVALUATOR_MAP");
/*     */           
/*  82 */           EventEvaluator<ILoggingEvent> ee = (EventEvaluator)evaluatorMap.get(evaluatorStr);
/*     */           
/*  84 */           if (ee != null) {
/*  85 */             addEvaluator(ee);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isRange(String depthStr) {
/*  93 */     return depthStr.contains(getDefaultRangeDelimiter());
/*     */   }
/*     */   
/*     */   private String[] splitRange(String depthStr) {
/*  97 */     return depthStr.split(Pattern.quote(getDefaultRangeDelimiter()), 2);
/*     */   }
/*     */   
/*     */   private void checkRange() {
/* 101 */     if ((this.depthStart < 0) || (this.depthEnd < 0)) {
/* 102 */       addError("Invalid depthStart/depthEnd range [" + this.depthStart + ", " + this.depthEnd + "] (negative values are not allowed)");
/* 103 */     } else if (this.depthStart >= this.depthEnd) {
/* 104 */       addError("Invalid depthEnd range [" + this.depthStart + ", " + this.depthEnd + "] (start greater or equal to end)");
/*     */     }
/*     */   }
/*     */   
/*     */   private void addEvaluator(EventEvaluator<ILoggingEvent> ee) {
/* 109 */     if (this.evaluatorList == null) {
/* 110 */       this.evaluatorList = new ArrayList();
/*     */     }
/* 112 */     this.evaluatorList.add(ee);
/*     */   }
/*     */   
/*     */   public String convert(ILoggingEvent le) {
/* 116 */     StringBuilder buf = new StringBuilder();
/*     */     
/* 118 */     if (this.evaluatorList != null) {
/* 119 */       boolean printCallerData = false;
/* 120 */       for (int i = 0; i < this.evaluatorList.size(); i++) {
/* 121 */         EventEvaluator<ILoggingEvent> ee = (EventEvaluator)this.evaluatorList.get(i);
/*     */         try {
/* 123 */           if (ee.evaluate(le)) {
/* 124 */             printCallerData = true;
/* 125 */             break;
/*     */           }
/*     */         } catch (EvaluationException eex) {
/* 128 */           this.errorCount += 1;
/* 129 */           if (this.errorCount < 4) {
/* 130 */             addError("Exception thrown for evaluator named [" + ee.getName() + "]", eex);
/*     */           }
/* 132 */           else if (this.errorCount == 4) {
/* 133 */             ErrorStatus errorStatus = new ErrorStatus("Exception thrown for evaluator named [" + ee.getName() + "].", this, eex);
/*     */             
/*     */ 
/* 136 */             errorStatus.add(new ErrorStatus("This was the last warning about this evaluator's errors.We don't want the StatusManager to get flooded.", this));
/*     */             
/*     */ 
/* 139 */             addStatus(errorStatus);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 145 */       if (!printCallerData) {
/* 146 */         return "";
/*     */       }
/*     */     }
/*     */     
/* 150 */     StackTraceElement[] cda = le.getCallerData();
/* 151 */     if ((cda != null) && (cda.length > this.depthStart)) {
/* 152 */       int limit = this.depthEnd < cda.length ? this.depthEnd : cda.length;
/*     */       
/* 154 */       for (int i = this.depthStart; i < limit; i++) {
/* 155 */         buf.append(getCallerLinePrefix());
/* 156 */         buf.append(i);
/* 157 */         buf.append("\t at ");
/* 158 */         buf.append(cda[i]);
/* 159 */         buf.append(CoreConstants.LINE_SEPARATOR);
/*     */       }
/* 161 */       return buf.toString();
/*     */     }
/* 163 */     return CallerData.CALLER_DATA_NA;
/*     */   }
/*     */   
/*     */   protected String getCallerLinePrefix()
/*     */   {
/* 168 */     return "Caller+";
/*     */   }
/*     */   
/*     */   protected String getDefaultRangeDelimiter() {
/* 172 */     return "..";
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\pattern\CallerDataConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */