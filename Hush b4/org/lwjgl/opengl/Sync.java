// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.opengl;

import org.lwjgl.Sys;

class Sync
{
    private static final long NANOS_IN_SECOND = 1000000000L;
    private static long nextFrame;
    private static boolean initialised;
    private static RunningAvg sleepDurations;
    private static RunningAvg yieldDurations;
    
    public static void sync(final int fps) {
        if (fps <= 0) {
            return;
        }
        if (!Sync.initialised) {
            initialise();
        }
        try {
            long t2;
            for (long t0 = getTime(); Sync.nextFrame - t0 > Sync.sleepDurations.avg(); t0 = t2) {
                Thread.sleep(1L);
                Sync.sleepDurations.add((t2 = getTime()) - t0);
            }
            Sync.sleepDurations.dampenForLowResTicker();
            for (long t0 = getTime(); Sync.nextFrame - t0 > Sync.yieldDurations.avg(); t0 = t2) {
                Thread.yield();
                Sync.yieldDurations.add((t2 = getTime()) - t0);
            }
        }
        catch (InterruptedException ex) {}
        Sync.nextFrame = Math.max(Sync.nextFrame + 1000000000L / fps, getTime());
    }
    
    private static void initialise() {
        Sync.initialised = true;
        Sync.sleepDurations.init(1000000L);
        Sync.yieldDurations.init((int)(-(getTime() - getTime()) * 1.333));
        Sync.nextFrame = getTime();
        final String osName = System.getProperty("os.name");
        if (osName.startsWith("Win")) {
            final Thread timerAccuracyThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(Long.MAX_VALUE);
                    }
                    catch (Exception ex) {}
                }
            });
            timerAccuracyThread.setName("LWJGL Timer");
            timerAccuracyThread.setDaemon(true);
            timerAccuracyThread.start();
        }
    }
    
    private static long getTime() {
        return Sys.getTime() * 1000000000L / Sys.getTimerResolution();
    }
    
    static {
        Sync.nextFrame = 0L;
        Sync.initialised = false;
        Sync.sleepDurations = new RunningAvg(10);
        Sync.yieldDurations = new RunningAvg(10);
    }
    
    private static class RunningAvg
    {
        private final long[] slots;
        private int offset;
        private static final long DAMPEN_THRESHOLD = 10000000L;
        private static final float DAMPEN_FACTOR = 0.9f;
        
        public RunningAvg(final int slotCount) {
            this.slots = new long[slotCount];
            this.offset = 0;
        }
        
        public void init(final long value) {
            while (this.offset < this.slots.length) {
                this.slots[this.offset++] = value;
            }
        }
        
        public void add(final long value) {
            this.slots[this.offset++ % this.slots.length] = value;
            this.offset %= this.slots.length;
        }
        
        public long avg() {
            long sum = 0L;
            for (int i = 0; i < this.slots.length; ++i) {
                sum += this.slots[i];
            }
            return sum / this.slots.length;
        }
        
        public void dampenForLowResTicker() {
            if (this.avg() > 10000000L) {
                for (int i = 0; i < this.slots.length; ++i) {
                    final long[] slots = this.slots;
                    final int n = i;
                    slots[n] *= (long)0.9f;
                }
            }
        }
    }
}
