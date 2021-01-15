/*     */ package ch.qos.logback.core.spi;
/*     */ 
/*     */ import ch.qos.logback.core.Appender;
/*     */ import java.util.Iterator;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
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
/*     */ public class AppenderAttachableImpl<E>
/*     */   implements AppenderAttachable<E>
/*     */ {
/*  29 */   private final CopyOnWriteArrayList<Appender<E>> appenderList = new CopyOnWriteArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addAppender(Appender<E> newAppender)
/*     */   {
/*  36 */     if (newAppender == null) {
/*  37 */       throw new IllegalArgumentException("Null argument disallowed");
/*     */     }
/*  39 */     this.appenderList.addIfAbsent(newAppender);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int appendLoopOnAppenders(E e)
/*     */   {
/*  46 */     int size = 0;
/*  47 */     for (Appender<E> appender : this.appenderList) {
/*  48 */       appender.doAppend(e);
/*  49 */       size++;
/*     */     }
/*  51 */     return size;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<Appender<E>> iteratorForAppenders()
/*     */   {
/*  61 */     return this.appenderList.iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Appender<E> getAppender(String name)
/*     */   {
/*  71 */     if (name == null) {
/*  72 */       return null;
/*     */     }
/*  74 */     Appender<E> found = null;
/*  75 */     for (Appender<E> appender : this.appenderList) {
/*  76 */       if (name.equals(appender.getName())) {
/*  77 */         return appender;
/*     */       }
/*     */     }
/*  80 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAttached(Appender<E> appender)
/*     */   {
/*  90 */     if (appender == null) {
/*  91 */       return false;
/*     */     }
/*  93 */     for (Appender<E> a : this.appenderList) {
/*  94 */       if (a == appender) return true;
/*     */     }
/*  96 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void detachAndStopAllAppenders()
/*     */   {
/* 103 */     for (Appender<E> a : this.appenderList) {
/* 104 */       a.stop();
/*     */     }
/* 106 */     this.appenderList.clear();
/*     */   }
/*     */   
/* 109 */   static final long START = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean detachAppender(Appender<E> appender)
/*     */   {
/* 116 */     if (appender == null) {
/* 117 */       return false;
/*     */     }
/*     */     
/* 120 */     boolean result = this.appenderList.remove(appender);
/* 121 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean detachAppender(String name)
/*     */   {
/* 129 */     if (name == null) {
/* 130 */       return false;
/*     */     }
/* 132 */     boolean removed = false;
/* 133 */     for (Appender<E> a : this.appenderList) {
/* 134 */       if (name.equals(a.getName())) {
/* 135 */         removed = this.appenderList.remove(a);
/* 136 */         break;
/*     */       }
/*     */     }
/* 139 */     return removed;
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\spi\AppenderAttachableImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */