/*     */ package org.slf4j.impl;
/*     */ 
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.selector.ContextSelector;
/*     */ import ch.qos.logback.classic.util.ContextInitializer;
/*     */ import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import ch.qos.logback.core.status.StatusUtil;
/*     */ import ch.qos.logback.core.util.StatusPrinter;
/*     */ import org.slf4j.ILoggerFactory;
/*     */ import org.slf4j.helpers.Util;
/*     */ import org.slf4j.spi.LoggerFactoryBinder;
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
/*     */ public class StaticLoggerBinder
/*     */   implements LoggerFactoryBinder
/*     */ {
/*  43 */   public static String REQUESTED_API_VERSION = "1.6";
/*     */   
/*     */ 
/*     */ 
/*     */   static final String NULL_CS_URL = "http://logback.qos.ch/codes.html#null_CS";
/*     */   
/*     */ 
/*  50 */   private static StaticLoggerBinder SINGLETON = new StaticLoggerBinder();
/*     */   
/*  52 */   private static Object KEY = new Object();
/*     */   
/*     */   static {
/*  55 */     SINGLETON.init();
/*     */   }
/*     */   
/*  58 */   private boolean initialized = false;
/*  59 */   private LoggerContext defaultLoggerContext = new LoggerContext();
/*  60 */   private final ContextSelectorStaticBinder contextSelectorBinder = ContextSelectorStaticBinder.getSingleton();
/*     */   
/*     */   private StaticLoggerBinder()
/*     */   {
/*  64 */     this.defaultLoggerContext.setName("default");
/*     */   }
/*     */   
/*     */   public static StaticLoggerBinder getSingleton() {
/*  68 */     return SINGLETON;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static void reset()
/*     */   {
/*  75 */     SINGLETON = new StaticLoggerBinder();
/*  76 */     SINGLETON.init();
/*     */   }
/*     */   
/*     */   void init()
/*     */   {
/*     */     try
/*     */     {
/*     */       try
/*     */       {
/*  85 */         new ContextInitializer(this.defaultLoggerContext).autoConfig();
/*     */       } catch (JoranException je) {
/*  87 */         Util.report("Failed to auto configure default logger context", je);
/*     */       }
/*     */       
/*  90 */       if (!StatusUtil.contextHasStatusListener(this.defaultLoggerContext)) {
/*  91 */         StatusPrinter.printInCaseOfErrorsOrWarnings(this.defaultLoggerContext);
/*     */       }
/*  93 */       this.contextSelectorBinder.init(this.defaultLoggerContext, KEY);
/*  94 */       this.initialized = true;
/*     */     }
/*     */     catch (Throwable t) {
/*  97 */       Util.report("Failed to instantiate [" + LoggerContext.class.getName() + "]", t);
/*     */     }
/*     */   }
/*     */   
/*     */   public ILoggerFactory getLoggerFactory()
/*     */   {
/* 103 */     if (!this.initialized) {
/* 104 */       return this.defaultLoggerContext;
/*     */     }
/*     */     
/* 107 */     if (this.contextSelectorBinder.getContextSelector() == null) {
/* 108 */       throw new IllegalStateException("contextSelector cannot be null. See also http://logback.qos.ch/codes.html#null_CS");
/*     */     }
/*     */     
/* 111 */     return this.contextSelectorBinder.getContextSelector().getLoggerContext();
/*     */   }
/*     */   
/*     */   public String getLoggerFactoryClassStr() {
/* 115 */     return this.contextSelectorBinder.getClass().getName();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\org\slf4j\impl\StaticLoggerBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */