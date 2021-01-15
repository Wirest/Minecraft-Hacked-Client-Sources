/*     */ package ch.qos.logback.classic.net;
/*     */ 
/*     */ import ch.qos.logback.classic.PatternLayout;
/*     */ import ch.qos.logback.classic.pattern.SyslogStartConverter;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.classic.spi.IThrowableProxy;
/*     */ import ch.qos.logback.classic.spi.StackTraceElementProxy;
/*     */ import ch.qos.logback.classic.util.LevelToSyslogSeverity;
/*     */ import ch.qos.logback.core.Layout;
/*     */ import ch.qos.logback.core.net.SyslogAppenderBase;
/*     */ import ch.qos.logback.core.net.SyslogOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
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
/*     */ 
/*     */ 
/*     */ public class SyslogAppender
/*     */   extends SyslogAppenderBase<ILoggingEvent>
/*     */ {
/*     */   public static final String DEFAULT_SUFFIX_PATTERN = "[%thread] %logger %msg";
/*     */   public static final String DEFAULT_STACKTRACE_PATTERN = "\t";
/*  44 */   PatternLayout stackTraceLayout = new PatternLayout();
/*  45 */   String stackTracePattern = "\t";
/*     */   
/*  47 */   boolean throwableExcluded = false;
/*     */   
/*     */   public void start()
/*     */   {
/*  51 */     super.start();
/*  52 */     setupStackTraceLayout();
/*     */   }
/*     */   
/*     */   String getPrefixPattern() {
/*  56 */     return "%syslogStart{" + getFacility() + "}%nopex{}";
/*     */   }
/*     */   
/*     */   public SyslogOutputStream createOutputStream() throws SocketException, UnknownHostException
/*     */   {
/*  61 */     return new SyslogOutputStream(getSyslogHost(), getPort());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getSeverityForEvent(Object eventObject)
/*     */   {
/*  72 */     ILoggingEvent event = (ILoggingEvent)eventObject;
/*  73 */     return LevelToSyslogSeverity.convert(event);
/*     */   }
/*     */   
/*     */   protected void postProcess(Object eventObject, OutputStream sw)
/*     */   {
/*  78 */     if (this.throwableExcluded) {
/*  79 */       return;
/*     */     }
/*  81 */     ILoggingEvent event = (ILoggingEvent)eventObject;
/*  82 */     IThrowableProxy tp = event.getThrowableProxy();
/*     */     
/*  84 */     if (tp == null) {
/*  85 */       return;
/*     */     }
/*  87 */     String stackTracePrefix = this.stackTraceLayout.doLayout(event);
/*  88 */     boolean isRootException = true;
/*  89 */     while (tp != null) {
/*  90 */       StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
/*     */       try {
/*  92 */         handleThrowableFirstLine(sw, tp, stackTracePrefix, isRootException);
/*  93 */         isRootException = false;
/*  94 */         for (StackTraceElementProxy step : stepArray) {
/*  95 */           StringBuilder sb = new StringBuilder();
/*  96 */           sb.append(stackTracePrefix).append(step);
/*  97 */           sw.write(sb.toString().getBytes());
/*  98 */           sw.flush();
/*     */         }
/*     */       } catch (IOException e) {
/*     */         break;
/*     */       }
/* 103 */       tp = tp.getCause();
/*     */     }
/*     */   }
/*     */   
/*     */   private void handleThrowableFirstLine(OutputStream sw, IThrowableProxy tp, String stackTracePrefix, boolean isRootException) throws IOException
/*     */   {
/* 109 */     StringBuilder sb = new StringBuilder().append(stackTracePrefix);
/*     */     
/* 111 */     if (!isRootException) {
/* 112 */       sb.append("Caused by: ");
/*     */     }
/* 114 */     sb.append(tp.getClassName()).append(": ").append(tp.getMessage());
/* 115 */     sw.write(sb.toString().getBytes());
/* 116 */     sw.flush();
/*     */   }
/*     */   
/*     */   boolean stackTraceHeaderLine(StringBuilder sb, boolean topException)
/*     */   {
/* 121 */     return false;
/*     */   }
/*     */   
/*     */   public Layout<ILoggingEvent> buildLayout() {
/* 125 */     PatternLayout layout = new PatternLayout();
/* 126 */     layout.getInstanceConverterMap().put("syslogStart", SyslogStartConverter.class.getName());
/*     */     
/* 128 */     if (this.suffixPattern == null) {
/* 129 */       this.suffixPattern = "[%thread] %logger %msg";
/*     */     }
/* 131 */     layout.setPattern(getPrefixPattern() + this.suffixPattern);
/* 132 */     layout.setContext(getContext());
/* 133 */     layout.start();
/* 134 */     return layout;
/*     */   }
/*     */   
/*     */   private void setupStackTraceLayout() {
/* 138 */     this.stackTraceLayout.getInstanceConverterMap().put("syslogStart", SyslogStartConverter.class.getName());
/*     */     
/*     */ 
/* 141 */     this.stackTraceLayout.setPattern(getPrefixPattern() + this.stackTracePattern);
/* 142 */     this.stackTraceLayout.setContext(getContext());
/* 143 */     this.stackTraceLayout.start();
/*     */   }
/*     */   
/*     */   public boolean isThrowableExcluded() {
/* 147 */     return this.throwableExcluded;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setThrowableExcluded(boolean throwableExcluded)
/*     */   {
/* 158 */     this.throwableExcluded = throwableExcluded;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getStackTracePattern()
/*     */   {
/* 168 */     return this.stackTracePattern;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setStackTracePattern(String stackTracePattern)
/*     */   {
/* 181 */     this.stackTracePattern = stackTracePattern;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\net\SyslogAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */