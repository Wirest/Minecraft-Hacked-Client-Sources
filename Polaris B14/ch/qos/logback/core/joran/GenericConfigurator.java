/*     */ package ch.qos.logback.core.joran;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.joran.event.SaxEvent;
/*     */ import ch.qos.logback.core.joran.event.SaxEventRecorder;
/*     */ import ch.qos.logback.core.joran.spi.DefaultNestedComponentRegistry;
/*     */ import ch.qos.logback.core.joran.spi.ElementPath;
/*     */ import ch.qos.logback.core.joran.spi.EventPlayer;
/*     */ import ch.qos.logback.core.joran.spi.InterpretationContext;
/*     */ import ch.qos.logback.core.joran.spi.Interpreter;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import ch.qos.logback.core.joran.spi.RuleStore;
/*     */ import ch.qos.logback.core.joran.spi.SimpleRuleStore;
/*     */ import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.status.StatusUtil;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.List;
/*     */ import org.xml.sax.InputSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GenericConfigurator
/*     */   extends ContextAwareBase
/*     */ {
/*     */   protected Interpreter interpreter;
/*     */   
/*     */   public final void doConfigure(URL url)
/*     */     throws JoranException
/*     */   {
/*  40 */     InputStream in = null;
/*     */     try {
/*  42 */       informContextOfURLUsedForConfiguration(getContext(), url);
/*  43 */       URLConnection urlConnection = url.openConnection();
/*     */       
/*     */ 
/*  46 */       urlConnection.setUseCaches(false);
/*     */       
/*  48 */       in = urlConnection.getInputStream();
/*  49 */       doConfigure(in);
/*     */       String errMsg;
/*     */       String errMsg; String errMsg; return; } catch (IOException ioe) { errMsg = "Could not open URL [" + url + "].";
/*  52 */       addError(errMsg, ioe);
/*  53 */       throw new JoranException(errMsg, ioe);
/*     */     } finally {
/*  55 */       if (in != null) {
/*     */         try {
/*  57 */           in.close();
/*     */         } catch (IOException ioe) {
/*  59 */           errMsg = "Could not close input stream";
/*  60 */           addError(errMsg, ioe);
/*  61 */           throw new JoranException(errMsg, ioe);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public final void doConfigure(String filename) throws JoranException {
/*  68 */     doConfigure(new File(filename));
/*     */   }
/*     */   
/*     */   public final void doConfigure(File file) throws JoranException {
/*  72 */     FileInputStream fis = null;
/*     */     try {
/*  74 */       informContextOfURLUsedForConfiguration(getContext(), file.toURI().toURL());
/*  75 */       fis = new FileInputStream(file);
/*  76 */       doConfigure(fis);
/*     */       String errMsg;
/*     */       String errMsg; String errMsg; return; } catch (IOException ioe) { errMsg = "Could not open [" + file.getPath() + "].";
/*  79 */       addError(errMsg, ioe);
/*  80 */       throw new JoranException(errMsg, ioe);
/*     */     } finally {
/*  82 */       if (fis != null) {
/*     */         try {
/*  84 */           fis.close();
/*     */         } catch (IOException ioe) {
/*  86 */           errMsg = "Could not close [" + file.getName() + "].";
/*  87 */           addError(errMsg, ioe);
/*  88 */           throw new JoranException(errMsg, ioe);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void informContextOfURLUsedForConfiguration(Context context, URL url) {
/*  95 */     ConfigurationWatchListUtil.setMainWatchURL(context, url);
/*     */   }
/*     */   
/*     */   public final void doConfigure(InputStream inputStream) throws JoranException {
/*  99 */     doConfigure(new InputSource(inputStream));
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract void addInstanceRules(RuleStore paramRuleStore);
/*     */   
/*     */   protected abstract void addImplicitRules(Interpreter paramInterpreter);
/*     */   
/*     */   protected void addDefaultNestedComponentRegistryRules(DefaultNestedComponentRegistry registry) {}
/*     */   
/*     */   protected ElementPath initialElementPath()
/*     */   {
/* 111 */     return new ElementPath();
/*     */   }
/*     */   
/*     */   protected void buildInterpreter() {
/* 115 */     RuleStore rs = new SimpleRuleStore(this.context);
/* 116 */     addInstanceRules(rs);
/* 117 */     this.interpreter = new Interpreter(this.context, rs, initialElementPath());
/* 118 */     InterpretationContext interpretationContext = this.interpreter.getInterpretationContext();
/* 119 */     interpretationContext.setContext(this.context);
/* 120 */     addImplicitRules(this.interpreter);
/* 121 */     addDefaultNestedComponentRegistryRules(interpretationContext.getDefaultNestedComponentRegistry());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final void doConfigure(InputSource inputSource)
/*     */     throws JoranException
/*     */   {
/* 129 */     long threshold = System.currentTimeMillis();
/* 130 */     if (!ConfigurationWatchListUtil.wasConfigurationWatchListReset(this.context)) {
/* 131 */       informContextOfURLUsedForConfiguration(getContext(), null);
/*     */     }
/* 133 */     SaxEventRecorder recorder = new SaxEventRecorder(this.context);
/* 134 */     recorder.recordEvents(inputSource);
/* 135 */     doConfigure(recorder.saxEventList);
/*     */     
/* 137 */     StatusUtil statusUtil = new StatusUtil(this.context);
/* 138 */     if (statusUtil.noXMLParsingErrorsOccurred(threshold)) {
/* 139 */       addInfo("Registering current configuration as safe fallback point");
/* 140 */       registerSafeConfiguration();
/*     */     }
/*     */   }
/*     */   
/*     */   public void doConfigure(List<SaxEvent> eventList) throws JoranException
/*     */   {
/* 146 */     buildInterpreter();
/*     */     
/* 148 */     synchronized (this.context.getConfigurationLock()) {
/* 149 */       this.interpreter.getEventPlayer().play(eventList);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerSafeConfiguration()
/*     */   {
/* 160 */     this.context.putObject("SAFE_JORAN_CONFIGURATION", this.interpreter.getEventPlayer().getCopyOfPlayerEventList());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<SaxEvent> recallSafeConfiguration()
/*     */   {
/* 167 */     return (List)this.context.getObject("SAFE_JORAN_CONFIGURATION");
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\joran\GenericConfigurator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */