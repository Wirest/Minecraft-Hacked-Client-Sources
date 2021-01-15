/*    */ package ch.qos.logback.core.util;
/*    */ 
/*    */ import ch.qos.logback.core.CoreConstants;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Executors;
/*    */ import java.util.concurrent.SynchronousQueue;
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import java.util.concurrent.ThreadPoolExecutor;
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExecutorServiceUtil
/*    */ {
/* 34 */   private static final ThreadFactory THREAD_FACTORY = new ThreadFactory()
/*    */   {
/* 36 */     private final ThreadFactory defaultFactory = Executors.defaultThreadFactory();
/* 37 */     private final AtomicInteger threadNumber = new AtomicInteger(1);
/*    */     
/*    */     public Thread newThread(Runnable r) {
/* 40 */       Thread thread = this.defaultFactory.newThread(r);
/* 41 */       if (!thread.isDaemon()) {
/* 42 */         thread.setDaemon(true);
/*    */       }
/* 44 */       thread.setName("logback-" + this.threadNumber.getAndIncrement());
/* 45 */       return thread;
/*    */     }
/*    */   };
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ExecutorService newExecutorService()
/*    */   {
/* 54 */     return new ThreadPoolExecutor(CoreConstants.CORE_POOL_SIZE, 32, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue(), THREAD_FACTORY);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void shutdown(ExecutorService executorService)
/*    */   {
/* 67 */     executorService.shutdownNow();
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\ch\qos\logback\core\util\ExecutorServiceUtil.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */