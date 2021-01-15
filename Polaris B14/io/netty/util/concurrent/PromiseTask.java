/*     */ package io.netty.util.concurrent;
/*     */ 
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.RunnableFuture;
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
/*     */ class PromiseTask<V>
/*     */   extends DefaultPromise<V>
/*     */   implements RunnableFuture<V>
/*     */ {
/*     */   protected final Callable<V> task;
/*     */   
/*     */   static <T> Callable<T> toCallable(Runnable runnable, T result)
/*     */   {
/*  24 */     return new RunnableAdapter(runnable, result);
/*     */   }
/*     */   
/*     */   private static final class RunnableAdapter<T> implements Callable<T> {
/*     */     final Runnable task;
/*     */     final T result;
/*     */     
/*     */     RunnableAdapter(Runnable task, T result) {
/*  32 */       this.task = task;
/*  33 */       this.result = result;
/*     */     }
/*     */     
/*     */     public T call()
/*     */     {
/*  38 */       this.task.run();
/*  39 */       return (T)this.result;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/*  44 */       return "Callable(task: " + this.task + ", result: " + this.result + ')';
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   PromiseTask(EventExecutor executor, Runnable runnable, V result)
/*     */   {
/*  51 */     this(executor, toCallable(runnable, result));
/*     */   }
/*     */   
/*     */   PromiseTask(EventExecutor executor, Callable<V> callable) {
/*  55 */     super(executor);
/*  56 */     this.task = callable;
/*     */   }
/*     */   
/*     */   public final int hashCode()
/*     */   {
/*  61 */     return System.identityHashCode(this);
/*     */   }
/*     */   
/*     */   public final boolean equals(Object obj)
/*     */   {
/*  66 */     return this == obj;
/*     */   }
/*     */   
/*     */   public void run()
/*     */   {
/*     */     try {
/*  72 */       if (setUncancellableInternal()) {
/*  73 */         V result = this.task.call();
/*  74 */         setSuccessInternal(result);
/*     */       }
/*     */     } catch (Throwable e) {
/*  77 */       setFailureInternal(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public final Promise<V> setFailure(Throwable cause)
/*     */   {
/*  83 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   protected final Promise<V> setFailureInternal(Throwable cause) {
/*  87 */     super.setFailure(cause);
/*  88 */     return this;
/*     */   }
/*     */   
/*     */   public final boolean tryFailure(Throwable cause)
/*     */   {
/*  93 */     return false;
/*     */   }
/*     */   
/*     */   protected final boolean tryFailureInternal(Throwable cause) {
/*  97 */     return super.tryFailure(cause);
/*     */   }
/*     */   
/*     */   public final Promise<V> setSuccess(V result)
/*     */   {
/* 102 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   protected final Promise<V> setSuccessInternal(V result) {
/* 106 */     super.setSuccess(result);
/* 107 */     return this;
/*     */   }
/*     */   
/*     */   public final boolean trySuccess(V result)
/*     */   {
/* 112 */     return false;
/*     */   }
/*     */   
/*     */   protected final boolean trySuccessInternal(V result) {
/* 116 */     return super.trySuccess(result);
/*     */   }
/*     */   
/*     */   public final boolean setUncancellable()
/*     */   {
/* 121 */     throw new IllegalStateException();
/*     */   }
/*     */   
/*     */   protected final boolean setUncancellableInternal() {
/* 125 */     return super.setUncancellable();
/*     */   }
/*     */   
/*     */   protected StringBuilder toStringBuilder()
/*     */   {
/* 130 */     StringBuilder buf = super.toStringBuilder();
/* 131 */     buf.setCharAt(buf.length() - 1, ',');
/*     */     
/* 133 */     return buf.append(" task: ").append(this.task).append(')');
/*     */   }
/*     */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\io\netty\util\concurrent\PromiseTask.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */