/*     */ package ch.qos.logback.classic.pattern;
/*     */ 
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.classic.spi.IThrowableProxy;
/*     */ import ch.qos.logback.classic.spi.StackTraceElementProxy;
/*     */ import ch.qos.logback.classic.spi.ThrowableProxyUtil;
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.CoreConstants;
/*     */ import ch.qos.logback.core.boolex.EvaluationException;
/*     */ import ch.qos.logback.core.boolex.EventEvaluator;
/*     */ import ch.qos.logback.core.status.ErrorStatus;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class ThrowableProxyConverter
/*     */   extends ThrowableHandlingConverter
/*     */ {
/*     */   protected static final int BUILDER_CAPACITY = 2048;
/*     */   int lengthOption;
/*  40 */   List<EventEvaluator<ILoggingEvent>> evaluatorList = null;
/*  41 */   List<String> ignoredStackTraceLines = null;
/*     */   
/*  43 */   int errorCount = 0;
/*     */   
/*     */ 
/*     */   public void start()
/*     */   {
/*  48 */     String lengthStr = getFirstOption();
/*     */     
/*  50 */     if (lengthStr == null) {
/*  51 */       this.lengthOption = Integer.MAX_VALUE;
/*     */     } else {
/*  53 */       lengthStr = lengthStr.toLowerCase();
/*  54 */       if ("full".equals(lengthStr)) {
/*  55 */         this.lengthOption = Integer.MAX_VALUE;
/*  56 */       } else if ("short".equals(lengthStr)) {
/*  57 */         this.lengthOption = 1;
/*     */       } else {
/*     */         try {
/*  60 */           this.lengthOption = Integer.parseInt(lengthStr);
/*     */         } catch (NumberFormatException nfe) {
/*  62 */           addError("Could not parse [" + lengthStr + "] as an integer");
/*  63 */           this.lengthOption = Integer.MAX_VALUE;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  68 */     List optionList = getOptionList();
/*     */     
/*  70 */     if ((optionList != null) && (optionList.size() > 1)) {
/*  71 */       int optionListSize = optionList.size();
/*  72 */       for (int i = 1; i < optionListSize; i++) {
/*  73 */         String evaluatorOrIgnoredStackTraceLine = (String)optionList.get(i);
/*  74 */         Context context = getContext();
/*  75 */         Map evaluatorMap = (Map)context.getObject("EVALUATOR_MAP");
/*  76 */         EventEvaluator<ILoggingEvent> ee = (EventEvaluator)evaluatorMap.get(evaluatorOrIgnoredStackTraceLine);
/*     */         
/*  78 */         if (ee != null) {
/*  79 */           addEvaluator(ee);
/*     */         } else {
/*  81 */           addIgnoreStackTraceLine(evaluatorOrIgnoredStackTraceLine);
/*     */         }
/*     */       }
/*     */     }
/*  85 */     super.start();
/*     */   }
/*     */   
/*     */   private void addEvaluator(EventEvaluator<ILoggingEvent> ee) {
/*  89 */     if (this.evaluatorList == null) {
/*  90 */       this.evaluatorList = new ArrayList();
/*     */     }
/*  92 */     this.evaluatorList.add(ee);
/*     */   }
/*     */   
/*     */   private void addIgnoreStackTraceLine(String ignoredStackTraceLine) {
/*  96 */     if (this.ignoredStackTraceLines == null) {
/*  97 */       this.ignoredStackTraceLines = new ArrayList();
/*     */     }
/*  99 */     this.ignoredStackTraceLines.add(ignoredStackTraceLine);
/*     */   }
/*     */   
/*     */   public void stop() {
/* 103 */     this.evaluatorList = null;
/* 104 */     super.stop();
/*     */   }
/*     */   
/*     */ 
/*     */   protected void extraData(StringBuilder builder, StackTraceElementProxy step) {}
/*     */   
/*     */ 
/*     */   public String convert(ILoggingEvent event)
/*     */   {
/* 113 */     IThrowableProxy tp = event.getThrowableProxy();
/* 114 */     if (tp == null) {
/* 115 */       return "";
/*     */     }
/*     */     
/*     */ 
/* 119 */     if (this.evaluatorList != null) {
/* 120 */       boolean printStack = true;
/* 121 */       for (int i = 0; i < this.evaluatorList.size(); i++) {
/* 122 */         EventEvaluator<ILoggingEvent> ee = (EventEvaluator)this.evaluatorList.get(i);
/*     */         try {
/* 124 */           if (ee.evaluate(event)) {
/* 125 */             printStack = false;
/* 126 */             break;
/*     */           }
/*     */         } catch (EvaluationException eex) {
/* 129 */           this.errorCount += 1;
/* 130 */           if (this.errorCount < 4) {
/* 131 */             addError("Exception thrown for evaluator named [" + ee.getName() + "]", eex);
/*     */           }
/* 133 */           else if (this.errorCount == 4) {
/* 134 */             ErrorStatus errorStatus = new ErrorStatus("Exception thrown for evaluator named [" + ee.getName() + "].", this, eex);
/*     */             
/*     */ 
/* 137 */             errorStatus.add(new ErrorStatus("This was the last warning about this evaluator's errors.We don't want the StatusManager to get flooded.", this));
/*     */             
/*     */ 
/* 140 */             addStatus(errorStatus);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 145 */       if (!printStack) {
/* 146 */         return "";
/*     */       }
/*     */     }
/*     */     
/* 150 */     return throwableProxyToString(tp);
/*     */   }
/*     */   
/*     */   protected String throwableProxyToString(IThrowableProxy tp) {
/* 154 */     StringBuilder sb = new StringBuilder(2048);
/*     */     
/* 156 */     recursiveAppend(sb, null, 1, tp);
/*     */     
/* 158 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private void recursiveAppend(StringBuilder sb, String prefix, int indent, IThrowableProxy tp) {
/* 162 */     if (tp == null)
/* 163 */       return;
/* 164 */     subjoinFirstLine(sb, prefix, indent, tp);
/* 165 */     sb.append(CoreConstants.LINE_SEPARATOR);
/* 166 */     subjoinSTEPArray(sb, indent, tp);
/* 167 */     IThrowableProxy[] suppressed = tp.getSuppressed();
/* 168 */     if (suppressed != null) {
/* 169 */       for (IThrowableProxy current : suppressed) {
/* 170 */         recursiveAppend(sb, "Suppressed: ", indent + 1, current);
/*     */       }
/*     */     }
/* 173 */     recursiveAppend(sb, "Caused by: ", indent, tp.getCause());
/*     */   }
/*     */   
/*     */   private void subjoinFirstLine(StringBuilder buf, String prefix, int indent, IThrowableProxy tp) {
/* 177 */     ThrowableProxyUtil.indent(buf, indent - 1);
/* 178 */     if (prefix != null) {
/* 179 */       buf.append(prefix);
/*     */     }
/* 181 */     subjoinExceptionMessage(buf, tp);
/*     */   }
/*     */   
/*     */   private void subjoinExceptionMessage(StringBuilder buf, IThrowableProxy tp) {
/* 185 */     buf.append(tp.getClassName()).append(": ").append(tp.getMessage());
/*     */   }
/*     */   
/*     */   protected void subjoinSTEPArray(StringBuilder buf, int indent, IThrowableProxy tp) {
/* 189 */     StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
/* 190 */     int commonFrames = tp.getCommonFrames();
/*     */     
/* 192 */     boolean unrestrictedPrinting = this.lengthOption > stepArray.length;
/*     */     
/*     */ 
/* 195 */     int maxIndex = unrestrictedPrinting ? stepArray.length : this.lengthOption;
/* 196 */     if ((commonFrames > 0) && (unrestrictedPrinting)) {
/* 197 */       maxIndex -= commonFrames;
/*     */     }
/*     */     
/* 200 */     int ignoredCount = 0;
/* 201 */     for (int i = 0; i < maxIndex; i++) {
/* 202 */       StackTraceElementProxy element = stepArray[i];
/* 203 */       if (!isIgnoredStackTraceLine(element.toString())) {
/* 204 */         ThrowableProxyUtil.indent(buf, indent);
/* 205 */         printStackLine(buf, ignoredCount, element);
/* 206 */         ignoredCount = 0;
/* 207 */         buf.append(CoreConstants.LINE_SEPARATOR);
/*     */       } else {
/* 209 */         ignoredCount++;
/* 210 */         if (maxIndex < stepArray.length) {
/* 211 */           maxIndex++;
/*     */         }
/*     */       }
/*     */     }
/* 215 */     if (ignoredCount > 0) {
/* 216 */       printIgnoredCount(buf, ignoredCount);
/* 217 */       buf.append(CoreConstants.LINE_SEPARATOR);
/*     */     }
/*     */     
/* 220 */     if ((commonFrames > 0) && (unrestrictedPrinting)) {
/* 221 */       ThrowableProxyUtil.indent(buf, indent);
/* 222 */       buf.append("... ").append(tp.getCommonFrames()).append(" common frames omitted").append(CoreConstants.LINE_SEPARATOR);
/*     */     }
/*     */   }
/*     */   
/*     */   private void printStackLine(StringBuilder buf, int ignoredCount, StackTraceElementProxy element)
/*     */   {
/* 228 */     buf.append(element);
/* 229 */     extraData(buf, element);
/* 230 */     if (ignoredCount > 0) {
/* 231 */       printIgnoredCount(buf, ignoredCount);
/*     */     }
/*     */   }
/*     */   
/*     */   private void printIgnoredCount(StringBuilder buf, int ignoredCount) {
/* 236 */     buf.append(" [").append(ignoredCount).append(" skipped]");
/*     */   }
/*     */   
/*     */   private boolean isIgnoredStackTraceLine(String line) {
/* 240 */     if (this.ignoredStackTraceLines != null) {
/* 241 */       for (String ignoredStackTraceLine : this.ignoredStackTraceLines) {
/* 242 */         if (line.contains(ignoredStackTraceLine)) {
/* 243 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 247 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\pattern\ThrowableProxyConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */