/*     */ package ch.qos.logback.core.sift;
/*     */ 
/*     */ import ch.qos.logback.core.Appender;
/*     */ import ch.qos.logback.core.AppenderBase;
/*     */ import ch.qos.logback.core.util.Duration;
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
/*     */ public abstract class SiftingAppenderBase<E>
/*     */   extends AppenderBase<E>
/*     */ {
/*     */   protected AppenderTracker<E> appenderTracker;
/*     */   AppenderFactory<E> appenderFactory;
/*  35 */   Duration timeout = new Duration(1800000L);
/*  36 */   int maxAppenderCount = Integer.MAX_VALUE;
/*     */   Discriminator<E> discriminator;
/*     */   
/*     */   public Duration getTimeout()
/*     */   {
/*  41 */     return this.timeout;
/*     */   }
/*     */   
/*     */   public void setTimeout(Duration timeout) {
/*  45 */     this.timeout = timeout;
/*     */   }
/*     */   
/*     */   public int getMaxAppenderCount() {
/*  49 */     return this.maxAppenderCount;
/*     */   }
/*     */   
/*     */   public void setMaxAppenderCount(int maxAppenderCount) {
/*  53 */     this.maxAppenderCount = maxAppenderCount;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAppenderFactory(AppenderFactory<E> appenderFactory)
/*     */   {
/*  61 */     this.appenderFactory = appenderFactory;
/*     */   }
/*     */   
/*     */   public void start()
/*     */   {
/*  66 */     int errors = 0;
/*  67 */     if (this.discriminator == null) {
/*  68 */       addError("Missing discriminator. Aborting");
/*  69 */       errors++;
/*     */     }
/*  71 */     if (!this.discriminator.isStarted()) {
/*  72 */       addError("Discriminator has not started successfully. Aborting");
/*  73 */       errors++;
/*     */     }
/*  75 */     if (this.appenderFactory == null) {
/*  76 */       addError("AppenderFactory has not been set. Aborting");
/*  77 */       errors++;
/*     */     } else {
/*  79 */       this.appenderTracker = new AppenderTracker(this.context, this.appenderFactory);
/*  80 */       this.appenderTracker.setMaxComponents(this.maxAppenderCount);
/*  81 */       this.appenderTracker.setTimeout(this.timeout.getMilliseconds());
/*     */     }
/*  83 */     if (errors == 0) {
/*  84 */       super.start();
/*     */     }
/*     */   }
/*     */   
/*     */   public void stop()
/*     */   {
/*  90 */     for (Appender<E> appender : this.appenderTracker.allComponents()) {
/*  91 */       appender.stop();
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract long getTimestamp(E paramE);
/*     */   
/*     */   protected void append(E event)
/*     */   {
/*  99 */     if (!isStarted()) {
/* 100 */       return;
/*     */     }
/* 102 */     String discriminatingValue = this.discriminator.getDiscriminatingValue(event);
/* 103 */     long timestamp = getTimestamp(event);
/*     */     
/* 105 */     Appender<E> appender = (Appender)this.appenderTracker.getOrCreate(discriminatingValue, timestamp);
/*     */     
/* 107 */     if (eventMarksEndOfLife(event)) {
/* 108 */       this.appenderTracker.endOfLife(discriminatingValue);
/*     */     }
/* 110 */     this.appenderTracker.removeStaleComponents(timestamp);
/* 111 */     appender.doAppend(event);
/*     */   }
/*     */   
/*     */   protected abstract boolean eventMarksEndOfLife(E paramE);
/*     */   
/*     */   public Discriminator<E> getDiscriminator() {
/* 117 */     return this.discriminator;
/*     */   }
/*     */   
/*     */   public void setDiscriminator(Discriminator<E> discriminator) {
/* 121 */     this.discriminator = discriminator;
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
/*     */   public AppenderTracker<E> getAppenderTracker()
/*     */   {
/* 135 */     return this.appenderTracker;
/*     */   }
/*     */   
/*     */   public String getDiscriminatorKey() {
/* 139 */     if (this.discriminator != null) {
/* 140 */       return this.discriminator.getKey();
/*     */     }
/* 142 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\sift\SiftingAppenderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */