package net.minecraft.world.storage;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

public class ThreadedFileIOBase implements Runnable {
   private static final ThreadedFileIOBase threadedIOInstance = new ThreadedFileIOBase();
   private List threadedIOQueue = Collections.synchronizedList(Lists.newArrayList());
   private volatile long writeQueuedCounter;
   private volatile long savedIOCounter;
   private volatile boolean isThreadWaiting;

   private ThreadedFileIOBase() {
      Thread thread = new Thread(this, "File IO Thread");
      thread.setPriority(1);
      thread.start();
   }

   public static ThreadedFileIOBase getThreadedIOInstance() {
      return threadedIOInstance;
   }

   public void run() {
      while(true) {
         this.processQueue();
      }
   }

   private void processQueue() {
      for(int i = 0; i < this.threadedIOQueue.size(); ++i) {
         IThreadedFileIO ithreadedfileio = (IThreadedFileIO)this.threadedIOQueue.get(i);
         boolean flag = ithreadedfileio.writeNextIO();
         if (!flag) {
            this.threadedIOQueue.remove(i--);
            ++this.savedIOCounter;
         }

         try {
            Thread.sleep(this.isThreadWaiting ? 0L : 10L);
         } catch (InterruptedException var6) {
            var6.printStackTrace();
         }
      }

      if (this.threadedIOQueue.isEmpty()) {
         try {
            Thread.sleep(25L);
         } catch (InterruptedException var5) {
            var5.printStackTrace();
         }
      }

   }

   public void queueIO(IThreadedFileIO p_75735_1_) {
      if (!this.threadedIOQueue.contains(p_75735_1_)) {
         ++this.writeQueuedCounter;
         this.threadedIOQueue.add(p_75735_1_);
      }

   }

   public void waitForFinish() throws InterruptedException {
      this.isThreadWaiting = true;

      while(this.writeQueuedCounter != this.savedIOCounter) {
         Thread.sleep(10L);
      }

      this.isThreadWaiting = false;
   }
}
