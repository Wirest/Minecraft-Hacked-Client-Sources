/*    */ package net.minecraft.world.storage;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ 
/*    */ public class ThreadedFileIOBase
/*    */   implements Runnable
/*    */ {
/* 10 */   private static final ThreadedFileIOBase threadedIOInstance = new ThreadedFileIOBase();
/* 11 */   private List<IThreadedFileIO> threadedIOQueue = Collections.synchronizedList(Lists.newArrayList());
/*    */   private volatile long writeQueuedCounter;
/*    */   private volatile long savedIOCounter;
/*    */   private volatile boolean isThreadWaiting;
/*    */   
/*    */   private ThreadedFileIOBase()
/*    */   {
/* 18 */     Thread thread = new Thread(this, "File IO Thread");
/* 19 */     thread.setPriority(1);
/* 20 */     thread.start();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static ThreadedFileIOBase getThreadedIOInstance()
/*    */   {
/* 28 */     return threadedIOInstance;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/*    */     for (;;)
/*    */     {
/* 35 */       processQueue();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private void processQueue()
/*    */   {
/* 44 */     for (int i = 0; i < this.threadedIOQueue.size(); i++)
/*    */     {
/* 46 */       IThreadedFileIO ithreadedfileio = (IThreadedFileIO)this.threadedIOQueue.get(i);
/* 47 */       boolean flag = ithreadedfileio.writeNextIO();
/*    */       
/* 49 */       if (!flag)
/*    */       {
/* 51 */         this.threadedIOQueue.remove(i--);
/* 52 */         this.savedIOCounter += 1L;
/*    */       }
/*    */       
/*    */       try
/*    */       {
/* 57 */         Thread.sleep(this.isThreadWaiting ? 0L : 10L);
/*    */       }
/*    */       catch (InterruptedException interruptedexception1)
/*    */       {
/* 61 */         interruptedexception1.printStackTrace();
/*    */       }
/*    */     }
/*    */     
/* 65 */     if (this.threadedIOQueue.isEmpty())
/*    */     {
/*    */       try
/*    */       {
/* 69 */         Thread.sleep(25L);
/*    */       }
/*    */       catch (InterruptedException interruptedexception)
/*    */       {
/* 73 */         interruptedexception.printStackTrace();
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void queueIO(IThreadedFileIO p_75735_1_)
/*    */   {
/* 83 */     if (!this.threadedIOQueue.contains(p_75735_1_))
/*    */     {
/* 85 */       this.writeQueuedCounter += 1L;
/* 86 */       this.threadedIOQueue.add(p_75735_1_);
/*    */     }
/*    */   }
/*    */   
/*    */   public void waitForFinish() throws InterruptedException
/*    */   {
/* 92 */     this.isThreadWaiting = true;
/*    */     
/* 94 */     while (this.writeQueuedCounter != this.savedIOCounter)
/*    */     {
/* 96 */       Thread.sleep(10L);
/*    */     }
/*    */     
/* 99 */     this.isThreadWaiting = false;
/*    */   }
/*    */ }


/* Location:              C:\Users\suzjan\AppData\Roaming\.minecraft\versions\Polaris\Polaris.jar!\net\minecraft\world\storage\ThreadedFileIOBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */