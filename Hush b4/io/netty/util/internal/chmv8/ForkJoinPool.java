// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal.chmv8;

import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.AccessController;
import java.lang.reflect.Field;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.List;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionException;
import java.util.Arrays;
import io.netty.util.internal.ThreadLocalRandom;
import java.security.Permission;
import sun.misc.Unsafe;
import java.util.concurrent.AbstractExecutorService;

public class ForkJoinPool extends AbstractExecutorService
{
    static final ThreadLocal<Submitter> submitters;
    public static final ForkJoinWorkerThreadFactory defaultForkJoinWorkerThreadFactory;
    private static final RuntimePermission modifyThreadPermission;
    static final ForkJoinPool common;
    static final int commonParallelism;
    private static int poolNumberSequence;
    private static final long IDLE_TIMEOUT = 2000000000L;
    private static final long FAST_IDLE_TIMEOUT = 200000000L;
    private static final long TIMEOUT_SLOP = 2000000L;
    private static final int MAX_HELP = 64;
    private static final int SEED_INCREMENT = 1640531527;
    private static final int AC_SHIFT = 48;
    private static final int TC_SHIFT = 32;
    private static final int ST_SHIFT = 31;
    private static final int EC_SHIFT = 16;
    private static final int SMASK = 65535;
    private static final int MAX_CAP = 32767;
    private static final int EVENMASK = 65534;
    private static final int SQMASK = 126;
    private static final int SHORT_SIGN = 32768;
    private static final int INT_SIGN = Integer.MIN_VALUE;
    private static final long STOP_BIT = 2147483648L;
    private static final long AC_MASK = -281474976710656L;
    private static final long TC_MASK = 281470681743360L;
    private static final long TC_UNIT = 4294967296L;
    private static final long AC_UNIT = 281474976710656L;
    private static final int UAC_SHIFT = 16;
    private static final int UTC_SHIFT = 0;
    private static final int UAC_MASK = -65536;
    private static final int UTC_MASK = 65535;
    private static final int UAC_UNIT = 65536;
    private static final int UTC_UNIT = 1;
    private static final int E_MASK = Integer.MAX_VALUE;
    private static final int E_SEQ = 65536;
    private static final int SHUTDOWN = Integer.MIN_VALUE;
    private static final int PL_LOCK = 2;
    private static final int PL_SIGNAL = 1;
    private static final int PL_SPINS = 256;
    static final int LIFO_QUEUE = 0;
    static final int FIFO_QUEUE = 1;
    static final int SHARED_QUEUE = -1;
    volatile long pad00;
    volatile long pad01;
    volatile long pad02;
    volatile long pad03;
    volatile long pad04;
    volatile long pad05;
    volatile long pad06;
    volatile long stealCount;
    volatile long ctl;
    volatile int plock;
    volatile int indexSeed;
    final short parallelism;
    final short mode;
    WorkQueue[] workQueues;
    final ForkJoinWorkerThreadFactory factory;
    final Thread.UncaughtExceptionHandler ueh;
    final String workerNamePrefix;
    volatile Object pad10;
    volatile Object pad11;
    volatile Object pad12;
    volatile Object pad13;
    volatile Object pad14;
    volatile Object pad15;
    volatile Object pad16;
    volatile Object pad17;
    volatile Object pad18;
    volatile Object pad19;
    volatile Object pad1a;
    volatile Object pad1b;
    private static final Unsafe U;
    private static final long CTL;
    private static final long PARKBLOCKER;
    private static final int ABASE;
    private static final int ASHIFT;
    private static final long STEALCOUNT;
    private static final long PLOCK;
    private static final long INDEXSEED;
    private static final long QBASE;
    private static final long QLOCK;
    
    private static void checkPermission() {
        final SecurityManager security = System.getSecurityManager();
        if (security != null) {
            security.checkPermission(ForkJoinPool.modifyThreadPermission);
        }
    }
    
    private static final synchronized int nextPoolId() {
        return ++ForkJoinPool.poolNumberSequence;
    }
    
    private int acquirePlock() {
        int spins = 256;
        int ps;
        int nps;
        while (((ps = this.plock) & 0x2) != 0x0 || !ForkJoinPool.U.compareAndSwapInt(this, ForkJoinPool.PLOCK, ps, nps = ps + 2)) {
            if (spins >= 0) {
                if (ThreadLocalRandom.current().nextInt() < 0) {
                    continue;
                }
                --spins;
            }
            else {
                if (!ForkJoinPool.U.compareAndSwapInt(this, ForkJoinPool.PLOCK, ps, ps | 0x1)) {
                    continue;
                }
                synchronized (this) {
                    if ((this.plock & 0x1) != 0x0) {
                        try {
                            this.wait();
                        }
                        catch (InterruptedException ie) {
                            try {
                                Thread.currentThread().interrupt();
                            }
                            catch (SecurityException ex) {}
                        }
                    }
                    else {
                        this.notifyAll();
                    }
                }
            }
        }
        return nps;
    }
    
    private void releasePlock(final int ps) {
        this.plock = ps;
        synchronized (this) {
            this.notifyAll();
        }
    }
    
