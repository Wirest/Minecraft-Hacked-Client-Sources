/*     */ package ch.qos.logback.core;
/*     */ 
/*     */ import ch.qos.logback.core.spi.AppenderAttachable;
/*     */ import ch.qos.logback.core.spi.AppenderAttachableImpl;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ import java.util.concurrent.BlockingQueue;
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
/*     */ public class AsyncAppenderBase<E>
/*     */   extends UnsynchronizedAppenderBase<E>
/*     */   implements AppenderAttachable<E>
/*     */ {
/*     */   AppenderAttachableImpl<E> aai;
/*     */   BlockingQueue<E> blockingQueue;
/*     */   public static final int DEFAULT_QUEUE_SIZE = 256;
/*     */   int queueSize;
/*     */   int appenderCount;
/*     */   static final int UNDEFINED = -1;
/*     */   int discardingThreshold;
/*     */   AsyncAppenderBase<E>.Worker worker;
/*     */   public static final int DEFAULT_MAX_FLUSH_TIME = 1000;
/*     */   int maxFlushTime;
/*     */   
/*     */   public AsyncAppenderBase()
/*     */   {
/*  41 */     this.aai = new AppenderAttachableImpl();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  48 */     this.queueSize = 256;
/*     */     
/*  50 */     this.appenderCount = 0;
/*     */     
/*     */ 
/*  53 */     this.discardingThreshold = -1;
/*     */     
/*  55 */     this.worker = new Worker();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  63 */     this.maxFlushTime = 1000;
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
/*     */   protected boolean isDiscardable(E eventObject)
/*     */   {
/*  76 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void preprocess(E eventObject) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void start()
/*     */   {
/*  92 */     if (this.appenderCount == 0) {
/*  93 */       addError("No attached appenders found.");
/*  94 */       return;
/*     */     }
/*  96 */     if (this.queueSize < 1) {
/*  97 */       addError("Invalid queue size [" + this.queueSize + "]");
/*  98 */       return;
/*     */     }
/* 100 */     this.blockingQueue = new ArrayBlockingQueue(this.queueSize);
/*     */     
/* 102 */     if (this.discardingThreshold == -1)
/* 103 */       this.discardingThreshold = (this.queueSize / 5);
/* 104 */     addInfo("Setting discardingThreshold to " + this.discardingThreshold);
/* 105 */     this.worker.setDaemon(true);
/* 106 */     this.worker.setName("AsyncAppender-Worker-" + getName());
/*     */     
/* 108 */     super.start();
/* 109 */     this.worker.start();
/*     */   }
/*     */   
/*     */   public void stop()
/*     */   {
/* 114 */     if (!isStarted()) {
/* 115 */       return;
/*     */     }
/*     */     
/*     */ 
/* 119 */     super.stop();
/*     */     
/*     */ 
/*     */ 
/* 123 */     this.worker.interrupt();
/*     */     try {
/* 125 */       this.worker.join(this.maxFlushTime);
/*     */       
/*     */ 
/* 128 */       if (this.worker.isAlive()) {
/* 129 */         addWarn("Max queue flush timeout (" + this.maxFlushTime + " ms) exceeded. Approximately " + this.blockingQueue.size() + " queued events were possibly discarded.");
/*     */       }
/*     */       else {
/* 132 */         addInfo("Queue flush finished successfully within timeout.");
/*     */       }
/*     */     }
/*     */     catch (InterruptedException e) {
/* 136 */       addError("Failed to join worker thread. " + this.blockingQueue.size() + " queued events may be discarded.", e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void append(E eventObject)
/*     */   {
/* 143 */     if ((isQueueBelowDiscardingThreshold()) && (isDiscardable(eventObject))) {
/* 144 */       return;
/*     */     }
/* 146 */     preprocess(eventObject);
/* 147 */     put(eventObject);
/*     */   }
/*     */   
/*     */   private boolean isQueueBelowDiscardingThreshold() {
/* 151 */     return this.blockingQueue.remainingCapacity() < this.discardingThreshold;
/*     */   }
/*     */   
/*     */   private void put(E eventObject) {
/*     */     try {
/* 156 */       this.blockingQueue.put(eventObject);
/*     */     }
/*     */     catch (InterruptedException e) {}
/*     */   }
/*     */   
/*     */   public int getQueueSize() {
/* 162 */     return this.queueSize;
/*     */   }
/*     */   
/*     */   public void setQueueSize(int queueSize) {
/* 166 */     this.queueSize = queueSize;
/*     */   }
/*     */   
/*     */   public int getDiscardingThreshold() {
/* 170 */     return this.discardingThreshold;
/*     */   }
/*     */   
/*     */   public void setDiscardingThreshold(int discardingThreshold) {
/* 174 */     this.discardingThreshold = discardingThreshold;
/*     */   }
/*     */   
/*     */   public int getMaxFlushTime() {
/* 178 */     return this.maxFlushTime;
/*     */   }
/*     */   
/*     */   public void setMaxFlushTime(int maxFlushTime) {
/* 182 */     this.maxFlushTime = maxFlushTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getNumberOfElementsInQueue()
/*     */   {
/* 191 */     return this.blockingQueue.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getRemainingCapacity()
/*     */   {
/* 201 */     return this.blockingQueue.remainingCapacity();
/*     */   }
/*     */   
/*     */ 
/*     */   public void addAppender(Appender<E> newAppender)
/*     */   {
/* 207 */     if (this.appenderCount == 0) {
/* 208 */       this.appenderCount += 1;
/* 209 */       addInfo("Attaching appender named [" + newAppender.getName() + "] to AsyncAppender.");
/* 210 */       this.aai.addAppender(newAppender);
/*     */     } else {
/* 212 */       addWarn("One and only one appender may be attached to AsyncAppender.");
/* 213 */       addWarn("Ignoring additional appender named [" + newAppender.getName() + "]");
/*     */     }
/*     */   }
/*     */   
/*     */   public Iterator<Appender<E>> iteratorForAppenders() {
/* 218 */     return this.aai.iteratorForAppenders();
/*     */   }
/*     */   
/*     */   public Appender<E> getAppender(String name) {
/* 222 */     return this.aai.getAppender(name);
/*     */   }
/*     */   
/*     */   public boolean isAttached(Appender<E> eAppender) {
/* 226 */     return this.aai.isAttached(eAppender);
/*     */   }
/*     */   
/*     */   public void detachAndStopAllAppenders() {
/* 230 */     this.aai.detachAndStopAllAppenders();
/*     */   }
/*     */   
/*     */   public boolean detachAppender(Appender<E> eAppender) {
/* 234 */     return this.aai.detachAppender(eAppender);
/*     */   }
/*     */   
/*     */ 
/* 238 */   public boolean detachAppender(String name) { return this.aai.detachAppender(name); }
/*     */   
/*     */   class Worker extends Thread {
/*     */     Worker() {}
/*     */     
/*     */     public void run() {
/* 244 */       AsyncAppenderBase<E> parent = AsyncAppenderBase.this;
/* 245 */       AppenderAttachableImpl<E> aai = parent.aai;
/*     */       for (;;)
/*     */       {
/* 248 */         if (parent.isStarted()) {
/*     */           try {
/* 250 */             E e = parent.blockingQueue.take();
/* 251 */             aai.appendLoopOnAppenders(e);
/*     */           }
/*     */           catch (InterruptedException ie) {}
/*     */         }
/*     */       }
/*     */       
/* 257 */       AsyncAppenderBase.this.addInfo("Worker thread will flush remaining events before exiting. ");
/*     */       
/* 259 */       for (E e : parent.blockingQueue) {
/* 260 */         aai.appendLoopOnAppenders(e);
/* 261 */         parent.blockingQueue.remove(e);
/*     */       }
/*     */       
/*     */ 
/* 265 */       aai.detachAndStopAllAppenders();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\AsyncAppenderBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */