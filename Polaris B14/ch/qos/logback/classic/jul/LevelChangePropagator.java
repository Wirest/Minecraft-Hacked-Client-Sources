/*     */ package ch.qos.logback.classic.jul;
/*     */ 
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.spi.LoggerContextListener;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.spi.LifeCycle;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.logging.LogManager;
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
/*     */ public class LevelChangePropagator
/*     */   extends ContextAwareBase
/*     */   implements LoggerContextListener, LifeCycle
/*     */ {
/*  35 */   private Set julLoggerSet = new HashSet();
/*  36 */   boolean isStarted = false;
/*  37 */   boolean resetJUL = false;
/*     */   
/*     */   public void setResetJUL(boolean resetJUL) {
/*  40 */     this.resetJUL = resetJUL;
/*     */   }
/*     */   
/*     */   public boolean isResetResistant() {
/*  44 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onStart(LoggerContext context) {}
/*     */   
/*     */ 
/*     */   public void onReset(LoggerContext context) {}
/*     */   
/*     */   public void onStop(LoggerContext context) {}
/*     */   
/*     */   public void onLevelChange(ch.qos.logback.classic.Logger logger, ch.qos.logback.classic.Level level)
/*     */   {
/*  57 */     propagate(logger, level);
/*     */   }
/*     */   
/*     */   private void propagate(ch.qos.logback.classic.Logger logger, ch.qos.logback.classic.Level level) {
/*  61 */     addInfo("Propagating " + level + " level on " + logger + " onto the JUL framework");
/*  62 */     java.util.logging.Logger julLogger = JULHelper.asJULLogger(logger);
/*     */     
/*     */ 
/*  65 */     this.julLoggerSet.add(julLogger);
/*  66 */     java.util.logging.Level julLevel = JULHelper.asJULLevel(level);
/*  67 */     julLogger.setLevel(julLevel);
/*     */   }
/*     */   
/*     */   public void resetJULLevels() {
/*  71 */     LogManager lm = LogManager.getLogManager();
/*     */     
/*  73 */     Enumeration e = lm.getLoggerNames();
/*  74 */     while (e.hasMoreElements()) {
/*  75 */       String loggerName = (String)e.nextElement();
/*  76 */       java.util.logging.Logger julLogger = lm.getLogger(loggerName);
/*  77 */       if ((JULHelper.isRegularNonRootLogger(julLogger)) && (julLogger.getLevel() != null)) {
/*  78 */         addInfo("Setting level of jul logger [" + loggerName + "] to null");
/*  79 */         julLogger.setLevel(null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void propagateExistingLoggerLevels() {
/*  85 */     LoggerContext loggerContext = (LoggerContext)this.context;
/*  86 */     List<ch.qos.logback.classic.Logger> loggerList = loggerContext.getLoggerList();
/*  87 */     for (ch.qos.logback.classic.Logger l : loggerList) {
/*  88 */       if (l.getLevel() != null) {
/*  89 */         propagate(l, l.getLevel());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void start() {
/*  95 */     if (this.resetJUL) {
/*  96 */       resetJULLevels();
/*     */     }
/*  98 */     propagateExistingLoggerLevels();
/*     */     
/* 100 */     this.isStarted = true;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 104 */     this.isStarted = false;
/*     */   }
/*     */   
/*     */   public boolean isStarted() {
/* 108 */     return this.isStarted;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\jul\LevelChangePropagator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */