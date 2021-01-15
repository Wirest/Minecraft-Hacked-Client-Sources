/*     */ package ch.qos.logback.core;
/*     */ 
/*     */ import ch.qos.logback.core.filter.Filter;
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import ch.qos.logback.core.spi.FilterAttachableImpl;
/*     */ import ch.qos.logback.core.spi.FilterReply;
/*     */ import ch.qos.logback.core.status.WarnStatus;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class UnsynchronizedAppenderBase<E>
/*     */   extends ContextAwareBase
/*     */   implements Appender<E>
/*     */ {
/*  34 */   protected boolean started = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  43 */   private ThreadLocal<Boolean> guard = new ThreadLocal();
/*     */   
/*     */ 
/*     */ 
/*     */   protected String name;
/*     */   
/*     */ 
/*     */ 
/*  51 */   private FilterAttachableImpl<E> fai = new FilterAttachableImpl();
/*     */   
/*     */   public String getName() {
/*  54 */     return this.name;
/*     */   }
/*     */   
/*  57 */   private int statusRepeatCount = 0;
/*  58 */   private int exceptionCount = 0;
/*     */   
/*     */ 
/*     */   static final int ALLOWED_REPEATS = 3;
/*     */   
/*     */ 
/*     */ 
/*     */   public void doAppend(E eventObject)
/*     */   {
/*  67 */     if (Boolean.TRUE.equals(this.guard.get())) {
/*  68 */       return;
/*     */     }
/*     */     try
/*     */     {
/*  72 */       this.guard.set(Boolean.TRUE);
/*     */       
/*  74 */       if (!this.started) {
/*  75 */         if (this.statusRepeatCount++ < 3) {
/*  76 */           addStatus(new WarnStatus("Attempted to append to non started appender [" + this.name + "].", this));
/*     */         }
/*     */         
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*  83 */         if (getFilterChainDecision(eventObject) == FilterReply.DENY) {
/*     */           return;
/*     */         }
/*     */         
/*     */ 
/*  88 */         append(eventObject);
/*     */       }
/*     */     } catch (Exception e) {
/*  91 */       if (this.exceptionCount++ < 3) {
/*  92 */         addError("Appender [" + this.name + "] failed to append.", e);
/*     */       }
/*     */     } finally {
/*  95 */       this.guard.set(Boolean.FALSE);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract void append(E paramE);
/*     */   
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/* 105 */     this.name = name;
/*     */   }
/*     */   
/*     */   public void start() {
/* 109 */     this.started = true;
/*     */   }
/*     */   
/*     */   public void stop() {
/* 113 */     this.started = false;
/*     */   }
/*     */   
/*     */   public boolean isStarted() {
/* 117 */     return this.started;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 121 */     return getClass().getName() + "[" + this.name + "]";
/*     */   }
/*     */   
/*     */   public void addFilter(Filter<E> newFilter) {
/* 125 */     this.fai.addFilter(newFilter);
/*     */   }
/*     */   
/*     */   public void clearAllFilters() {
/* 129 */     this.fai.clearAllFilters();
/*     */   }
/*     */   
/*     */   public List<Filter<E>> getCopyOfAttachedFiltersList() {
/* 133 */     return this.fai.getCopyOfAttachedFiltersList();
/*     */   }
/*     */   
/*     */   public FilterReply getFilterChainDecision(E event) {
/* 137 */     return this.fai.getFilterChainDecision(event);
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\UnsynchronizedAppenderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */