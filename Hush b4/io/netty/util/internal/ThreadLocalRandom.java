// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import io.netty.util.internal.logging.InternalLoggerFactory;
import java.util.concurrent.TimeUnit;
import java.security.SecureRandom;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;
import io.netty.util.internal.logging.InternalLogger;
import java.util.Random;

public final class ThreadLocalRandom extends Random
{
    private static final InternalLogger logger;
    private static final AtomicLong seedUniquifier;
    private static volatile long initialSeedUniquifier;
    private static final long multiplier = 25214903917L;
    private static final long addend = 11L;
    private static final long mask = 281474976710655L;
    private long rnd;
    boolean initialized;
    private long pad0;
    private long pad1;
    private long pad2;
    private long pad3;
    private long pad4;
    private long pad5;
    private long pad6;
    private long pad7;
    private static final long serialVersionUID = -5851777807851030925L;
    
    public static void setInitialSeedUniquifier(final long initialSeedUniquifier) {
        ThreadLocalRandom.initialSeedUniquifier = initialSeedUniquifier;
    }
    
    public static synchronized long getInitialSeedUniquifier() {
        long initialSeedUniquifier = ThreadLocalRandom.initialSeedUniquifier;
        if (initialSeedUniquifier == 0L) {
            initialSeedUniquifier = (ThreadLocalRandom.initialSeedUniquifier = SystemPropertyUtil.getLong("io.netty.initialSeedUniquifier", 0L));
        }
        if (initialSeedUniquifier == 0L) {
            final BlockingQueue<byte[]> queue = new LinkedBlockingQueue<byte[]>();
            final Thread generatorThread = new Thread("initialSeedUniquifierGenerator") {
                @Override
                public void run() {
                    final SecureRandom random = new SecureRandom();
                    queue.add(random.generateSeed(8));
                }
            };
            generatorThread.setDaemon(true);
            generatorThread.start();
            generatorThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(final Thread t, final Throwable e) {
                    ThreadLocalRandom.logger.debug("An exception has been raised by {}", t.getName(), e);
                }
            });
            final long timeoutSeconds = 3L;
            final long deadLine = System.nanoTime() + TimeUnit.SECONDS.toNanos(3L);
            boolean interrupted = false;
            while (true) {
                final long waitTime = deadLine - System.nanoTime();
                if (waitTime <= 0L) {
                    generatorThread.interrupt();
                    ThreadLocalRandom.logger.warn("Failed to generate a seed from SecureRandom within {} seconds. Not enough entrophy?", (Object)3L);
                    break;
                }
                try {
                    final byte[] seed = queue.poll(waitTime, TimeUnit.NANOSECONDS);
                    if (seed != null) {
                        initialSeedUniquifier = (((long)seed[0] & 0xFFL) << 56 | ((long)seed[1] & 0xFFL) << 48 | ((long)seed[2] & 0xFFL) << 40 | ((long)seed[3] & 0xFFL) << 32 | ((long)seed[4] & 0xFFL) << 24 | ((long)seed[5] & 0xFFL) << 16 | ((long)seed[6] & 0xFFL) << 8 | ((long)seed[7] & 0xFFL));
                        break;
                    }
                    continue;
                }
                catch (InterruptedException e) {
                    interrupted = true;
                    ThreadLocalRandom.logger.warn("Failed to generate a seed from SecureRandom due to an InterruptedException.");
                    break;
                }
            }
            initialSeedUniquifier ^= 0x3255ECDC33BAE119L;
            initialSeedUniquifier = (ThreadLocalRandom.initialSeedUniquifier = (initialSeedUniquifier ^ Long.reverse(System.nanoTime())));
            if (interrupted) {
                Thread.currentThread().interrupt();
                generatorThread.interrupt();
            }
        }
        return initialSeedUniquifier;
    }
    
    private static long newSeed() {
        final long startTime = System.nanoTime();
        long current;
        long actualCurrent;
        long next;
        do {
            current = ThreadLocalRandom.seedUniquifier.get();
            actualCurrent = ((current != 0L) ? current : getInitialSeedUniquifier());
            next = actualCurrent * 181783497276652981L;
        } while (!ThreadLocalRandom.seedUniquifier.compareAndSet(current, next));
        if (current == 0L && ThreadLocalRandom.logger.isDebugEnabled()) {
            ThreadLocalRandom.logger.debug(String.format("-Dio.netty.initialSeedUniquifier: 0x%016x (took %d ms)", actualCurrent, TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime)));
        }
        return next ^ System.nanoTime();
    }
    
    ThreadLocalRandom() {
        super(newSeed());
        this.initialized = true;
    }
    
    public static ThreadLocalRandom current() {
        return InternalThreadLocalMap.get().random();
    }
    
    @Override
    public void setSeed(final long seed) {
        if (this.initialized) {
            throw new UnsupportedOperationException();
        }
        this.rnd = ((seed ^ 0x5DEECE66DL) & 0xFFFFFFFFFFFFL);
    }
    
    @Override
    protected int next(final int bits) {
        this.rnd = (this.rnd * 25214903917L + 11L & 0xFFFFFFFFFFFFL);
        return (int)(this.rnd >>> 48 - bits);
    }
    
    public int nextInt(final int least, final int bound) {
        if (least >= bound) {
            throw new IllegalArgumentException();
        }
        return this.nextInt(bound - least) + least;
    }
    
    public long nextLong(long n) {
        if (n <= 0L) {
            throw new IllegalArgumentException("n must be positive");
        }
        long offset = 0L;
        while (n >= 2147483647L) {
            final int bits = this.next(2);
            final long half = n >>> 1;
            final long nextn = ((bits & 0x2) == 0x0) ? half : (n - half);
            if ((bits & 0x1) == 0x0) {
                offset += n - nextn;
            }
            n = nextn;
        }
        return offset + this.nextInt((int)n);
    }
    
    public long nextLong(final long least, final long bound) {
        if (least >= bound) {
            throw new IllegalArgumentException();
        }
        return this.nextLong(bound - least) + least;
    }
    
    public double nextDouble(final double n) {
        if (n <= 0.0) {
            throw new IllegalArgumentException("n must be positive");
        }
        return this.nextDouble() * n;
    }
    
    public double nextDouble(final double least, final double bound) {
        if (least >= bound) {
            throw new IllegalArgumentException();
        }
        return this.nextDouble() * (bound - least) + least;
    }
    
    static {
        logger = InternalLoggerFactory.getInstance(ThreadLocalRandom.class);
        seedUniquifier = new AtomicLong();
    }
}
