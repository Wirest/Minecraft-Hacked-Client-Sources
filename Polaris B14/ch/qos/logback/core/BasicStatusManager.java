/*     */ package ch.qos.logback.core;
/*     */ 
/*     */ import ch.qos.logback.core.helpers.CyclicBuffer;
/*     */ import ch.qos.logback.core.spi.LogbackLock;
/*     */ import ch.qos.logback.core.status.OnConsoleStatusListener;
/*     */ import ch.qos.logback.core.status.Status;
/*     */ import ch.qos.logback.core.status.StatusListener;
/*     */ import ch.qos.logback.core.status.StatusManager;
/*     */ import java.util.ArrayList;
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
/*     */ public class BasicStatusManager
/*     */   implements StatusManager
/*     */ {
/*     */   public static final int MAX_HEADER_COUNT = 150;
/*     */   public static final int TAIL_SIZE = 150;
/*  31 */   int count = 0;
/*     */   
/*     */ 
/*  34 */   protected final List<Status> statusList = new ArrayList();
/*  35 */   protected final CyclicBuffer<Status> tailBuffer = new CyclicBuffer(150);
/*     */   
/*  37 */   protected final LogbackLock statusListLock = new LogbackLock();
/*     */   
/*  39 */   int level = 0;
/*     */   
/*     */ 
/*  42 */   protected final List<StatusListener> statusListenerList = new ArrayList();
/*  43 */   protected final LogbackLock statusListenerListLock = new LogbackLock();
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
/*     */   public void add(Status newStatus)
/*     */   {
/*  60 */     fireStatusAddEvent(newStatus);
/*     */     
/*  62 */     this.count += 1;
/*  63 */     if (newStatus.getLevel() > this.level) {
/*  64 */       this.level = newStatus.getLevel();
/*     */     }
/*     */     
/*  67 */     synchronized (this.statusListLock) {
/*  68 */       if (this.statusList.size() < 150) {
/*  69 */         this.statusList.add(newStatus);
/*     */       } else {
/*  71 */         this.tailBuffer.add(newStatus);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public List<Status> getCopyOfStatusList()
/*     */   {
/*  78 */     synchronized (this.statusListLock) {
/*  79 */       List<Status> tList = new ArrayList(this.statusList);
/*  80 */       tList.addAll(this.tailBuffer.asList());
/*  81 */       return tList;
/*     */     }
/*     */   }
/*     */   
/*     */   private void fireStatusAddEvent(Status status) {
/*  86 */     synchronized (this.statusListenerListLock) {
/*  87 */       for (StatusListener sl : this.statusListenerList) {
/*  88 */         sl.addStatusEvent(status);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear() {
/*  94 */     synchronized (this.statusListLock) {
/*  95 */       this.count = 0;
/*  96 */       this.statusList.clear();
/*  97 */       this.tailBuffer.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public int getLevel() {
/* 102 */     return this.level;
/*     */   }
/*     */   
/*     */   public int getCount() {
/* 106 */     return this.count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(StatusListener listener)
/*     */   {
/* 114 */     synchronized (this.statusListenerListLock) {
/* 115 */       if ((listener instanceof OnConsoleStatusListener)) {
/* 116 */         boolean alreadyPresent = checkForPresence(this.statusListenerList, listener.getClass());
/* 117 */         if (alreadyPresent)
/* 118 */           return;
/*     */       }
/* 120 */       this.statusListenerList.add(listener);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean checkForPresence(List<StatusListener> statusListenerList, Class<?> aClass) {
/* 125 */     for (StatusListener e : statusListenerList) {
/* 126 */       if (e.getClass() == aClass)
/* 127 */         return true;
/*     */     }
/* 129 */     return false;
/*     */   }
/*     */   
/*     */   public void remove(StatusListener listener)
/*     */   {
/* 134 */     synchronized (this.statusListenerListLock) {
/* 135 */       this.statusListenerList.remove(listener);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public List<StatusListener> getCopyOfStatusListenerList()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 53	ch/qos/logback/core/BasicStatusManager:statusListenerListLock	Lch/qos/logback/core/spi/LogbackLock;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: new 32	java/util/ArrayList
/*     */     //   10: dup
/*     */     //   11: aload_0
/*     */     //   12: getfield 51	ch/qos/logback/core/BasicStatusManager:statusListenerList	Ljava/util/List;
/*     */     //   15: invokespecial 86	java/util/ArrayList:<init>	(Ljava/util/Collection;)V
/*     */     //   18: aload_1
/*     */     //   19: monitorexit
/*     */     //   20: areturn
/*     */     //   21: astore_2
/*     */     //   22: aload_1
/*     */     //   23: monitorexit
/*     */     //   24: aload_2
/*     */     //   25: athrow
/*     */     // Line number table:
/*     */     //   Java source line #140	-> byte code offset #0
/*     */     //   Java source line #141	-> byte code offset #7
/*     */     //   Java source line #142	-> byte code offset #21
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	26	0	this	BasicStatusManager
/*     */     //   5	18	1	Ljava/lang/Object;	Object
/*     */     //   21	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	20	21	finally
/*     */     //   21	24	21	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\BasicStatusManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */