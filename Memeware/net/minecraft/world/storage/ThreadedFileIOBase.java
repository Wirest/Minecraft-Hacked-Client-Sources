package net.minecraft.world.storage;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

public class ThreadedFileIOBase implements Runnable {
    /**
     * Instance of ThreadedFileIOBase
     */
    private static final ThreadedFileIOBase threadedIOInstance = new ThreadedFileIOBase();
    private List threadedIOQueue = Collections.synchronizedList(Lists.newArrayList());
    private volatile long writeQueuedCounter;
    private volatile long savedIOCounter;
    private volatile boolean isThreadWaiting;
    private static final String __OBFID = "CL_00000605";

    private ThreadedFileIOBase() {
        Thread var1 = new Thread(this, "File IO Thread");
        var1.setPriority(1);
        var1.start();
    }

    public static ThreadedFileIOBase func_178779_a() {
        return threadedIOInstance;
    }

    public void run() {
        while (true) {
            this.processQueue();
        }
    }

    /**
     * Process the items that are in the queue
     */
    private void processQueue() {
        for (int var1 = 0; var1 < this.threadedIOQueue.size(); ++var1) {
            IThreadedFileIO var2 = (IThreadedFileIO) this.threadedIOQueue.get(var1);
            boolean var3 = var2.writeNextIO();

            if (!var3) {
                this.threadedIOQueue.remove(var1--);
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

    /**
     * threaded io
     */
    public void queueIO(IThreadedFileIO p_75735_1_) {
        if (!this.threadedIOQueue.contains(p_75735_1_)) {
            ++this.writeQueuedCounter;
            this.threadedIOQueue.add(p_75735_1_);
        }
    }

    public void waitForFinish() throws InterruptedException {
        this.isThreadWaiting = true;

        while (this.writeQueuedCounter != this.savedIOCounter) {
            Thread.sleep(10L);
        }

        this.isThreadWaiting = false;
    }
}
