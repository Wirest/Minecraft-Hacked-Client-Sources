// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.mapped;

import java.util.concurrent.Callable;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import org.lwjgl.MemoryUtil;
import java.nio.IntBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import org.lwjgl.LWJGLUtil;

final class CacheLineSize
{
    private CacheLineSize() {
    }
    
    static int getCacheLineSize() {
        final int THREADS = 2;
        final int REPEATS = 200000;
        final int LOCAL_REPEATS = 100000;
        final int MAX_SIZE = LWJGLUtil.getPrivilegedInteger("org.lwjgl.util.mapped.CacheLineMaxSize", 1024) / 4;
        final double TIME_THRESHOLD = 1.0 + LWJGLUtil.getPrivilegedInteger("org.lwjgl.util.mapped.CacheLineTimeThreshold", 50) / 100.0;
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final ExecutorCompletionService<Long> completionService = new ExecutorCompletionService<Long>(executorService);
        try {
            final IntBuffer memory = getMemory(MAX_SIZE);
            final int WARMUP = 10;
            for (int i = 0; i < 10; ++i) {
                doTest(2, 100000, 0, memory, completionService);
            }
            long totalTime = 0L;
            int count = 0;
            int cacheLineSize = 64;
            boolean found = false;
            for (int j = MAX_SIZE; j >= 1; j >>= 1) {
                final long time = doTest(2, 100000, j, memory, completionService);
                if (totalTime > 0L) {
                    final long avgTime = totalTime / count;
                    if (time / (double)avgTime > TIME_THRESHOLD) {
                        cacheLineSize = (j << 1) * 4;
                        found = true;
                        break;
                    }
                }
                totalTime += time;
                ++count;
            }
            if (LWJGLUtil.DEBUG) {
                if (found) {
                    LWJGLUtil.log("Cache line size detected: " + cacheLineSize + " bytes");
                }
                else {
                    LWJGLUtil.log("Failed to detect cache line size, assuming " + cacheLineSize + " bytes");
                }
            }
            return cacheLineSize;
        }
        finally {
            executorService.shutdown();
        }
    }
    
    public static void main(final String[] args) {
        CacheUtil.getCacheLineSize();
    }
    
    static long memoryLoop(final int index, final int repeats, final IntBuffer memory, final int padding) {
        final long address = MemoryUtil.getAddress(memory) + index * padding * 4;
        final long time = System.nanoTime();
        for (int i = 0; i < repeats; ++i) {
            MappedHelper.ivput(MappedHelper.ivget(address) + 1, address);
        }
        return System.nanoTime() - time;
    }
    
    private static IntBuffer getMemory(final int START_SIZE) {
        final int PAGE_SIZE = MappedObjectUnsafe.INSTANCE.pageSize();
        final ByteBuffer buffer = ByteBuffer.allocateDirect(START_SIZE * 4 + PAGE_SIZE).order(ByteOrder.nativeOrder());
        if (MemoryUtil.getAddress(buffer) % PAGE_SIZE != 0L) {
            buffer.position(PAGE_SIZE - (int)(MemoryUtil.getAddress(buffer) & (long)(PAGE_SIZE - 1)));
        }
        return buffer.asIntBuffer();
    }
    
    private static long doTest(final int threads, final int repeats, final int padding, final IntBuffer memory, final ExecutorCompletionService<Long> completionService) {
        for (int i = 0; i < threads; ++i) {
            submitTest(completionService, i, repeats, memory, padding);
        }
        return waitForResults(threads, completionService);
    }
    
    private static void submitTest(final ExecutorCompletionService<Long> completionService, final int index, final int repeats, final IntBuffer memory, final int padding) {
        completionService.submit(new Callable<Long>() {
            public Long call() throws Exception {
                return CacheLineSize.memoryLoop(index, repeats, memory, padding);
            }
        });
    }
    
    private static long waitForResults(final int count, final ExecutorCompletionService<Long> completionService) {
        try {
            long totalTime = 0L;
            for (int i = 0; i < count; ++i) {
                totalTime += completionService.take().get();
            }
            return totalTime;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
