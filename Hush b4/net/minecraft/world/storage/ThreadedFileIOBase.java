// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.world.storage;

import java.util.Collections;
import com.google.common.collect.Lists;
import java.util.List;

public class ThreadedFileIOBase implements Runnable
{
    private static final ThreadedFileIOBase threadedIOInstance;
    private List<IThreadedFileIO> threadedIOQueue;
    private volatile long writeQueuedCounter;
    private volatile long savedIOCounter;
    private volatile boolean isThreadWaiting;
    
    static {
        threadedIOInstance = new ThreadedFileIOBase();
    }
    
    private ThreadedFileIOBase() {
        this.threadedIOQueue = Collections.synchronizedList((List<IThreadedFileIO>)Lists.newArrayList());
        final Thread thread = new Thread(this, "File IO Thread");
        thread.setPriority(1);
        thread.start();
    }
    
    public static ThreadedFileIOBase getThreadedIOInstance() {
        return ThreadedFileIOBase.threadedIOInstance;
    }
    
    @Override
    public void run() {
        while (true) {
            this.processQueue();
        }
    }
    
    private void processQueue() {
        for (int i = 0; i < this.threadedIOQueue.size(); ++i) {
            final IThreadedFileIO ithreadedfileio = this.threadedIOQueue.get(i);
            final boolean flag = ithreadedfileio.writeNextIO();
            if (!flag) {
                this.threadedIOQueue.remove(i--);
                ++this.savedIOCounter;
            }
            try {
                Thread.sleep(this.isThreadWaiting ? 0L : 10L);
            }
            catch (InterruptedException interruptedexception1) {
                interruptedexception1.printStackTrace();
            }
        }
        if (this.threadedIOQueue.isEmpty()) {
            try {
                Thread.sleep(25L);
            }
            catch (InterruptedException interruptedexception2) {
                interruptedexception2.printStackTrace();
            }
        }
    }
    
    public void queueIO(final IThreadedFileIO p_75735_1_) {
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
