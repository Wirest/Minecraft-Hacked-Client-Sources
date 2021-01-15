/*     */ package ch.qos.logback.core.pattern;
/*     */ 
/*     */ import ch.qos.logback.core.Context;
/*     */ import ch.qos.logback.core.spi.ContextAware;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.spi.LifeCycle;
/*     */ import ch.qos.logback.core.status.Status;
/*     */ import java.util.List;
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
/*     */ public abstract class DynamicConverter<E>
/*     */   extends FormattingConverter<E>
/*     */   implements LifeCycle, ContextAware
/*     */ {
/*  27 */   ContextAwareBase cab = new ContextAwareBase(this);
/*     */   
/*     */ 
/*     */ 
/*     */   private List<String> optionList;
/*     */   
/*     */ 
/*     */ 
/*  35 */   protected boolean started = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/*  44 */     this.started = true;
/*     */   }
/*     */   
/*     */   public void stop() {
/*  48 */     this.started = false;
/*     */   }
/*     */   
/*     */   public boolean isStarted() {
/*  52 */     return this.started;
/*     */   }
/*     */   
/*     */   public void setOptionList(List<String> optionList) {
/*  56 */     this.optionList = optionList;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFirstOption()
/*     */   {
/*  66 */     if ((this.optionList == null) || (this.optionList.size() == 0)) {
/*  67 */       return null;
/*     */     }
/*  69 */     return (String)this.optionList.get(0);
/*     */   }
/*     */   
/*     */   protected List<String> getOptionList()
/*     */   {
/*  74 */     return this.optionList;
/*     */   }
/*     */   
/*     */   public void setContext(Context context) {
/*  78 */     this.cab.setContext(context);
/*     */   }
/*     */   
/*     */   public Context getContext() {
/*  82 */     return this.cab.getContext();
/*     */   }
/*     */   
/*     */   public void addStatus(Status status) {
/*  86 */     this.cab.addStatus(status);
/*     */   }
/*     */   
/*     */   public void addInfo(String msg) {
/*  90 */     this.cab.addInfo(msg);
/*     */   }
/*     */   
/*     */   public void addInfo(String msg, Throwable ex) {
/*  94 */     this.cab.addInfo(msg, ex);
/*     */   }
/*     */   
/*     */   public void addWarn(String msg) {
/*  98 */     this.cab.addWarn(msg);
/*     */   }
/*     */   
/*     */   public void addWarn(String msg, Throwable ex) {
/* 102 */     this.cab.addWarn(msg, ex);
/*     */   }
/*     */   
/*     */   public void addError(String msg) {
/* 106 */     this.cab.addError(msg);
/*     */   }
/*     */   
/*     */   public void addError(String msg, Throwable ex) {
/* 110 */     this.cab.addError(msg, ex);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\pattern\DynamicConverter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */