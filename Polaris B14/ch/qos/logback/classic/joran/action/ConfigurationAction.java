/*     */ package ch.qos.logback.classic.joran.action;
/*     */ 
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.turbo.ReconfigureOnChangeFilter;
/*     */ import ch.qos.logback.classic.util.EnvUtil;
/*     */ import ch.qos.logback.core.joran.action.Action;
/*     */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*     */ import ch.qos.logback.core.status.OnConsoleStatusListener;
/*     */ import ch.qos.logback.core.util.ContextUtil;
/*     */ import ch.qos.logback.core.util.Duration;
/*     */ import ch.qos.logback.core.util.OptionHelper;
/*     */ import org.xml.sax.Attributes;
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
/*     */ public class ConfigurationAction
/*     */   extends Action
/*     */ {
/*     */   static final String INTERNAL_DEBUG_ATTR = "debug";
/*     */   static final String SCAN_ATTR = "scan";
/*     */   static final String SCAN_PERIOD_ATTR = "scanPeriod";
/*     */   static final String DEBUG_SYSTEM_PROPERTY_KEY = "logback.debug";
/*  34 */   long threshold = 0L;
/*     */   
/*     */   public void begin(InterpretationContext ic, String name, Attributes attributes) {
/*  37 */     this.threshold = System.currentTimeMillis();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  42 */     String debugAttrib = getSystemProperty("logback.debug");
/*  43 */     if (debugAttrib == null) {
/*  44 */       debugAttrib = ic.subst(attributes.getValue("debug"));
/*     */     }
/*     */     
/*  47 */     if ((OptionHelper.isEmpty(debugAttrib)) || (debugAttrib.equalsIgnoreCase("false")) || (debugAttrib.equalsIgnoreCase("null")))
/*     */     {
/*  49 */       addInfo("debug attribute not set");
/*     */     } else {
/*  51 */       OnConsoleStatusListener.addNewInstanceToContext(this.context);
/*     */     }
/*     */     
/*  54 */     processScanAttrib(ic, attributes);
/*     */     
/*  56 */     ContextUtil contextUtil = new ContextUtil(this.context);
/*  57 */     contextUtil.addHostNameAsProperty();
/*     */     
/*  59 */     if (EnvUtil.isGroovyAvailable()) {
/*  60 */       LoggerContext lc = (LoggerContext)this.context;
/*  61 */       contextUtil.addGroovyPackages(lc.getFrameworkPackages());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  66 */     ic.pushObject(getContext());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   String getSystemProperty(String name)
/*     */   {
/*     */     try
/*     */     {
/*  75 */       return System.getProperty(name);
/*     */     } catch (SecurityException ex) {}
/*  77 */     return null;
/*     */   }
/*     */   
/*     */   void processScanAttrib(InterpretationContext ic, Attributes attributes)
/*     */   {
/*  82 */     String scanAttrib = ic.subst(attributes.getValue("scan"));
/*  83 */     if ((!OptionHelper.isEmpty(scanAttrib)) && (!"false".equalsIgnoreCase(scanAttrib)))
/*     */     {
/*  85 */       ReconfigureOnChangeFilter rocf = new ReconfigureOnChangeFilter();
/*  86 */       rocf.setContext(this.context);
/*  87 */       String scanPeriodAttrib = ic.subst(attributes.getValue("scanPeriod"));
/*  88 */       if (!OptionHelper.isEmpty(scanPeriodAttrib)) {
/*     */         try {
/*  90 */           Duration duration = Duration.valueOf(scanPeriodAttrib);
/*  91 */           rocf.setRefreshPeriod(duration.getMilliseconds());
/*  92 */           addInfo("Setting ReconfigureOnChangeFilter scanning period to " + duration);
/*     */         }
/*     */         catch (NumberFormatException nfe) {
/*  95 */           addError("Error while converting [" + scanAttrib + "] to long", nfe);
/*     */         }
/*     */       }
/*  98 */       rocf.start();
/*  99 */       LoggerContext lc = (LoggerContext)this.context;
/* 100 */       addInfo("Adding ReconfigureOnChangeFilter as a turbo filter");
/* 101 */       lc.addTurboFilter(rocf);
/*     */     }
/*     */   }
/*     */   
/*     */   public void end(InterpretationContext ec, String name) {
/* 106 */     addInfo("End of configuration.");
/* 107 */     ec.popObject();
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\classic\joran\action\ConfigurationAction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */