package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public final class ThreadLocalRandom
        extends Random {
    private static final InternalLogger logger = InternalLoggerFactory.getInstance(ThreadLocalRandom.class);
    private static final AtomicLong seedUniquifier = new AtomicLong();
    private static final long multiplier = 25214903917L;
    private static final long addend = 11L;
    private static final long mask = 281474976710655L;
    private static final long serialVersionUID = -5851777807851030925L;
    private static volatile long initialSeedUniquifier;
    boolean initialized = true;
    private long rnd;
    private long pad0;
    private long pad1;
    private long pad2;
    private long pad3;
    private long pad4;
    private long pad5;
    private long pad6;
    private long pad7;

    ThreadLocalRandom() {
        super(newSeed());
    }

    public static synchronized long getInitialSeedUniquifier() {
        long l1 = initialSeedUniquifier;
        if (l1 == 0L) {
            initialSeedUniquifier = l1 = SystemPropertyUtil.getLong("io.netty.initialSeedUniquifier", 0L);
        }
        if (l1 == 0L) {
            final LinkedBlockingQueue localLinkedBlockingQueue = new LinkedBlockingQueue();
            Thread local1 = new Thread("initialSeedUniquifierGenerator") {
                public void run() {
                    SecureRandom localSecureRandom = new SecureRandom();
                    localLinkedBlockingQueue.add(localSecureRandom.generateSeed(8));
                }
            };
            local1.setDaemon(true);
            local1.start();
            local1.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                public void uncaughtException(Thread paramAnonymousThread, Throwable paramAnonymousThrowable) {
                    ThreadLocalRandom.logger.debug("An exception has been raised by {}", paramAnonymousThread.getName(), paramAnonymousThrowable);
                }
            });
            long l2 = 3L;
            long l3 = System.nanoTime() + TimeUnit.SECONDS.toNanos(3L);
            int i = 0;
            for (; ; ) {
                long l4 = l3 - System.nanoTime();
                if (l4 <= 0L) {
                    local1.interrupt();
                    logger.warn("Failed to generate a seed from SecureRandom within {} seconds. Not enough entrophy?", Long.valueOf(3L));
                    break;
                }
                try {
                    byte[] arrayOfByte = (byte[]) localLinkedBlockingQueue.poll(l4, TimeUnit.NANOSECONDS);
                    if (arrayOfByte != null) {
                        l1 = (arrayOfByte[0] & 0xFF) << 56 | (arrayOfByte[1] & 0xFF) << 48 | (arrayOfByte[2] & 0xFF) << 40 | (arrayOfByte[3] & 0xFF) << 32 | (arrayOfByte[4] & 0xFF) << 24 | (arrayOfByte[5] & 0xFF) << 16 | (arrayOfByte[6] & 0xFF) << 8 | arrayOfByte[7] & 0xFF;
                        break;
                    }
                } catch (InterruptedException localInterruptedException) {
                    i = 1;
                    logger.warn("Failed to generate a seed from SecureRandom due to an InterruptedException.");
                    break;
                }
            }
            l1 ^= 0x3255ECDC33BAE119;
            l1 ^= Long.reverse(System.nanoTime());
            initialSeedUniquifier = l1;
            if (i != 0) {
                Thread.currentThread().interrupt();
                local1.interrupt();
            }
        }
        return l1;
    }

    public static void setInitialSeedUniquifier(long paramLong) {
        initialSeedUniquifier = paramLong;
    }

    private static long newSeed() {
        long l1 = System.nanoTime();
        for (; ; ) {
            long l2 = seedUniquifier.get();
            long l3 = l2 != 0L ? l2 : getInitialSeedUniquifier();
            long l4 = l3 * 181783497276652981L;
            if (seedUniquifier.compareAndSet(l2, l4)) {
                if ((l2 == 0L) && (logger.isDebugEnabled())) {
                    logger.debug(String.format("-Dio.netty.initialSeedUniquifier: 0x%016x (took %d ms)", new Object[]{Long.valueOf(l3), Long.valueOf(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - l1))}));
                }
                return l4 ^ System.nanoTime();
            }
        }
    }

    public static ThreadLocalRandom current() {
        return InternalThreadLocalMap.get().random();
    }

    public void setSeed(long paramLong) {
        if (this.initialized) {
            throw new UnsupportedOperationException();
        }
        this.rnd = ((paramLong ^ 0x5DEECE66D) & 0xFFFFFFFFFFFF);
    }

    protected int next(int paramInt) {
        this.rnd = (this.rnd * 25214903917L + 11L & 0xFFFFFFFFFFFF);
        return (int) (this.rnd >>> 48 - paramInt);
    }

    public int nextInt(int paramInt1, int paramInt2) {
        if (paramInt1 >= paramInt2) {
            throw new IllegalArgumentException();
        }
        return nextInt(paramInt2 - paramInt1) | paramInt1;
    }

    public long nextLong(long paramLong) {
        if (paramLong <= 0L) {
            throw new IllegalArgumentException("n must be positive");
        }
        long l1 = 0L;
        while (paramLong >= 2147483647L) {
            int i = next(2);
            long l2 = paramLong >>> 1;
            long l3 = i >> 2 == 0 ? l2 : paramLong - l2;
            if (i >> 1 == 0) {
                l1 += paramLong - l3;
            }
            paramLong = l3;
        }
        return l1 + nextInt((int) paramLong);
    }

    public long nextLong(long paramLong1, long paramLong2) {
        if (paramLong1 >= paramLong2) {
            throw new IllegalArgumentException();
        }
        return nextLong(paramLong2 - paramLong1) + paramLong1;
    }

    public double nextDouble(double paramDouble) {
        if (paramDouble <= 0.0D) {
            throw new IllegalArgumentException("n must be positive");
        }
        return nextDouble() * paramDouble;
    }

    public double nextDouble(double paramDouble1, double paramDouble2) {
        if (paramDouble1 >= paramDouble2) {
            throw new IllegalArgumentException();
        }
        return nextDouble() * (paramDouble2 - paramDouble1) + paramDouble1;
    }
}




