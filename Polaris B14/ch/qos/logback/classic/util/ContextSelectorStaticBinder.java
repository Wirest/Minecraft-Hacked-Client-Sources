/*     */ package ch.qos.logback.classic.util;
/*     */ 
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.selector.ContextJNDISelector;
/*     */ import ch.qos.logback.classic.selector.ContextSelector;
/*     */ import ch.qos.logback.classic.selector.DefaultContextSelector;
/*     */ import ch.qos.logback.core.util.Loader;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
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
/*     */ public class ContextSelectorStaticBinder
/*     */ {
/*  35 */   static ContextSelectorStaticBinder singleton = new ContextSelectorStaticBinder();
/*     */   ContextSelector contextSelector;
/*     */   Object key;
/*     */   
/*     */   public static ContextSelectorStaticBinder getSingleton()
/*     */   {
/*  41 */     return singleton;
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
/*     */ 
/*     */ 
/*     */   public void init(LoggerContext defaultLoggerContext, Object key)
/*     */     throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException
/*     */   {
/*  57 */     if (this.key == null) {
/*  58 */       this.key = key;
/*  59 */     } else if (this.key != key) {
/*  60 */       throw new IllegalAccessException("Only certain classes can access this method.");
/*     */     }
/*     */     
/*     */ 
/*  64 */     String contextSelectorStr = OptionHelper.getSystemProperty("logback.ContextSelector");
/*     */     
/*  66 */     if (contextSelectorStr == null) {
/*  67 */       this.contextSelector = new DefaultContextSelector(defaultLoggerContext);
/*  68 */     } else if (contextSelectorStr.equals("JNDI"))
/*     */     {
/*  70 */       this.contextSelector = new ContextJNDISelector(defaultLoggerContext);
/*     */     } else {
/*  72 */       this.contextSelector = dynamicalContextSelector(defaultLoggerContext, contextSelectorStr);
/*     */     }
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
/*     */   static ContextSelector dynamicalContextSelector(LoggerContext defaultLoggerContext, String contextSelectorStr)
/*     */     throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException
/*     */   {
/*  97 */     Class<?> contextSelectorClass = Loader.loadClass(contextSelectorStr);
/*  98 */     Constructor cons = contextSelectorClass.getConstructor(new Class[] { LoggerContext.class });
/*     */     
/* 100 */     return (ContextSelector)cons.newInstance(new Object[] { defaultLoggerContext });
/*     */   }
/*     */   
/*     */   public ContextSelector getContextSelector() {
/* 104 */     return this.contextSelector;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\util\ContextSelectorStaticBinder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */