/*     */ package ch.qos.logback.classic.net;
/*     */ 
/*     */ import ch.qos.logback.classic.ClassicConstants;
/*     */ import ch.qos.logback.classic.PatternLayout;
/*     */ import ch.qos.logback.classic.boolex.OnErrorEvaluator;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.core.Layout;
/*     */ import ch.qos.logback.core.boolex.EventEvaluator;
/*     */ import ch.qos.logback.core.helpers.CyclicBuffer;
/*     */ import ch.qos.logback.core.net.SMTPAppenderBase;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SMTPAppender
/*     */   extends SMTPAppenderBase<ILoggingEvent>
/*     */ {
/*     */   static final String DEFAULT_SUBJECT_PATTERN = "%logger{20} - %m";
/*  42 */   private int bufferSize = 512;
/*  43 */   private boolean includeCallerData = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SMTPAppender() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/*  57 */     if (this.eventEvaluator == null) {
/*  58 */       OnErrorEvaluator onError = new OnErrorEvaluator();
/*  59 */       onError.setContext(getContext());
/*  60 */       onError.setName("onError");
/*  61 */       onError.start();
/*  62 */       this.eventEvaluator = onError;
/*     */     }
/*  64 */     super.start();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SMTPAppender(EventEvaluator<ILoggingEvent> eventEvaluator)
/*     */   {
/*  72 */     this.eventEvaluator = eventEvaluator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void subAppend(CyclicBuffer<ILoggingEvent> cb, ILoggingEvent event)
/*     */   {
/*  80 */     if (this.includeCallerData) {
/*  81 */       event.getCallerData();
/*     */     }
/*  83 */     event.prepareForDeferredProcessing();
/*  84 */     cb.add(event);
/*     */   }
/*     */   
/*     */   protected void fillBuffer(CyclicBuffer<ILoggingEvent> cb, StringBuffer sbuf)
/*     */   {
/*  89 */     int len = cb.length();
/*  90 */     for (int i = 0; i < len; i++) {
/*  91 */       ILoggingEvent event = (ILoggingEvent)cb.get();
/*  92 */       sbuf.append(this.layout.doLayout(event));
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean eventMarksEndOfLife(ILoggingEvent eventObject) {
/*  97 */     Marker marker = eventObject.getMarker();
/*  98 */     if (marker == null) {
/*  99 */       return false;
/*     */     }
/* 101 */     return marker.contains(ClassicConstants.FINALIZE_SESSION_MARKER);
/*     */   }
/*     */   
/*     */ 
/*     */   protected Layout<ILoggingEvent> makeSubjectLayout(String subjectStr)
/*     */   {
/* 107 */     if (subjectStr == null) {
/* 108 */       subjectStr = "%logger{20} - %m";
/*     */     }
/* 110 */     PatternLayout pl = new PatternLayout();
/* 111 */     pl.setContext(getContext());
/* 112 */     pl.setPattern(subjectStr);
/*     */     
/*     */ 
/*     */ 
/* 116 */     pl.setPostCompileProcessor(null);
/* 117 */     pl.start();
/* 118 */     return pl;
/*     */   }
/*     */   
/*     */   protected PatternLayout makeNewToPatternLayout(String toPattern)
/*     */   {
/* 123 */     PatternLayout pl = new PatternLayout();
/* 124 */     pl.setPattern(toPattern + "%nopex");
/* 125 */     return pl;
/*     */   }
/*     */   
/*     */   public boolean isIncludeCallerData() {
/* 129 */     return this.includeCallerData;
/*     */   }
/*     */   
/*     */   public void setIncludeCallerData(boolean includeCallerData) {
/* 133 */     this.includeCallerData = includeCallerData;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\net\SMTPAppender.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */