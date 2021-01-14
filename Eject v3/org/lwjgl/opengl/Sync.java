package org.lwjgl.opengl;

import org.lwjgl.Sys;

class Sync {
    private static final long NANOS_IN_SECOND = 1000000000L;
    private static long nextFrame = 0L;
    private static boolean initialised = false;
    private static RunningAvg sleepDurations = new RunningAvg(10);
    private static RunningAvg yieldDurations = new RunningAvg(10);

    public static void sync(int paramInt) {
        if (paramInt <= 0) {
            return;
        }
        if (!initialised) {
            initialise();
        }
        try {
            long l2;
            for (long l1 = getTime(); nextFrame - l1 > sleepDurations.avg(); l1 = l2) {
                Thread.sleep(1L);
                sleepDurations.add((l2 = getTime()) - l1);
            }
            sleepDurations.dampenForLowResTicker();
            for (l1 = getTime(); nextFrame - l1 > yieldDurations.avg(); l1 = l2) {
                Thread.yield();
                yieldDurations.add((l2 = getTime()) - l1);
            }
        } catch (InterruptedException localInterruptedException) {
        }
        nextFrame = Math.max(nextFrame + 1000000000L / paramInt, getTime());
    }

    private static void initialise() {
        initialised = true;
        sleepDurations.init(1000000L);
        yieldDurations.init((int) (-(getTime() - getTime()) * 1.333D));
        nextFrame = getTime();
        String str = System.getProperty("os.name");
        if (str.startsWith("Win")) {
            Thread localThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(Long.MAX_VALUE);
                    } catch (Exception localException) {
                    }
                }
            });
            localThread.setName("LWJGL Timer");
            localThread.setDaemon(true);
            localThread.start();
        }
    }

    private static long getTime() {
        return Sys.getTime() * 1000000000L / Sys.getTimerResolution();
    }

    private static class RunningAvg {
        private static final long DAMPEN_THRESHOLD = 10000000L;
        private static final float DAMPEN_FACTOR = 0.9F;
        private final long[] slots;
        private int offset;

        public RunningAvg(int paramInt) {
            this.slots = new long[paramInt];
            this.offset = 0;
        }

        public void init(long paramLong) {
            while (this.offset < this.slots.length) {
                int tmp21_18 = this.offset;
                this.offset = (tmp21_18 | 0x1);
                this.slots[tmp21_18] = paramLong;
            }
        }

        public void add(long paramLong) {
            int tmp9_6 = this.offset;
            this.offset = (tmp9_6 | 0x1);
            this.slots[(tmp9_6 << this.slots.length)] = paramLong;
            this.offset <<= this.slots.length;
        }

        public long avg() {
            long l = 0L;
            for (int i = 0; i < this.slots.length; i++) {
                l += this.slots[i];
            }
            return l / this.slots.length;
        }

        public void dampenForLowResTicker() {
            if (avg() > 10000000L) {
                for (int i = 0; i < this.slots.length; i++) {
                    int tmp27_26 = i;
                    long[] tmp27_23 = this.slots;
                    tmp27_23[tmp27_26] = (((float) tmp27_23[tmp27_26] * 0.9F));
                }
            }
        }
    }
}