    private void tryAddWorker() {
        long c;
        int u;
        int e;
        while ((u = (int)((c = this.ctl) >>> 32)) < 0 && (u & 0x8000) != 0x0 && (e = (int)c) >= 0) {
            final long nc = (long)((u + 1 & 0xFFFF) | (u + 65536 & 0xFFFF0000)) << 32 | (long)e;
            if (ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, c, nc)) {
                Throwable ex = null;
                ForkJoinWorkerThread wt = null;
                try {
                    final ForkJoinWorkerThreadFactory fac;
                    if ((fac = this.factory) != null && (wt = fac.newThread(this)) != null) {
                        wt.start();
                        break;
                    }
                }
                catch (Throwable rex) {
                    ex = rex;
                }
                this.deregisterWorker(wt, ex);
                break;
            }
        }
    }
    
    final WorkQueue registerWorker(final ForkJoinWorkerThread wt) {
        wt.setDaemon(true);
        final Thread.UncaughtExceptionHandler handler;
        if ((handler = this.ueh) != null) {
            wt.setUncaughtExceptionHandler(handler);
        }
        Unsafe u;
        long indexseed;
        int s;
        do {
            u = ForkJoinPool.U;
            indexseed = ForkJoinPool.INDEXSEED;
            s = this.indexSeed;
            s += 1640531527;
        } while (!u.compareAndSwapInt(this, indexseed, s, s) || s == 0);
        final WorkQueue w = new WorkQueue(this, wt, this.mode, s);
        int ps = 0;
        Label_0108: {
            if (((ps = this.plock) & 0x2) == 0x0) {
                final Unsafe u2 = ForkJoinPool.U;
                final long plock = ForkJoinPool.PLOCK;
                final int expected = ps;
                ps += 2;
                if (u2.compareAndSwapInt(this, plock, expected, ps)) {
                    break Label_0108;
                }
            }
            ps = this.acquirePlock();
        }
        final int nps = (ps & Integer.MIN_VALUE) | (ps + 2 & Integer.MAX_VALUE);
        try {
            WorkQueue[] ws;
            if ((ws = this.workQueues) != null) {
                int n = ws.length;
                int m = n - 1;
                int r = s << 1 | 0x1;
                if (ws[r &= m] != null) {
                    int probes = 0;
                    for (int step = (n <= 4) ? 2 : ((n >>> 1 & 0xFFFE) + 2); ws[r = (r + step & m)] != null; ws = (this.workQueues = Arrays.copyOf(ws, n <<= 1)), m = n - 1, probes = 0) {
                        if (++probes >= n) {}
                    }
                }
                w.poolIndex = (short)r;
                ws[w.eventCount = r] = w;
            }
        }
        finally {
            if (!ForkJoinPool.U.compareAndSwapInt(this, ForkJoinPool.PLOCK, ps, nps)) {
                this.releasePlock(nps);
            }
        }
        wt.setName(this.workerNamePrefix.concat(Integer.toString(w.poolIndex >>> 1)));
        return w;
    }
    
    final void deregisterWorker(final ForkJoinWorkerThread wt, final Throwable ex) {
        WorkQueue w = null;
        if (wt != null && (w = wt.workQueue) != null) {
            w.qlock = -1;
            long sc;
            while (!ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.STEALCOUNT, sc = this.stealCount, sc + w.nsteals)) {}
            int ps = 0;
            Label_0086: {
                if (((ps = this.plock) & 0x2) == 0x0) {
                    final Unsafe u2 = ForkJoinPool.U;
                    final long plock = ForkJoinPool.PLOCK;
                    final int expected = ps;
                    ps += 2;
                    if (u2.compareAndSwapInt(this, plock, expected, ps)) {
                        break Label_0086;
                    }
                }
                ps = this.acquirePlock();
            }
            final int nps = (ps & Integer.MIN_VALUE) | (ps + 2 & Integer.MAX_VALUE);
            try {
                final int idx = w.poolIndex;
                final WorkQueue[] ws = this.workQueues;
                if (ws != null && idx >= 0 && idx < ws.length && ws[idx] == w) {
                    ws[idx] = null;
                }
            }
            finally {
                if (!ForkJoinPool.U.compareAndSwapInt(this, ForkJoinPool.PLOCK, ps, nps)) {
                    this.releasePlock(nps);
                }
            }
        }
        long c;
        while (!ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, c = this.ctl, (c - 281474976710656L & 0xFFFF000000000000L) | (c - 4294967296L & 0xFFFF00000000L) | (c & 0xFFFFFFFFL))) {}
        if (!this.tryTerminate(false, false) && w != null && w.array != null) {
            w.cancelAll();
            int u;
            int e;
            while ((u = (int)((c = this.ctl) >>> 32)) < 0 && (e = (int)c) >= 0) {
                if (e > 0) {
                    final WorkQueue[] ws2;
                    final int i;
                    if ((ws2 = this.workQueues) == null || (i = (e & 0xFFFF)) >= ws2.length) {
                        break;
                    }
                    final WorkQueue v;
                    if ((v = ws2[i]) == null) {
                        break;
                    }
                    final long nc = (long)(v.nextWait & Integer.MAX_VALUE) | (long)(u + 65536) << 32;
                    if (v.eventCount != (e | Integer.MIN_VALUE)) {
                        break;
                    }
                    if (!ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, c, nc)) {
                        continue;
                    }
                    v.eventCount = (e + 65536 & Integer.MAX_VALUE);
                    final Thread p;
                    if ((p = v.parker) != null) {
                        ForkJoinPool.U.unpark(p);
                        break;
                    }
                    break;
                }
                else {
                    if ((short)u < 0) {
                        this.tryAddWorker();
                        break;
                    }
                    break;
                }
            }
        }
        if (ex == null) {
            ForkJoinTask.helpExpungeStaleExceptions();
        }
        else {
            ForkJoinTask.rethrow(ex);
        }
    }
    
    final void externalPush(final ForkJoinTask<?> task) {
        final Submitter z = ForkJoinPool.submitters.get();
        final int ps = this.plock;
        final WorkQueue[] ws = this.workQueues;
        final int m;
        final int r;
        final WorkQueue q;
        if (z != null && ps > 0 && ws != null && (m = ws.length - 1) >= 0 && (q = ws[m & (r = z.seed) & 0x7E]) != null && r != 0 && ForkJoinPool.U.compareAndSwapInt(q, ForkJoinPool.QLOCK, 0, 1)) {
            final ForkJoinTask<?>[] a;
            if ((a = q.array) != null) {
                final int am = a.length - 1;
                final int s;
                final int n;
                if (am > (n = (s = q.top) - q.base)) {
                    final int j = ((am & s) << ForkJoinPool.ASHIFT) + ForkJoinPool.ABASE;
                    ForkJoinPool.U.putOrderedObject(a, j, task);
                    q.top = s + 1;
                    q.qlock = 0;
                    if (n <= 1) {
                        this.signalWork(ws, q);
                    }
                    return;
                }
            }
            q.qlock = 0;
        }
        this.fullExternalPush(task);
    }
    
    private void fullExternalPush(final ForkJoinTask<?> task) {
        int r = 0;
        Submitter z = ForkJoinPool.submitters.get();
        while (true) {
            if (z == null) {
                final Unsafe u = ForkJoinPool.U;
                final long indexseed = ForkJoinPool.INDEXSEED;
                r = this.indexSeed;
                r += 1640531527;
                if (u.compareAndSwapInt(this, indexseed, r, r) && r != 0) {
                    ForkJoinPool.submitters.set(z = new Submitter(r));
                }
            }
            else if (r == 0) {
                r = z.seed;
                r ^= r << 13;
                r ^= r >>> 17;
                r = (z.seed = (r ^ r << 5));
            }
            int ps;
            if ((ps = this.plock) < 0) {
                throw new RejectedExecutionException();
            }
            WorkQueue[] ws;
            final int m;
            if (ps == 0 || (ws = this.workQueues) == null || (m = ws.length - 1) < 0) {
                final int p = this.parallelism;
                int n = (p > 1) ? (p - 1) : 1;
                n |= n >>> 1;
                n |= n >>> 2;
                n |= n >>> 4;
                n |= n >>> 8;
                n |= n >>> 16;
                n = n + 1 << 1;
                final WorkQueue[] nws = (WorkQueue[])(((ws = this.workQueues) == null || ws.length == 0) ? new WorkQueue[n] : null);
                Label_0284: {
                    if (((ps = this.plock) & 0x2) == 0x0) {
                        final Unsafe u2 = ForkJoinPool.U;
                        final long plock = ForkJoinPool.PLOCK;
                        final int expected = ps;
                        ps += 2;
                        if (u2.compareAndSwapInt(this, plock, expected, ps)) {
                            break Label_0284;
                        }
                    }
                    ps = this.acquirePlock();
                }
                if (((ws = this.workQueues) == null || ws.length == 0) && nws != null) {
                    this.workQueues = nws;
                }
                final int nps = (ps & Integer.MIN_VALUE) | (ps + 2 & Integer.MAX_VALUE);
                if (ForkJoinPool.U.compareAndSwapInt(this, ForkJoinPool.PLOCK, ps, nps)) {
                    continue;
                }
                this.releasePlock(nps);
            }
            else {
                final int k;
                WorkQueue q;
                if ((q = ws[k = (r & m & 0x7E)]) != null) {
                    if (q.qlock == 0 && ForkJoinPool.U.compareAndSwapInt(q, ForkJoinPool.QLOCK, 0, 1)) {
                        ForkJoinTask<?>[] a = q.array;
                        final int s = q.top;
                        boolean submitted = false;
                        try {
                            if ((a != null && a.length > s + 1 - q.base) || (a = q.growArray()) != null) {
                                final int j = ((a.length - 1 & s) << ForkJoinPool.ASHIFT) + ForkJoinPool.ABASE;
                                ForkJoinPool.U.putOrderedObject(a, j, task);
                                q.top = s + 1;
                                submitted = true;
                            }
                        }
                        finally {
                            q.qlock = 0;
                        }
                        if (submitted) {
                            this.signalWork(ws, q);
                            return;
                        }
                    }
                    r = 0;
                }
                else if (((ps = this.plock) & 0x2) == 0x0) {
                    q = new WorkQueue(this, null, -1, r);
                    q.poolIndex = (short)k;
                    Label_0596: {
                        if (((ps = this.plock) & 0x2) == 0x0) {
                            final Unsafe u3 = ForkJoinPool.U;
                            final long plock2 = ForkJoinPool.PLOCK;
                            final int expected2 = ps;
                            ps += 2;
                            if (u3.compareAndSwapInt(this, plock2, expected2, ps)) {
                                break Label_0596;
                            }
                        }
                        ps = this.acquirePlock();
                    }
                    if ((ws = this.workQueues) != null && k < ws.length && ws[k] == null) {
                        ws[k] = q;
                    }
                    final int nps2 = (ps & Integer.MIN_VALUE) | (ps + 2 & Integer.MAX_VALUE);
                    if (ForkJoinPool.U.compareAndSwapInt(this, ForkJoinPool.PLOCK, ps, nps2)) {
                        continue;
                    }
                    this.releasePlock(nps2);
                }
                else {
                    r = 0;
                }
            }
        }
    }
    
    final void incrementActiveCount() {
        long c;
        while (!ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, c = this.ctl, (c & 0xFFFFFFFFFFFFL) | (c & 0xFFFF000000000000L) + 281474976710656L)) {}
    }
    
    final void signalWork(final WorkQueue[] ws, final WorkQueue q) {
        long c;
        int u;
        while ((u = (int)((c = this.ctl) >>> 32)) < 0) {
            final int e;
            if ((e = (int)c) <= 0) {
                if ((short)u < 0) {
                    this.tryAddWorker();
                }
            }
            else {
                final int i;
                if (ws != null && ws.length > (i = (e & 0xFFFF))) {
                    final WorkQueue w;
                    if ((w = ws[i]) != null) {
                        final long nc = (long)(w.nextWait & Integer.MAX_VALUE) | (long)(u + 65536) << 32;
                        final int ne = e + 65536 & Integer.MAX_VALUE;
                        if (w.eventCount == (e | Integer.MIN_VALUE) && ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, c, nc)) {
                            w.eventCount = ne;
                            final Thread p;
                            if ((p = w.parker) != null) {
                                ForkJoinPool.U.unpark(p);
                            }
                        }
                        else if (q == null || q.base < q.top) {
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    final void runWorker(final WorkQueue w) {
        w.growArray();
        for (int r = w.hint; this.scan(w, r) == 0; r ^= r << 13, r ^= r >>> 17, r ^= r << 5) {}
    }
    
    private final int scan(final WorkQueue w, final int r) {
        final long c = this.ctl;
        final WorkQueue[] ws;
        final int m;
        if ((ws = this.workQueues) != null && (m = ws.length - 1) >= 0 && w != null) {
            int j = m + m + 1;
            final int ec = w.eventCount;
            WorkQueue q;
            int b;
            ForkJoinTask<?>[] a;
            while ((q = ws[r - j & m]) == null || (b = q.base) - q.top >= 0 || (a = q.array) == null) {
                if (--j < 0) {
                    final int e;
                    if ((ec | (e = (int)c)) < 0) {
                        return this.awaitWork(w, c, ec);
                    }
                    if (this.ctl == c) {
                        final long nc = (long)ec | (c - 281474976710656L & 0xFFFFFFFF00000000L);
                        w.nextWait = e;
                        w.eventCount = (ec | Integer.MIN_VALUE);
                        if (!ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, c, nc)) {
                            w.eventCount = ec;
                        }
                        return 0;
                    }
                    return 0;
                }
            }
            final long i = ((a.length - 1 & b) << ForkJoinPool.ASHIFT) + ForkJoinPool.ABASE;
            final ForkJoinTask<?> t;
            if ((t = (ForkJoinTask<?>)ForkJoinPool.U.getObjectVolatile(a, i)) != null) {
                if (ec < 0) {
                    this.helpRelease(c, ws, w, q, b);
                }
                else if (q.base == b && ForkJoinPool.U.compareAndSwapObject(a, i, t, null)) {
                    ForkJoinPool.U.putOrderedInt(q, ForkJoinPool.QBASE, b + 1);
                    if (b + 1 - q.top < 0) {
                        this.signalWork(ws, q);
                    }
                    w.runTask(t);
                }
            }
        }
        return 0;
    }
    
    private final int awaitWork(final WorkQueue w, final long c, final int ec) {
        int stat;
        if ((stat = w.qlock) >= 0 && w.eventCount == ec && this.ctl == c && !Thread.interrupted()) {
            final int e = (int)c;
            final int u = (int)(c >>> 32);
            final int d = (u >> 16) + this.parallelism;
            if (e < 0 || (d <= 0 && this.tryTerminate(false, false))) {
                final int qlock = -1;
                w.qlock = qlock;
                stat = qlock;
            }
            else {
                final int ns;
                if ((ns = w.nsteals) != 0) {
                    w.nsteals = 0;
                    long sc;
                    while (!ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.STEALCOUNT, sc = this.stealCount, sc + ns)) {}
                }
                else {
                    final long pc = (d > 0 || ec != (e | Integer.MIN_VALUE)) ? 0L : ((long)(w.nextWait & Integer.MAX_VALUE) | (long)(u + 65536) << 32);
                    long parkTime;
                    long deadline;
                    if (pc != 0L) {
                        final int dc = -(short)(c >>> 32);
                        parkTime = ((dc < 0) ? 200000000L : ((dc + 1) * 2000000000L));
                        deadline = System.nanoTime() + parkTime - 2000000L;
                    }
                    else {
                        deadline = (parkTime = 0L);
                    }
                    if (w.eventCount == ec && this.ctl == c) {
                        final Thread wt = Thread.currentThread();
                        ForkJoinPool.U.putObject(wt, ForkJoinPool.PARKBLOCKER, this);
                        w.parker = wt;
                        if (w.eventCount == ec && this.ctl == c) {
                            ForkJoinPool.U.park(false, parkTime);
                        }
                        w.parker = null;
                        ForkJoinPool.U.putObject(wt, ForkJoinPool.PARKBLOCKER, null);
                        if (parkTime != 0L && this.ctl == c && deadline - System.nanoTime() <= 0L && ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, c, pc)) {
                            final int qlock2 = -1;
                            w.qlock = qlock2;
                            stat = qlock2;
                        }
                    }
                }
            }
        }
        return stat;
    }
    
    private final void helpRelease(final long c, final WorkQueue[] ws, final WorkQueue w, final WorkQueue q, final int b) {
        final int e;
        final int i;
        final WorkQueue v;
        if (w != null && w.eventCount < 0 && (e = (int)c) > 0 && ws != null && ws.length > (i = (e & 0xFFFF)) && (v = ws[i]) != null && this.ctl == c) {
            final long nc = (long)(v.nextWait & Integer.MAX_VALUE) | (long)((int)(c >>> 32) + 65536) << 32;
            final int ne = e + 65536 & Integer.MAX_VALUE;
            if (q != null && q.base == b && w.eventCount < 0 && v.eventCount == (e | Integer.MIN_VALUE) && ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, c, nc)) {
                v.eventCount = ne;
                final Thread p;
                if ((p = v.parker) != null) {
                    ForkJoinPool.U.unpark(p);
                }
            }
        }
    }
    
    private int tryHelpStealer(final WorkQueue joiner, final ForkJoinTask<?> task) {
        int stat = 0;
        int steps = 0;
        Label_0477: {
            if (task != null && joiner != null && joiner.base - joiner.top >= 0) {
            Label_0025:
                while (true) {
                    ForkJoinTask<?> subtask = task;
                    WorkQueue j = joiner;
                    int s = 0;
                Label_0031:
                    while ((s = task.status) >= 0) {
                        final WorkQueue[] ws;
                        if ((ws = this.workQueues) == null) {
                            break Label_0477;
                        }
                        final int m;
                        if ((m = ws.length - 1) <= 0) {
                            break Label_0477;
                        }
                        WorkQueue v = null;
                        Label_0186: {
                            int h;
                            if ((v = ws[h = ((j.hint | 0x1) & m)]) == null || v.currentSteal != subtask) {
                                final int origin = h;
                                do {
                                    if (((h = (h + 2 & m)) & 0xF) == 0x1) {
                                        if (subtask.status < 0) {
                                            continue Label_0025;
                                        }
                                        if (j.currentJoin != subtask) {
                                            continue Label_0025;
                                        }
                                    }
                                    if ((v = ws[h]) != null && v.currentSteal == subtask) {
                                        j.hint = h;
                                        break Label_0186;
                                    }
                                } while (h != origin);
                                break Label_0477;
                            }
                        }
                        while (subtask.status >= 0) {
                            final int b;
                            final ForkJoinTask[] a;
                            if ((b = v.base) - v.top < 0 && (a = v.array) != null) {
                                final int i = ((a.length - 1 & b) << ForkJoinPool.ASHIFT) + ForkJoinPool.ABASE;
                                ForkJoinTask<?> t = (ForkJoinTask<?>)ForkJoinPool.U.getObjectVolatile(a, i);
                                if (subtask.status < 0 || j.currentJoin != subtask) {
                                    break;
                                }
                                if (v.currentSteal != subtask) {
                                    break;
                                }
                                stat = 1;
                                if (v.base != b) {
                                    continue;
                                }
                                if (t == null) {
                                    break Label_0477;
                                }
                                if (ForkJoinPool.U.compareAndSwapObject(a, i, t, null)) {
                                    ForkJoinPool.U.putOrderedInt(v, ForkJoinPool.QBASE, b + 1);
                                    final ForkJoinTask<?> ps = joiner.currentSteal;
                                    final int jt = joiner.top;
                                    do {
                                        (joiner.currentSteal = t).doExec();
                                    } while (task.status >= 0 && joiner.top != jt && (t = joiner.pop()) != null);
                                    joiner.currentSteal = ps;
                                    break Label_0477;
                                }
                                continue;
                            }
                            else {
                                final ForkJoinTask<?> next = v.currentJoin;
                                if (subtask.status < 0 || j.currentJoin != subtask) {
                                    break;
                                }
                                if (v.currentSteal != subtask) {
                                    break;
                                }
                                if (next == null) {
                                    break Label_0477;
                                }
                                if (++steps == 64) {
                                    break Label_0477;
                                }
                                subtask = next;
                                j = v;
                                continue Label_0031;
                            }
                        }
                        continue Label_0025;
                    }
                    stat = s;
                    break;
                }
            }
        }
        return stat;
    }
    
    private int helpComplete(final WorkQueue joiner, final CountedCompleter<?> task) {
        int s = 0;
        final WorkQueue[] ws;
        final int m;
        if ((ws = this.workQueues) != null && (m = ws.length - 1) >= 0 && joiner != null && task != null) {
            int j = joiner.poolIndex;
            final int scans = m + m + 1;
            long c = 0L;
            int k = scans;
            while ((s = task.status) >= 0) {
                if (joiner.internalPopAndExecCC(task)) {
                    k = scans;
                }
                else {
                    if ((s = task.status) < 0) {
                        break;
                    }
                    final WorkQueue q;
                    if ((q = ws[j & m]) != null && q.pollAndExecCC(task)) {
                        k = scans;
                    }
                    else if (--k < 0) {
                        if (c == (c = this.ctl)) {
                            break;
                        }
                        k = scans;
                    }
                }
                j += 2;
            }
        }
        return s;
    }
    
    final boolean tryCompensate(final long c) {
        final WorkQueue[] ws = this.workQueues;
        final int pc = this.parallelism;
        final int e = (int)c;
        final int m;
        if (ws != null && (m = ws.length - 1) >= 0 && e >= 0 && this.ctl == c) {
            final WorkQueue w = ws[e & m];
            if (e != 0 && w != null) {
                final long nc = (long)(w.nextWait & Integer.MAX_VALUE) | (c & 0xFFFFFFFF00000000L);
                final int ne = e + 65536 & Integer.MAX_VALUE;
                if (w.eventCount == (e | Integer.MIN_VALUE) && ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, c, nc)) {
                    w.eventCount = ne;
                    final Thread p;
                    if ((p = w.parker) != null) {
                        ForkJoinPool.U.unpark(p);
                    }
                    return true;
                }
            }
            else {
                final int tc;
                if ((tc = (short)(c >>> 32)) >= 0 && (int)(c >> 48) + pc > 1) {
                    final long nc2 = (c - 281474976710656L & 0xFFFF000000000000L) | (c & 0xFFFFFFFFFFFFL);
                    if (ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, c, nc2)) {
                        return true;
                    }
                }
                else if (tc + pc < 32767) {
                    final long nc2 = (c + 4294967296L & 0xFFFF00000000L) | (c & 0xFFFF0000FFFFFFFFL);
                    if (ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, c, nc2)) {
                        Throwable ex = null;
                        ForkJoinWorkerThread wt = null;
                        try {
                            final ForkJoinWorkerThreadFactory fac;
                            if ((fac = this.factory) != null && (wt = fac.newThread(this)) != null) {
                                wt.start();
                                return true;
                            }
                        }
                        catch (Throwable rex) {
                            ex = rex;
                        }
                        this.deregisterWorker(wt, ex);
                    }
                }
            }
        }
        return false;
    }
    
    final int awaitJoin(final WorkQueue joiner, final ForkJoinTask<?> task) {
        int s = 0;
        if (task != null && (s = task.status) >= 0 && joiner != null) {
            final ForkJoinTask<?> prevJoin = joiner.currentJoin;
            joiner.currentJoin = task;
            while (joiner.tryRemoveAndExec(task) && (s = task.status) >= 0) {}
            if (s >= 0 && task instanceof CountedCompleter) {
                s = this.helpComplete(joiner, (CountedCompleter)task);
            }
            long cc = 0L;
            while (s >= 0 && (s = task.status) >= 0) {
                if ((s = this.tryHelpStealer(joiner, task)) == 0 && (s = task.status) >= 0) {
                    if (!this.tryCompensate(cc)) {
                        cc = this.ctl;
                    }
                    else {
                        if (task.trySetSignal() && (s = task.status) >= 0) {
                            synchronized (task) {
                                if (task.status >= 0) {
                                    try {
                                        task.wait();
                                    }
                                    catch (InterruptedException ie) {}
                                }
                                else {
                                    task.notifyAll();
                                }
                            }
                        }
                        long c;
                        while (!ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, c = this.ctl, (c & 0xFFFFFFFFFFFFL) | (c & 0xFFFF000000000000L) + 281474976710656L)) {}
                    }
                }
            }
            joiner.currentJoin = prevJoin;
        }
        return s;
    }
    
    final void helpJoinOnce(final WorkQueue joiner, final ForkJoinTask<?> task) {
        int s;
        if (joiner != null && task != null && (s = task.status) >= 0) {
            final ForkJoinTask<?> prevJoin = joiner.currentJoin;
            joiner.currentJoin = task;
            while (joiner.tryRemoveAndExec(task) && (s = task.status) >= 0) {}
            if (s >= 0) {
                if (task instanceof CountedCompleter) {
                    this.helpComplete(joiner, (CountedCompleter)task);
                }
                while (task.status >= 0 && this.tryHelpStealer(joiner, task) > 0) {}
            }
            joiner.currentJoin = prevJoin;
        }
    }
    
    private WorkQueue findNonEmptyStealQueue() {
        final int r = ThreadLocalRandom.current().nextInt();
        while (true) {
            final int ps = this.plock;
            final WorkQueue[] ws;
            final int m;
            if ((ws = this.workQueues) != null && (m = ws.length - 1) >= 0) {
                for (int j = m + 1 << 2; j >= 0; --j) {
                    final WorkQueue q;
                    if ((q = ws[(r - j << 1 | 0x1) & m]) != null && q.base - q.top < 0) {
                        return q;
                    }
                }
            }
            if (this.plock == ps) {
                return null;
            }
        }
    }
    
    final void helpQuiescePool(final WorkQueue w) {
        final ForkJoinTask<?> ps = w.currentSteal;
        boolean active = true;
        while (true) {
            ForkJoinTask<?> t;
            if ((t = w.nextLocalTask()) != null) {
                t.doExec();
            }
            else {
                final WorkQueue q;
                if ((q = this.findNonEmptyStealQueue()) != null) {
                    if (!active) {
                        active = true;
                        long c;
                        while (!ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, c = this.ctl, (c & 0xFFFFFFFFFFFFL) | (c & 0xFFFF000000000000L) + 281474976710656L)) {}
                    }
                    final int b;
                    if ((b = q.base) - q.top >= 0 || (t = q.pollAt(b)) == null) {
                        continue;
                    }
                    (w.currentSteal = t).doExec();
                    w.currentSteal = ps;
                }
                else if (active) {
                    final long c;
                    final long nc = ((c = this.ctl) & 0xFFFFFFFFFFFFL) | (c & 0xFFFF000000000000L) - 281474976710656L;
                    if ((int)(nc >> 48) + this.parallelism == 0) {
                        break;
                    }
                    if (!ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, c, nc)) {
                        continue;
                    }
                    active = false;
                }
                else {
                    final long c;
                    if ((int)((c = this.ctl) >> 48) + this.parallelism <= 0 && ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, c, (c & 0xFFFFFFFFFFFFL) | (c & 0xFFFF000000000000L) + 281474976710656L)) {
                        break;
                    }
                    continue;
                }
            }
        }
    }
    
    final ForkJoinTask<?> nextTaskFor(final WorkQueue w) {
        ForkJoinTask<?> t;
        while ((t = w.nextLocalTask()) == null) {
            final WorkQueue q;
            if ((q = this.findNonEmptyStealQueue()) == null) {
                return null;
            }
            final int b;
            if ((b = q.base) - q.top < 0 && (t = q.pollAt(b)) != null) {
                return t;
            }
        }
        return t;
    }
    
    static int getSurplusQueuedTaskCount() {
        final Thread t;
        if ((t = Thread.currentThread()) instanceof ForkJoinWorkerThread) {
            final ForkJoinWorkerThread wt;
            final ForkJoinPool pool;
            int p = (pool = (wt = (ForkJoinWorkerThread)t).pool).parallelism;
            final WorkQueue q;
            final int n = (q = wt.workQueue).top - q.base;
            final int a = (int)(pool.ctl >> 48) + p;
            return n - ((a > (p >>>= 1)) ? 0 : ((a > (p >>>= 1)) ? 1 : ((a > (p >>>= 1)) ? 2 : ((a > (p >>>= 1)) ? 4 : 8))));
        }
        return 0;
    }
    
    private boolean tryTerminate(final boolean now, final boolean enable) {
        if (this == ForkJoinPool.common) {
            return false;
        }
        int ps;
        if ((ps = this.plock) >= 0) {
            if (!enable) {
                return false;
            }
            Label_0053: {
                if ((ps & 0x2) == 0x0) {
                    final Unsafe u = ForkJoinPool.U;
                    final long plock = ForkJoinPool.PLOCK;
                    final int expected = ps;
                    ps += 2;
                    if (u.compareAndSwapInt(this, plock, expected, ps)) {
                        break Label_0053;
                    }
                }
                ps = this.acquirePlock();
            }
            final int nps = (ps + 2 & Integer.MAX_VALUE) | Integer.MIN_VALUE;
            if (!ForkJoinPool.U.compareAndSwapInt(this, ForkJoinPool.PLOCK, ps, nps)) {
                this.releasePlock(nps);
            }
        }
        long c;
        while (((c = this.ctl) & 0x80000000L) == 0x0L) {
            if (!now) {
                if ((int)(c >> 48) + this.parallelism > 0) {
                    return false;
                }
                final WorkQueue[] ws;
                if ((ws = this.workQueues) != null) {
                    for (int i = 0; i < ws.length; ++i) {
                        final WorkQueue w;
                        if ((w = ws[i]) != null && (!w.isEmpty() || ((i & 0x1) != 0x0 && w.eventCount >= 0))) {
                            this.signalWork(ws, w);
                            return false;
                        }
                    }
                }
            }
            if (ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, c, c | 0x80000000L)) {
                for (int pass = 0; pass < 3; ++pass) {
                    final WorkQueue[] ws2;
                    if ((ws2 = this.workQueues) != null) {
                        final int n = ws2.length;
                        for (int j = 0; j < n; ++j) {
                            final WorkQueue w2;
                            if ((w2 = ws2[j]) != null) {
                                w2.qlock = -1;
                                if (pass > 0) {
                                    w2.cancelAll();
                                    final Thread wt;
                                    if (pass > 1 && (wt = w2.owner) != null) {
                                        if (!wt.isInterrupted()) {
                                            try {
                                                wt.interrupt();
                                            }
                                            catch (Throwable t) {}
                                        }
                                        ForkJoinPool.U.unpark(wt);
                                    }
                                }
                            }
                        }
                        int j;
                        WorkQueue w2;
                        long cc;
                        int e;
                        while ((e = ((int)(cc = this.ctl) & Integer.MAX_VALUE)) != 0 && (j = (e & 0xFFFF)) < n && j >= 0 && (w2 = ws2[j]) != null) {
                            final long nc = (long)(w2.nextWait & Integer.MAX_VALUE) | (cc + 281474976710656L & 0xFFFF000000000000L) | (cc & 0xFFFF80000000L);
                            if (w2.eventCount == (e | Integer.MIN_VALUE) && ForkJoinPool.U.compareAndSwapLong(this, ForkJoinPool.CTL, cc, nc)) {
                                w2.eventCount = (e + 65536 & Integer.MAX_VALUE);
                                w2.qlock = -1;
                                final Thread p;
                                if ((p = w2.parker) == null) {
                                    continue;
                                }
                                ForkJoinPool.U.unpark(p);
                            }
                        }
                    }
                }
            }
        }
        if ((short)(c >>> 32) + this.parallelism <= 0) {
            synchronized (this) {
                this.notifyAll();
            }
        }
        return true;
    }
    
    static WorkQueue commonSubmitterQueue() {
        final Submitter z;
        final ForkJoinPool p;
        final WorkQueue[] ws;
        final int m;
        return ((z = ForkJoinPool.submitters.get()) != null && (p = ForkJoinPool.common) != null && (ws = p.workQueues) != null && (m = ws.length - 1) >= 0) ? ws[m & z.seed & 0x7E] : null;
    }
    
    final boolean tryExternalUnpush(final ForkJoinTask<?> task) {
        final Submitter z = ForkJoinPool.submitters.get();
        final WorkQueue[] ws = this.workQueues;
        boolean popped = false;
        final int m;
        final WorkQueue joiner;
        final int s;
        final ForkJoinTask<?>[] a;
        if (z != null && ws != null && (m = ws.length - 1) >= 0 && (joiner = ws[z.seed & m & 0x7E]) != null && joiner.base != (s = joiner.top) && (a = joiner.array) != null) {
            final long j = ((a.length - 1 & s - 1) << ForkJoinPool.ASHIFT) + ForkJoinPool.ABASE;
            if (ForkJoinPool.U.getObject(a, j) == task && ForkJoinPool.U.compareAndSwapInt(joiner, ForkJoinPool.QLOCK, 0, 1)) {
                if (joiner.top == s && joiner.array == a && ForkJoinPool.U.compareAndSwapObject(a, j, task, null)) {
                    joiner.top = s - 1;
                    popped = true;
                }
                joiner.qlock = 0;
            }
        }
        return popped;
    }
    
    final int externalHelpComplete(final CountedCompleter<?> task) {
        final Submitter z = ForkJoinPool.submitters.get();
        final WorkQueue[] ws = this.workQueues;
        int s = 0;
        final int m;
        int j;
        final WorkQueue joiner;
        if (z != null && ws != null && (m = ws.length - 1) >= 0 && (joiner = ws[(j = z.seed) & m & 0x7E]) != null && task != null) {
            final int scans = m + m + 1;
            long c = 0L;
            j |= 0x1;
            int k = scans;
            while ((s = task.status) >= 0) {
                if (joiner.externalPopAndExecCC(task)) {
                    k = scans;
                }
                else {
                    if ((s = task.status) < 0) {
                        break;
                    }
                    final WorkQueue q;
                    if ((q = ws[j & m]) != null && q.pollAndExecCC(task)) {
                        k = scans;
                    }
                    else if (--k < 0) {
                        if (c == (c = this.ctl)) {
                            break;
                        }
                        k = scans;
                    }
                }
                j += 2;
            }
        }
        return s;
    }
    
    public ForkJoinPool() {
        this(Math.min(32767, Runtime.getRuntime().availableProcessors()), ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, false);
    }
    
    public ForkJoinPool(final int parallelism) {
        this(parallelism, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, false);
    }
    
    public ForkJoinPool(final int parallelism, final ForkJoinWorkerThreadFactory factory, final Thread.UncaughtExceptionHandler handler, final boolean asyncMode) {
        this(checkParallelism(parallelism), checkFactory(factory), handler, asyncMode ? 1 : 0, "ForkJoinPool-" + nextPoolId() + "-worker-");
        checkPermission();
    }
    
    private static int checkParallelism(final int parallelism) {
        if (parallelism <= 0 || parallelism > 32767) {
            throw new IllegalArgumentException();
        }
        return parallelism;
    }
    
    private static ForkJoinWorkerThreadFactory checkFactory(final ForkJoinWorkerThreadFactory factory) {
        if (factory == null) {
            throw new NullPointerException();
        }
        return factory;
    }
    
    private ForkJoinPool(final int parallelism, final ForkJoinWorkerThreadFactory factory, final Thread.UncaughtExceptionHandler handler, final int mode, final String workerNamePrefix) {
        this.workerNamePrefix = workerNamePrefix;
        this.factory = factory;
        this.ueh = handler;
        this.mode = (short)mode;
        this.parallelism = (short)parallelism;
        final long np = -parallelism;
        this.ctl = ((np << 48 & 0xFFFF000000000000L) | (np << 32 & 0xFFFF00000000L));
    }
    
    public static ForkJoinPool commonPool() {
        return ForkJoinPool.common;
    }
    
    public <T> T invoke(final ForkJoinTask<T> task) {
        if (task == null) {
            throw new NullPointerException();
        }
        this.externalPush(task);
        return task.join();
    }
    
    public void execute(final ForkJoinTask<?> task) {
        if (task == null) {
            throw new NullPointerException();
        }
        this.externalPush(task);
    }
    
    @Override
    public void execute(final Runnable task) {
        if (task == null) {
            throw new NullPointerException();
        }
        ForkJoinTask<?> job;
        if (task instanceof ForkJoinTask) {
            job = (ForkJoinTask<?>)task;
        }
        else {
            job = new ForkJoinTask.RunnableExecuteAction(task);
        }
        this.externalPush(job);
    }
    
    public <T> ForkJoinTask<T> submit(final ForkJoinTask<T> task) {
        if (task == null) {
            throw new NullPointerException();
        }
        this.externalPush(task);
        return task;
    }
    
    @Override
    public <T> ForkJoinTask<T> submit(final Callable<T> task) {
        final ForkJoinTask<T> job = new ForkJoinTask.AdaptedCallable<T>((Callable<? extends T>)task);
        this.externalPush(job);
        return job;
    }
    
    @Override
    public <T> ForkJoinTask<T> submit(final Runnable task, final T result) {
        final ForkJoinTask<T> job = new ForkJoinTask.AdaptedRunnable<T>(task, result);
        this.externalPush(job);
        return job;
    }
    
    @Override
    public ForkJoinTask<?> submit(final Runnable task) {
        if (task == null) {
            throw new NullPointerException();
        }
        ForkJoinTask<?> job;
        if (task instanceof ForkJoinTask) {
            job = (ForkJoinTask<?>)task;
        }
        else {
            job = new ForkJoinTask.AdaptedRunnableAction(task);
        }
        this.externalPush(job);
        return job;
    }
    
    @Override
    public <T> List<Future<T>> invokeAll(final Collection<? extends Callable<T>> tasks) {
        final ArrayList<Future<T>> futures = new ArrayList<Future<T>>(tasks.size());
        boolean done = false;
        try {
            for (final Callable<T> t : tasks) {
                final ForkJoinTask<T> f = new ForkJoinTask.AdaptedCallable<T>((Callable<? extends T>)t);
                futures.add(f);
                this.externalPush(f);
            }
            for (int i = 0, size = futures.size(); i < size; ++i) {
                ((ForkJoinTask)futures.get(i)).quietlyJoin();
            }
            done = true;
            return futures;
        }
        finally {
            if (!done) {
                for (int j = 0, size2 = futures.size(); j < size2; ++j) {
                    futures.get(j).cancel(false);
                }
            }
        }
    }
    
    public ForkJoinWorkerThreadFactory getFactory() {
        return this.factory;
    }
    
    public Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return this.ueh;
    }
    
    public int getParallelism() {
        final int par;
        return ((par = this.parallelism) > 0) ? par : 1;
    }
    
    public static int getCommonPoolParallelism() {
        return ForkJoinPool.commonParallelism;
    }
    
    public int getPoolSize() {
        return this.parallelism + (short)(this.ctl >>> 32);
    }
    
    public boolean getAsyncMode() {
        return this.mode == 1;
    }
    
    public int getRunningThreadCount() {
        int rc = 0;
        final WorkQueue[] ws;
        if ((ws = this.workQueues) != null) {
            for (int i = 1; i < ws.length; i += 2) {
                final WorkQueue w;
                if ((w = ws[i]) != null && w.isApparentlyUnblocked()) {
                    ++rc;
                }
            }
        }
        return rc;
    }
    
    public int getActiveThreadCount() {
        final int r = this.parallelism + (int)(this.ctl >> 48);
        return (r <= 0) ? 0 : r;
    }
    
    public boolean isQuiescent() {
        return this.parallelism + (int)(this.ctl >> 48) <= 0;
    }
    
    public long getStealCount() {
        long count = this.stealCount;
        final WorkQueue[] ws;
        if ((ws = this.workQueues) != null) {
            for (int i = 1; i < ws.length; i += 2) {
                final WorkQueue w;
                if ((w = ws[i]) != null) {
                    count += w.nsteals;
                }
            }
        }
        return count;
    }
    
    public long getQueuedTaskCount() {
        long count = 0L;
        final WorkQueue[] ws;
        if ((ws = this.workQueues) != null) {
            for (int i = 1; i < ws.length; i += 2) {
                final WorkQueue w;
                if ((w = ws[i]) != null) {
                    count += w.queueSize();
                }
            }
        }
        return count;
    }
    
    public int getQueuedSubmissionCount() {
        int count = 0;
        final WorkQueue[] ws;
        if ((ws = this.workQueues) != null) {
            for (int i = 0; i < ws.length; i += 2) {
                final WorkQueue w;
                if ((w = ws[i]) != null) {
                    count += w.queueSize();
                }
            }
        }
        return count;
    }
    
    public boolean hasQueuedSubmissions() {
        final WorkQueue[] ws;
        if ((ws = this.workQueues) != null) {
            for (int i = 0; i < ws.length; i += 2) {
                final WorkQueue w;
                if ((w = ws[i]) != null && !w.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    protected ForkJoinTask<?> pollSubmission() {
        final WorkQueue[] ws;
        if ((ws = this.workQueues) != null) {
            for (int i = 0; i < ws.length; i += 2) {
                final WorkQueue w;
                final ForkJoinTask<?> t;
                if ((w = ws[i]) != null && (t = w.poll()) != null) {
                    return t;
                }
            }
        }
        return null;
    }
    
    protected int drainTasksTo(final Collection<? super ForkJoinTask<?>> c) {
        int count = 0;
        final WorkQueue[] ws;
        if ((ws = this.workQueues) != null) {
            for (int i = 0; i < ws.length; ++i) {
                final WorkQueue w;
                if ((w = ws[i]) != null) {
                    ForkJoinTask<?> t;
                    while ((t = w.poll()) != null) {
                        c.add(t);
                        ++count;
                    }
                }
            }
        }
        return count;
    }
    
    @Override
    public String toString() {
        long qt = 0L;
        long qs = 0L;
        int rc = 0;
        long st = this.stealCount;
        final long c = this.ctl;
        final WorkQueue[] ws;
        if ((ws = this.workQueues) != null) {
            for (int i = 0; i < ws.length; ++i) {
                final WorkQueue w;
                if ((w = ws[i]) != null) {
                    final int size = w.queueSize();
                    if ((i & 0x1) == 0x0) {
                        qs += size;
                    }
                    else {
                        qt += size;
                        st += w.nsteals;
                        if (w.isApparentlyUnblocked()) {
                            ++rc;
                        }
                    }
                }
            }
        }
        final int pc = this.parallelism;
        final int tc = pc + (short)(c >>> 32);
        int ac = pc + (int)(c >> 48);
        if (ac < 0) {
            ac = 0;
        }
        String level;
        if ((c & 0x80000000L) != 0x0L) {
            level = ((tc == 0) ? "Terminated" : "Terminating");
        }
        else {
            level = ((this.plock < 0) ? "Shutting down" : "Running");
        }
        return super.toString() + "[" + level + ", parallelism = " + pc + ", size = " + tc + ", active = " + ac + ", running = " + rc + ", steals = " + st + ", tasks = " + qt + ", submissions = " + qs + "]";
    }
    
    @Override
    public void shutdown() {
        checkPermission();
        this.tryTerminate(false, true);
    }
    
    @Override
    public List<Runnable> shutdownNow() {
        checkPermission();
        this.tryTerminate(true, true);
        return Collections.emptyList();
    }
    
    @Override
    public boolean isTerminated() {
        final long c = this.ctl;
        return (c & 0x80000000L) != 0x0L && (short)(c >>> 32) + this.parallelism <= 0;
    }
    
    public boolean isTerminating() {
        final long c = this.ctl;
        return (c & 0x80000000L) != 0x0L && (short)(c >>> 32) + this.parallelism > 0;
    }
    
    @Override
    public boolean isShutdown() {
        return this.plock < 0;
    }
    
    @Override
    public boolean awaitTermination(final long timeout, final TimeUnit unit) throws InterruptedException {
        if (Thread.interrupted()) {
            throw new InterruptedException();
        }
        if (this == ForkJoinPool.common) {
            this.awaitQuiescence(timeout, unit);
            return false;
        }
        long nanos = unit.toNanos(timeout);
        if (this.isTerminated()) {
            return true;
        }
        if (nanos <= 0L) {
            return false;
        }
        final long deadline = System.nanoTime() + nanos;
        synchronized (this) {
            while (!this.isTerminated()) {
                if (nanos <= 0L) {
                    return false;
                }
                final long millis = TimeUnit.NANOSECONDS.toMillis(nanos);
                this.wait((millis > 0L) ? millis : 1L);
                nanos = deadline - System.nanoTime();
            }
            return true;
        }
    }
    
    public boolean awaitQuiescence(final long timeout, final TimeUnit unit) {
        final long nanos = unit.toNanos(timeout);
        final Thread thread = Thread.currentThread();
        final ForkJoinWorkerThread wt;
        if (thread instanceof ForkJoinWorkerThread && (wt = (ForkJoinWorkerThread)thread).pool == this) {
            this.helpQuiescePool(wt.workQueue);
            return true;
        }
        final long startTime = System.nanoTime();
        int r = 0;
        boolean found = true;
        WorkQueue[] ws;
        int m;
        while (!this.isQuiescent() && (ws = this.workQueues) != null && (m = ws.length - 1) >= 0) {
            if (!found) {
                if (System.nanoTime() - startTime > nanos) {
                    return false;
                }
                Thread.yield();
            }
            found = false;
            int j = m + 1 << 2;
            while (j >= 0) {
                final WorkQueue q;
                final int b;
                if ((q = ws[r++ & m]) != null && (b = q.base) - q.top < 0) {
                    found = true;
                    final ForkJoinTask<?> t;
                    if ((t = q.pollAt(b)) != null) {
                        t.doExec();
                        break;
                    }
                    break;
                }
                else {
                    --j;
                }
            }
        }
        return true;
    }
    
    static void quiesceCommonPool() {
        ForkJoinPool.common.awaitQuiescence(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }
    
    public static void managedBlock(final ManagedBlocker blocker) throws InterruptedException {
        final Thread t = Thread.currentThread();
        if (t instanceof ForkJoinWorkerThread) {
            final ForkJoinPool p = ((ForkJoinWorkerThread)t).pool;
            while (!blocker.isReleasable()) {
                if (p.tryCompensate(p.ctl)) {
                    try {
                        while (!blocker.isReleasable() && !blocker.block()) {}
                    }
                    finally {
                        p.incrementActiveCount();
                    }
                    break;
                }
            }
        }
        else {
            while (!blocker.isReleasable() && !blocker.block()) {}
        }
    }
    
    @Override
    protected <T> RunnableFuture<T> newTaskFor(final Runnable runnable, final T value) {
        return new ForkJoinTask.AdaptedRunnable<T>(runnable, value);
    }
    
    @Override
    protected <T> RunnableFuture<T> newTaskFor(final Callable<T> callable) {
        return new ForkJoinTask.AdaptedCallable<T>((Callable<? extends T>)callable);
    }
    
    private static ForkJoinPool makeCommonPool() {
        int parallelism = -1;
        ForkJoinWorkerThreadFactory factory = ForkJoinPool.defaultForkJoinWorkerThreadFactory;
        Thread.UncaughtExceptionHandler handler = null;
        try {
            final String pp = System.getProperty("java.util.concurrent.ForkJoinPool.common.parallelism");
            final String fp = System.getProperty("java.util.concurrent.ForkJoinPool.common.threadFactory");
            final String hp = System.getProperty("java.util.concurrent.ForkJoinPool.common.exceptionHandler");
            if (pp != null) {
                parallelism = Integer.parseInt(pp);
            }
            if (fp != null) {
                factory = (ForkJoinWorkerThreadFactory)ClassLoader.getSystemClassLoader().loadClass(fp).newInstance();
            }
            if (hp != null) {
                handler = (Thread.UncaughtExceptionHandler)ClassLoader.getSystemClassLoader().loadClass(hp).newInstance();
            }
        }
        catch (Exception ex) {}
        if (parallelism < 0 && (parallelism = Runtime.getRuntime().availableProcessors() - 1) < 0) {
            parallelism = 0;
        }
        if (parallelism > 32767) {
            parallelism = 32767;
        }
        return new ForkJoinPool(parallelism, factory, handler, 0, "ForkJoinPool.commonPool-worker-");
    }
    
    private static Unsafe getUnsafe() {
        try {
            return Unsafe.getUnsafe();
        }
        catch (SecurityException tryReflectionInstead) {
            try {
                return AccessController.doPrivileged((PrivilegedExceptionAction<Unsafe>)new PrivilegedExceptionAction<Unsafe>() {
                    @Override
                    public Unsafe run() throws Exception {
                        final Class<Unsafe> k = Unsafe.class;
                        for (final Field f : k.getDeclaredFields()) {
                            f.setAccessible(true);
                            final Object x = f.get(null);
                            if (k.isInstance(x)) {
                                return k.cast(x);
                            }
                        }
                        throw new NoSuchFieldError("the Unsafe");
                    }
                });
            }
            catch (PrivilegedActionException e) {
                throw new RuntimeException("Could not initialize intrinsics", e.getCause());
            }
        }
    }
    
    static {
        try {
            U = getUnsafe();
            final Class<?> k = ForkJoinPool.class;
            CTL = ForkJoinPool.U.objectFieldOffset(k.getDeclaredField("ctl"));
            STEALCOUNT = ForkJoinPool.U.objectFieldOffset(k.getDeclaredField("stealCount"));
            PLOCK = ForkJoinPool.U.objectFieldOffset(k.getDeclaredField("plock"));
            INDEXSEED = ForkJoinPool.U.objectFieldOffset(k.getDeclaredField("indexSeed"));
            final Class<?> tk = Thread.class;
            PARKBLOCKER = ForkJoinPool.U.objectFieldOffset(tk.getDeclaredField("parkBlocker"));
            final Class<?> wk = WorkQueue.class;
            QBASE = ForkJoinPool.U.objectFieldOffset(wk.getDeclaredField("base"));
            QLOCK = ForkJoinPool.U.objectFieldOffset(wk.getDeclaredField("qlock"));
            final Class<?> ak = ForkJoinTask[].class;
            ABASE = ForkJoinPool.U.arrayBaseOffset(ak);
            final int scale = ForkJoinPool.U.arrayIndexScale(ak);
            if ((scale & scale - 1) != 0x0) {
                throw new Error("data type scale not a power of two");
            }
            ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
        }
        catch (Exception e) {
            throw new Error(e);
        }
        submitters = new ThreadLocal<Submitter>();
        defaultForkJoinWorkerThreadFactory = new DefaultForkJoinWorkerThreadFactory();
        modifyThreadPermission = new RuntimePermission("modifyThread");
        common = AccessController.doPrivileged((PrivilegedAction<ForkJoinPool>)new PrivilegedAction<ForkJoinPool>() {
            @Override
            public ForkJoinPool run() {
                return makeCommonPool();
            }
        });
        final int par = ForkJoinPool.common.parallelism;
        commonParallelism = ((par > 0) ? par : 1);
    }
    
    static final class DefaultForkJoinWorkerThreadFactory implements ForkJoinWorkerThreadFactory
    {
        @Override
        public final ForkJoinWorkerThread newThread(final ForkJoinPool pool) {
            return new ForkJoinWorkerThread(pool);
        }
    }
    
    static final class EmptyTask extends ForkJoinTask<Void>
    {
        private static final long serialVersionUID = -7721805057305804111L;
        
        EmptyTask() {
            this.status = -268435456;
        }
        
        @Override
        public final Void getRawResult() {
            return null;
        }
        
        public final void setRawResult(final Void x) {
        }
        
        public final boolean exec() {
            return true;
        }
    }
    
    static final class WorkQueue
    {
        static final int INITIAL_QUEUE_CAPACITY = 8192;
        static final int MAXIMUM_QUEUE_CAPACITY = 67108864;
        volatile long pad00;
        volatile long pad01;
        volatile long pad02;
        volatile long pad03;
        volatile long pad04;
        volatile long pad05;
        volatile long pad06;
        volatile int eventCount;
        int nextWait;
        int nsteals;
        int hint;
        short poolIndex;
        final short mode;
        volatile int qlock;
        volatile int base;
        int top;
        ForkJoinTask<?>[] array;
        final ForkJoinPool pool;
        final ForkJoinWorkerThread owner;
        volatile Thread parker;
        volatile ForkJoinTask<?> currentJoin;
        ForkJoinTask<?> currentSteal;
        volatile Object pad10;
        volatile Object pad11;
        volatile Object pad12;
        volatile Object pad13;
        volatile Object pad14;
        volatile Object pad15;
        volatile Object pad16;
        volatile Object pad17;
        volatile Object pad18;
        volatile Object pad19;
        volatile Object pad1a;
        volatile Object pad1b;
        volatile Object pad1c;
        volatile Object pad1d;
        private static final Unsafe U;
        private static final long QBASE;
        private static final long QLOCK;
        private static final int ABASE;
        private static final int ASHIFT;
        
        WorkQueue(final ForkJoinPool pool, final ForkJoinWorkerThread owner, final int mode, final int seed) {
            this.pool = pool;
            this.owner = owner;
            this.mode = (short)mode;
            this.hint = seed;
            final int n = 4096;
            this.top = n;
            this.base = n;
        }
        
        final int queueSize() {
            final int n = this.base - this.top;
            return (n >= 0) ? 0 : (-n);
        }
        
        final boolean isEmpty() {
            final int s;
            final int n = this.base - (s = this.top);
            final ForkJoinTask<?>[] a;
            final int m;
            return n >= 0 || (n == -1 && ((a = this.array) == null || (m = a.length - 1) < 0 || WorkQueue.U.getObject(a, ((m & s - 1) << WorkQueue.ASHIFT) + (long)WorkQueue.ABASE) == null));
        }
        
        final void push(final ForkJoinTask<?> task) {
            final int s = this.top;
            final ForkJoinTask<?>[] a;
            if ((a = this.array) != null) {
                final int m = a.length - 1;
                WorkQueue.U.putOrderedObject(a, ((m & s) << WorkQueue.ASHIFT) + WorkQueue.ABASE, task);
                final int top = s + 1;
                this.top = top;
                final int n;
                if ((n = top - this.base) <= 2) {
                    final ForkJoinPool p;
                    (p = this.pool).signalWork(p.workQueues, this);
                }
                else if (n >= m) {
                    this.growArray();
                }
            }
        }
        
        final ForkJoinTask<?>[] growArray() {
            final ForkJoinTask<?>[] oldA = this.array;
            final int size = (oldA != null) ? (oldA.length << 1) : 8192;
            if (size > 67108864) {
                throw new RejectedExecutionException("Queue capacity exceeded");
            }
            final ForkJoinTask<?>[] array = (ForkJoinTask<?>[])new ForkJoinTask[size];
            this.array = array;
            final ForkJoinTask<?>[] a = array;
            final int oldMask;
            if (oldA != null && (oldMask = oldA.length - 1) >= 0) {
                final int t = this.top;
                int b;
                if (t - (b = this.base) > 0) {
                    final int mask = size - 1;
                    do {
                        final int oldj = ((b & oldMask) << WorkQueue.ASHIFT) + WorkQueue.ABASE;
                        final int j = ((b & mask) << WorkQueue.ASHIFT) + WorkQueue.ABASE;
                        final ForkJoinTask<?> x = (ForkJoinTask<?>)WorkQueue.U.getObjectVolatile(oldA, oldj);
                        if (x != null && WorkQueue.U.compareAndSwapObject(oldA, oldj, x, null)) {
                            WorkQueue.U.putObjectVolatile(a, j, x);
                        }
                    } while (++b != t);
                }
            }
            return a;
        }
        
        final ForkJoinTask<?> pop() {
            final ForkJoinTask<?>[] a;
            final int m;
            if ((a = this.array) != null && (m = a.length - 1) >= 0) {
                int s;
                while ((s = this.top - 1) - this.base >= 0) {
                    final long j = ((m & s) << WorkQueue.ASHIFT) + WorkQueue.ABASE;
                    final ForkJoinTask<?> t;
                    if ((t = (ForkJoinTask<?>)WorkQueue.U.getObject(a, j)) == null) {
                        break;
                    }
                    if (WorkQueue.U.compareAndSwapObject(a, j, t, null)) {
                        this.top = s;
                        return t;
                    }
                }
            }
            return null;
        }
        
        final ForkJoinTask<?> pollAt(final int b) {
            final ForkJoinTask<?>[] a;
            if ((a = this.array) != null) {
                final int j = ((a.length - 1 & b) << WorkQueue.ASHIFT) + WorkQueue.ABASE;
                final ForkJoinTask<?> t;
                if ((t = (ForkJoinTask<?>)WorkQueue.U.getObjectVolatile(a, j)) != null && this.base == b && WorkQueue.U.compareAndSwapObject(a, j, t, null)) {
                    WorkQueue.U.putOrderedInt(this, WorkQueue.QBASE, b + 1);
                    return t;
                }
            }
            return null;
        }
        
        final ForkJoinTask<?> poll() {
            int b;
            ForkJoinTask<?>[] a;
            while ((b = this.base) - this.top < 0 && (a = this.array) != null) {
                final int j = ((a.length - 1 & b) << WorkQueue.ASHIFT) + WorkQueue.ABASE;
                final ForkJoinTask<?> t = (ForkJoinTask<?>)WorkQueue.U.getObjectVolatile(a, j);
                if (t != null) {
                    if (WorkQueue.U.compareAndSwapObject(a, j, t, null)) {
                        WorkQueue.U.putOrderedInt(this, WorkQueue.QBASE, b + 1);
                        return t;
                    }
                    continue;
                }
                else {
                    if (this.base != b) {
                        continue;
                    }
                    if (b + 1 == this.top) {
                        break;
                    }
                    Thread.yield();
                }
            }
            return null;
        }
        
        final ForkJoinTask<?> nextLocalTask() {
            return (this.mode == 0) ? this.pop() : this.poll();
        }
        
        final ForkJoinTask<?> peek() {
            final ForkJoinTask<?>[] a = this.array;
            final int m;
            if (a == null || (m = a.length - 1) < 0) {
                return null;
            }
            final int i = (this.mode == 0) ? (this.top - 1) : this.base;
            final int j = ((i & m) << WorkQueue.ASHIFT) + WorkQueue.ABASE;
            return (ForkJoinTask<?>)WorkQueue.U.getObjectVolatile(a, j);
        }
        
        final boolean tryUnpush(final ForkJoinTask<?> t) {
            final ForkJoinTask<?>[] a;
            int s;
            if ((a = this.array) != null && (s = this.top) != this.base && WorkQueue.U.compareAndSwapObject(a, ((a.length - 1 & --s) << WorkQueue.ASHIFT) + WorkQueue.ABASE, t, null)) {
                this.top = s;
                return true;
            }
            return false;
        }
        
        final void cancelAll() {
            ForkJoinTask.cancelIgnoringExceptions(this.currentJoin);
            ForkJoinTask.cancelIgnoringExceptions(this.currentSteal);
            ForkJoinTask<?> t;
            while ((t = this.poll()) != null) {
                ForkJoinTask.cancelIgnoringExceptions(t);
            }
        }
        
        final void pollAndExecAll() {
            ForkJoinTask<?> t;
            while ((t = this.poll()) != null) {
                t.doExec();
            }
        }
        
        final void runTask(final ForkJoinTask<?> task) {
            this.currentSteal = task;
            if (task != null) {
                task.doExec();
                final ForkJoinTask<?>[] a = this.array;
                final int md = this.mode;
                ++this.nsteals;
                this.currentSteal = null;
                if (md != 0) {
                    this.pollAndExecAll();
                }
                else if (a != null) {
                    final int m = a.length - 1;
                    int s;
                    while ((s = this.top - 1) - this.base >= 0) {
                        final long i = ((m & s) << WorkQueue.ASHIFT) + WorkQueue.ABASE;
                        final ForkJoinTask<?> t = (ForkJoinTask<?>)WorkQueue.U.getObject(a, i);
                        if (t == null) {
                            break;
                        }
                        if (!WorkQueue.U.compareAndSwapObject(a, i, t, null)) {
                            continue;
                        }
                        this.top = s;
                        t.doExec();
                    }
                }
            }
        }
        
        final boolean tryRemoveAndExec(final ForkJoinTask<?> task) {
            final ForkJoinTask<?>[] a;
            final int m;
            int s;
            final int b;
            int n;
            boolean stat;
            if (task != null && (a = this.array) != null && (m = a.length - 1) >= 0 && (n = (s = this.top) - (b = this.base)) > 0) {
                boolean removed = false;
                boolean empty = true;
                stat = true;
                while (true) {
                    final long j = ((--s & m) << WorkQueue.ASHIFT) + WorkQueue.ABASE;
                    final ForkJoinTask<?> t = (ForkJoinTask<?>)WorkQueue.U.getObject(a, j);
                    if (t == null) {
                        break;
                    }
                    if (t == task) {
                        if (s + 1 == this.top) {
                            if (!WorkQueue.U.compareAndSwapObject(a, j, task, null)) {
                                break;
                            }
                            this.top = s;
                            removed = true;
                            break;
                        }
                        else {
                            if (this.base == b) {
                                removed = WorkQueue.U.compareAndSwapObject(a, j, task, new EmptyTask());
                                break;
                            }
                            break;
                        }
                    }
                    else {
                        if (t.status >= 0) {
                            empty = false;
                        }
                        else if (s + 1 == this.top) {
                            if (WorkQueue.U.compareAndSwapObject(a, j, t, null)) {
                                this.top = s;
                                break;
                            }
                            break;
                        }
                        if (--n != 0) {
                            continue;
                        }
                        if (!empty && this.base == b) {
                            stat = false;
                            break;
                        }
                        break;
                    }
                }
                if (removed) {
                    task.doExec();
                }
            }
            else {
                stat = false;
            }
            return stat;
        }
        
        final boolean pollAndExecCC(final CountedCompleter<?> root) {
            final int b;
            final ForkJoinTask<?>[] a;
            if ((b = this.base) - this.top < 0 && (a = this.array) != null) {
                final long j = ((a.length - 1 & b) << WorkQueue.ASHIFT) + WorkQueue.ABASE;
                final Object o;
                if ((o = WorkQueue.U.getObjectVolatile(a, j)) == null) {
                    return true;
                }
                if (o instanceof CountedCompleter) {
                    CountedCompleter<?> r;
                    final CountedCompleter<?> t = r = (CountedCompleter<?>)o;
                    while (r != root) {
                        if ((r = r.completer) == null) {
                            return false;
                        }
                    }
                    if (this.base == b && WorkQueue.U.compareAndSwapObject(a, j, t, null)) {
                        WorkQueue.U.putOrderedInt(this, WorkQueue.QBASE, b + 1);
                        t.doExec();
                    }
                    return true;
                }
            }
            return false;
        }
        
        final boolean externalPopAndExecCC(final CountedCompleter<?> root) {
            final int s;
            final ForkJoinTask<?>[] a;
            if (this.base - (s = this.top) < 0 && (a = this.array) != null) {
                final long j = ((a.length - 1 & s - 1) << WorkQueue.ASHIFT) + WorkQueue.ABASE;
                final Object o;
                if ((o = WorkQueue.U.getObject(a, j)) instanceof CountedCompleter) {
                    CountedCompleter<?> r;
                    final CountedCompleter<?> t = r = (CountedCompleter<?>)o;
                    while (r != root) {
                        if ((r = r.completer) == null) {
                            return false;
                        }
                    }
                    if (WorkQueue.U.compareAndSwapInt(this, WorkQueue.QLOCK, 0, 1)) {
                        if (this.top == s && this.array == a && WorkQueue.U.compareAndSwapObject(a, j, t, null)) {
                            this.top = s - 1;
                            this.qlock = 0;
                            t.doExec();
                        }
                        else {
                            this.qlock = 0;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        
        final boolean internalPopAndExecCC(final CountedCompleter<?> root) {
            final int s;
            final ForkJoinTask<?>[] a;
            if (this.base - (s = this.top) < 0 && (a = this.array) != null) {
                final long j = ((a.length - 1 & s - 1) << WorkQueue.ASHIFT) + WorkQueue.ABASE;
                final Object o;
                if ((o = WorkQueue.U.getObject(a, j)) instanceof CountedCompleter) {
                    CountedCompleter<?> r;
                    final CountedCompleter<?> t = r = (CountedCompleter<?>)o;
                    while (r != root) {
                        if ((r = r.completer) == null) {
                            return false;
                        }
                    }
                    if (WorkQueue.U.compareAndSwapObject(a, j, t, null)) {
                        this.top = s - 1;
                        t.doExec();
                    }
                    return true;
                }
            }
            return false;
        }
        
        final boolean isApparentlyUnblocked() {
            final Thread wt;
            final Thread.State s;
            return this.eventCount >= 0 && (wt = this.owner) != null && (s = wt.getState()) != Thread.State.BLOCKED && s != Thread.State.WAITING && s != Thread.State.TIMED_WAITING;
        }
        
        static {
            try {
                U = getUnsafe();
                final Class<?> k = WorkQueue.class;
                final Class<?> ak = ForkJoinTask[].class;
                QBASE = WorkQueue.U.objectFieldOffset(k.getDeclaredField("base"));
                QLOCK = WorkQueue.U.objectFieldOffset(k.getDeclaredField("qlock"));
                ABASE = WorkQueue.U.arrayBaseOffset(ak);
                final int scale = WorkQueue.U.arrayIndexScale(ak);
                if ((scale & scale - 1) != 0x0) {
                    throw new Error("data type scale not a power of two");
                }
                ASHIFT = 31 - Integer.numberOfLeadingZeros(scale);
            }
            catch (Exception e) {
                throw new Error(e);
            }
        }
    }
    
    static final class Submitter
    {
        int seed;
        
        Submitter(final int s) {
            this.seed = s;
        }
    }
    
    public interface ManagedBlocker
    {
        boolean block() throws InterruptedException;
        
        boolean isReleasable();
    }
    
    public interface ForkJoinWorkerThreadFactory
    {
        ForkJoinWorkerThread newThread(final ForkJoinPool p0);
    }
}
