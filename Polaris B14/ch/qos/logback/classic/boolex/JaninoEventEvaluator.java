/*     */ package ch.qos.logback.classic.boolex;
/*     */ 
/*     */ import ch.qos.logback.classic.Level;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.classic.spi.IThrowableProxy;
/*     */ import ch.qos.logback.classic.spi.LoggerContextVO;
/*     */ import ch.qos.logback.classic.spi.ThrowableProxy;
/*     */ import ch.qos.logback.core.CoreConstants;
/*     */ import ch.qos.logback.core.boolex.JaninoEventEvaluatorBase;
/*     */ import ch.qos.logback.core.boolex.Matcher;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Marker;
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
/*     */ public class JaninoEventEvaluator
/*     */   extends JaninoEventEvaluatorBase<ILoggingEvent>
/*     */ {
/*     */   public static final String IMPORT_LEVEL = "import ch.qos.logback.classic.Level;\r\n";
/*  36 */   public static final List<String> DEFAULT_PARAM_NAME_LIST = new ArrayList();
/*  37 */   public static final List<Class> DEFAULT_PARAM_TYPE_LIST = new ArrayList();
/*     */   
/*     */   static {
/*  40 */     DEFAULT_PARAM_NAME_LIST.add("DEBUG");
/*  41 */     DEFAULT_PARAM_NAME_LIST.add("INFO");
/*  42 */     DEFAULT_PARAM_NAME_LIST.add("WARN");
/*  43 */     DEFAULT_PARAM_NAME_LIST.add("ERROR");
/*     */     
/*  45 */     DEFAULT_PARAM_NAME_LIST.add("event");
/*  46 */     DEFAULT_PARAM_NAME_LIST.add("message");
/*     */     
/*  48 */     DEFAULT_PARAM_NAME_LIST.add("formattedMessage");
/*  49 */     DEFAULT_PARAM_NAME_LIST.add("logger");
/*  50 */     DEFAULT_PARAM_NAME_LIST.add("loggerContext");
/*  51 */     DEFAULT_PARAM_NAME_LIST.add("level");
/*  52 */     DEFAULT_PARAM_NAME_LIST.add("timeStamp");
/*  53 */     DEFAULT_PARAM_NAME_LIST.add("marker");
/*  54 */     DEFAULT_PARAM_NAME_LIST.add("mdc");
/*  55 */     DEFAULT_PARAM_NAME_LIST.add("throwableProxy");
/*  56 */     DEFAULT_PARAM_NAME_LIST.add("throwable");
/*     */     
/*  58 */     DEFAULT_PARAM_TYPE_LIST.add(Integer.TYPE);
/*  59 */     DEFAULT_PARAM_TYPE_LIST.add(Integer.TYPE);
/*  60 */     DEFAULT_PARAM_TYPE_LIST.add(Integer.TYPE);
/*  61 */     DEFAULT_PARAM_TYPE_LIST.add(Integer.TYPE);
/*     */     
/*  63 */     DEFAULT_PARAM_TYPE_LIST.add(ILoggingEvent.class);
/*  64 */     DEFAULT_PARAM_TYPE_LIST.add(String.class);
/*  65 */     DEFAULT_PARAM_TYPE_LIST.add(String.class);
/*  66 */     DEFAULT_PARAM_TYPE_LIST.add(String.class);
/*  67 */     DEFAULT_PARAM_TYPE_LIST.add(LoggerContextVO.class);
/*  68 */     DEFAULT_PARAM_TYPE_LIST.add(Integer.TYPE);
/*  69 */     DEFAULT_PARAM_TYPE_LIST.add(Long.TYPE);
/*  70 */     DEFAULT_PARAM_TYPE_LIST.add(Marker.class);
/*  71 */     DEFAULT_PARAM_TYPE_LIST.add(Map.class);
/*  72 */     DEFAULT_PARAM_TYPE_LIST.add(IThrowableProxy.class);
/*  73 */     DEFAULT_PARAM_TYPE_LIST.add(Throwable.class);
/*     */   }
/*     */   
/*     */   protected String getDecoratedExpression() {
/*  77 */     String expression = getExpression();
/*  78 */     if (!expression.contains("return")) {
/*  79 */       expression = "return " + expression + ";";
/*  80 */       addInfo("Adding [return] prefix and a semicolon suffix. Expression becomes [" + expression + "]");
/*  81 */       addInfo("See also http://logback.qos.ch/codes.html#block");
/*     */     }
/*     */     
/*  84 */     return "import ch.qos.logback.classic.Level;\r\n" + expression;
/*     */   }
/*     */   
/*     */   protected String[] getParameterNames() {
/*  88 */     List<String> fullNameList = new ArrayList();
/*  89 */     fullNameList.addAll(DEFAULT_PARAM_NAME_LIST);
/*     */     
/*  91 */     for (int i = 0; i < this.matcherList.size(); i++) {
/*  92 */       Matcher m = (Matcher)this.matcherList.get(i);
/*  93 */       fullNameList.add(m.getName());
/*     */     }
/*     */     
/*  96 */     return (String[])fullNameList.toArray(CoreConstants.EMPTY_STRING_ARRAY);
/*     */   }
/*     */   
/*     */   protected Class[] getParameterTypes() {
/* 100 */     List<Class> fullTypeList = new ArrayList();
/* 101 */     fullTypeList.addAll(DEFAULT_PARAM_TYPE_LIST);
/* 102 */     for (int i = 0; i < this.matcherList.size(); i++) {
/* 103 */       fullTypeList.add(Matcher.class);
/*     */     }
/* 105 */     return (Class[])fullTypeList.toArray(CoreConstants.EMPTY_CLASS_ARRAY);
/*     */   }
/*     */   
/*     */   protected Object[] getParameterValues(ILoggingEvent loggingEvent) {
/* 109 */     int matcherListSize = this.matcherList.size();
/*     */     
/* 111 */     int i = 0;
/* 112 */     Object[] values = new Object[DEFAULT_PARAM_NAME_LIST.size() + matcherListSize];
/*     */     
/*     */ 
/* 115 */     values[(i++)] = Level.DEBUG_INTEGER;
/* 116 */     values[(i++)] = Level.INFO_INTEGER;
/* 117 */     values[(i++)] = Level.WARN_INTEGER;
/* 118 */     values[(i++)] = Level.ERROR_INTEGER;
/*     */     
/* 120 */     values[(i++)] = loggingEvent;
/* 121 */     values[(i++)] = loggingEvent.getMessage();
/* 122 */     values[(i++)] = loggingEvent.getFormattedMessage();
/* 123 */     values[(i++)] = loggingEvent.getLoggerName();
/* 124 */     values[(i++)] = loggingEvent.getLoggerContextVO();
/* 125 */     values[(i++)] = loggingEvent.getLevel().toInteger();
/* 126 */     values[(i++)] = Long.valueOf(loggingEvent.getTimeStamp());
/*     */     
/*     */ 
/*     */ 
/* 130 */     values[(i++)] = loggingEvent.getMarker();
/* 131 */     values[(i++)] = loggingEvent.getMDCPropertyMap();
/*     */     
/* 133 */     IThrowableProxy iThrowableProxy = loggingEvent.getThrowableProxy();
/*     */     
/* 135 */     if (iThrowableProxy != null) {
/* 136 */       values[(i++)] = iThrowableProxy;
/* 137 */       if ((iThrowableProxy instanceof ThrowableProxy)) {
/* 138 */         values[(i++)] = ((ThrowableProxy)iThrowableProxy).getThrowable();
/*     */       } else {
/* 140 */         values[(i++)] = null;
/*     */       }
/*     */     } else {
/* 143 */       values[(i++)] = null;
/* 144 */       values[(i++)] = null;
/*     */     }
/*     */     
/* 147 */     for (int j = 0; j < matcherListSize; j++) {
/* 148 */       values[(i++)] = ((Matcher)this.matcherList.get(j));
/*     */     }
/*     */     
/* 151 */     return values;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\boolex\JaninoEventEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */