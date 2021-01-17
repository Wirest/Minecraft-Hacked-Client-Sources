// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import java.util.Iterator;
import com.google.common.collect.Sets;
import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Level;
import com.google.common.collect.MapMaker;
import java.util.List;
import com.google.common.annotations.VisibleForTesting;
import java.util.EnumMap;
import java.util.Collections;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.concurrent.ThreadSafe;
import com.google.common.annotations.Beta;

@Beta
@ThreadSafe
public class CycleDetectingLockFactory
{
    private static final ConcurrentMap<Class<? extends Enum>, Map<? extends Enum, LockGraphNode>> lockGraphNodesPerType;
    private static final Logger logger;
    final Policy policy;
    private static final ThreadLocal<ArrayList<LockGraphNode>> acquiredLocks;
    
    public static CycleDetectingLockFactory newInstance(final Policy policy) {
        return new CycleDetectingLockFactory(policy);
    }
    
    public ReentrantLock newReentrantLock(final String lockName) {
        return this.newReentrantLock(lockName, false);
    }
    
    public ReentrantLock newReentrantLock(final String lockName, final boolean fair) {
        return (this.policy == Policies.DISABLED) ? new ReentrantLock(fair) : new CycleDetectingReentrantLock(new LockGraphNode(lockName), fair);
    }
    
    public ReentrantReadWriteLock newReentrantReadWriteLock(final String lockName) {
        return this.newReentrantReadWriteLock(lockName, false);
    }
    
    public ReentrantReadWriteLock newReentrantReadWriteLock(final String lockName, final boolean fair) {
        return (this.policy == Policies.DISABLED) ? new ReentrantReadWriteLock(fair) : new CycleDetectingReentrantReadWriteLock(new LockGraphNode(lockName), fair);
    }
    
    public static <E extends Enum<E>> WithExplicitOrdering<E> newInstanceWithExplicitOrdering(final Class<E> enumClass, final Policy policy) {
        Preconditions.checkNotNull(enumClass);
        Preconditions.checkNotNull(policy);
        final Map<E, LockGraphNode> lockGraphNodes = (Map<E, LockGraphNode>)getOrCreateNodes(enumClass);
        return new WithExplicitOrdering<E>(policy, lockGraphNodes);
    }
    
    private static Map<? extends Enum, LockGraphNode> getOrCreateNodes(final Class<? extends Enum> clazz) {
        Map<? extends Enum, LockGraphNode> existing = CycleDetectingLockFactory.lockGraphNodesPerType.get(clazz);
        if (existing != null) {
            return existing;
        }
        final Map<? extends Enum, LockGraphNode> created = createNodes(clazz);
        existing = CycleDetectingLockFactory.lockGraphNodesPerType.putIfAbsent(clazz, created);
        return Objects.firstNonNull(existing, created);
    }
    
    @VisibleForTesting
    static <E extends Enum<E>> Map<E, LockGraphNode> createNodes(final Class<E> clazz) {
        final EnumMap<E, LockGraphNode> map = Maps.newEnumMap(clazz);
        final E[] keys = clazz.getEnumConstants();
        final int numKeys = keys.length;
        final ArrayList<LockGraphNode> nodes = Lists.newArrayListWithCapacity(numKeys);
        for (final E key : keys) {
            final LockGraphNode node = new LockGraphNode(getLockName(key));
            nodes.add(node);
            map.put(key, node);
        }
        for (int i = 1; i < numKeys; ++i) {
            nodes.get(i).checkAcquiredLocks(Policies.THROW, nodes.subList(0, i));
        }
        for (int i = 0; i < numKeys - 1; ++i) {
            nodes.get(i).checkAcquiredLocks(Policies.DISABLED, nodes.subList(i + 1, numKeys));
        }
        return Collections.unmodifiableMap((Map<? extends E, ? extends LockGraphNode>)map);
    }
    
    private static String getLockName(final Enum<?> rank) {
        return rank.getDeclaringClass().getSimpleName() + "." + rank.name();
    }
    
    private CycleDetectingLockFactory(final Policy policy) {
        this.policy = Preconditions.checkNotNull(policy);
    }
    
    private void aboutToAcquire(final CycleDetectingLock lock) {
        if (!lock.isAcquiredByCurrentThread()) {
            final ArrayList<LockGraphNode> acquiredLockList = CycleDetectingLockFactory.acquiredLocks.get();
            final LockGraphNode node = lock.getLockGraphNode();
            node.checkAcquiredLocks(this.policy, acquiredLockList);
            acquiredLockList.add(node);
        }
    }
    
    private void lockStateChanged(final CycleDetectingLock lock) {
        if (!lock.isAcquiredByCurrentThread()) {
            final ArrayList<LockGraphNode> acquiredLockList = CycleDetectingLockFactory.acquiredLocks.get();
            final LockGraphNode node = lock.getLockGraphNode();
            for (int i = acquiredLockList.size() - 1; i >= 0; --i) {
                if (acquiredLockList.get(i) == node) {
                    acquiredLockList.remove(i);
                    break;
                }
            }
        }
    }
    
    static {
        lockGraphNodesPerType = new MapMaker().weakKeys().makeMap();
        logger = Logger.getLogger(CycleDetectingLockFactory.class.getName());
        acquiredLocks = new ThreadLocal<ArrayList<LockGraphNode>>() {
            @Override
            protected ArrayList<LockGraphNode> initialValue() {
                return Lists.newArrayListWithCapacity(3);
            }
        };
    }
    
    @Beta
    public enum Policies implements Policy
    {
        THROW {
            @Override
            public void handlePotentialDeadlock(final PotentialDeadlockException e) {
                throw e;
            }
        }, 
        WARN {
            @Override
            public void handlePotentialDeadlock(final PotentialDeadlockException e) {
                CycleDetectingLockFactory.logger.log(Level.SEVERE, "Detected potential deadlock", e);
            }
        }, 
        DISABLED {
            @Override
            public void handlePotentialDeadlock(final PotentialDeadlockException e) {
            }
        };
    }
    
    @Beta
    public static final class WithExplicitOrdering<E extends Enum<E>> extends CycleDetectingLockFactory
    {
        private final Map<E, LockGraphNode> lockGraphNodes;
        
        @VisibleForTesting
        WithExplicitOrdering(final Policy policy, final Map<E, LockGraphNode> lockGraphNodes) {
            super(policy, null);
            this.lockGraphNodes = lockGraphNodes;
        }
        
        public ReentrantLock newReentrantLock(final E rank) {
            return this.newReentrantLock(rank, false);
        }
        
        public ReentrantLock newReentrantLock(final E rank, final boolean fair) {
            return (this.policy == Policies.DISABLED) ? new ReentrantLock(fair) : new CycleDetectingReentrantLock((LockGraphNode)this.lockGraphNodes.get(rank), fair);
        }
        
        public ReentrantReadWriteLock newReentrantReadWriteLock(final E rank) {
            return this.newReentrantReadWriteLock(rank, false);
        }
        
        public ReentrantReadWriteLock newReentrantReadWriteLock(final E rank, final boolean fair) {
            return (this.policy == Policies.DISABLED) ? new ReentrantReadWriteLock(fair) : new CycleDetectingReentrantReadWriteLock((LockGraphNode)this.lockGraphNodes.get(rank), fair);
        }
    }
    
    private static class ExampleStackTrace extends IllegalStateException
    {
        static final StackTraceElement[] EMPTY_STACK_TRACE;
        static Set<String> EXCLUDED_CLASS_NAMES;
        
        ExampleStackTrace(final LockGraphNode node1, final LockGraphNode node2) {
            super(node1.getLockName() + " -> " + node2.getLockName());
            final StackTraceElement[] origStackTrace = this.getStackTrace();
            for (int i = 0, n = origStackTrace.length; i < n; ++i) {
                if (WithExplicitOrdering.class.getName().equals(origStackTrace[i].getClassName())) {
                    this.setStackTrace(ExampleStackTrace.EMPTY_STACK_TRACE);
                    break;
                }
                if (!ExampleStackTrace.EXCLUDED_CLASS_NAMES.contains(origStackTrace[i].getClassName())) {
                    this.setStackTrace(Arrays.copyOfRange(origStackTrace, i, n));
                    break;
                }
            }
        }
        
        static {
            EMPTY_STACK_TRACE = new StackTraceElement[0];
            ExampleStackTrace.EXCLUDED_CLASS_NAMES = ImmutableSet.of(CycleDetectingLockFactory.class.getName(), ExampleStackTrace.class.getName(), LockGraphNode.class.getName());
        }
    }
    
    @Beta
    public static final class PotentialDeadlockException extends ExampleStackTrace
    {
        private final ExampleStackTrace conflictingStackTrace;
        
        private PotentialDeadlockException(final LockGraphNode node1, final LockGraphNode node2, final ExampleStackTrace conflictingStackTrace) {
            super(node1, node2);
            this.initCause(this.conflictingStackTrace = conflictingStackTrace);
        }
        
        public ExampleStackTrace getConflictingStackTrace() {
            return this.conflictingStackTrace;
        }
        
        @Override
        public String getMessage() {
            final StringBuilder message = new StringBuilder(super.getMessage());
            for (Throwable t = this.conflictingStackTrace; t != null; t = t.getCause()) {
                message.append(", ").append(t.getMessage());
            }
            return message.toString();
        }
    }
    
    private static class LockGraphNode
    {
        final Map<LockGraphNode, ExampleStackTrace> allowedPriorLocks;
        final Map<LockGraphNode, PotentialDeadlockException> disallowedPriorLocks;
        final String lockName;
        
        LockGraphNode(final String lockName) {
            this.allowedPriorLocks = (Map<LockGraphNode, ExampleStackTrace>)new MapMaker().weakKeys().makeMap();
            this.disallowedPriorLocks = (Map<LockGraphNode, PotentialDeadlockException>)new MapMaker().weakKeys().makeMap();
            this.lockName = Preconditions.checkNotNull(lockName);
        }
        
        String getLockName() {
            return this.lockName;
        }
        
        void checkAcquiredLocks(final Policy policy, final List<LockGraphNode> acquiredLocks) {
            for (int i = 0, size = acquiredLocks.size(); i < size; ++i) {
                this.checkAcquiredLock(policy, acquiredLocks.get(i));
            }
        }
        
        void checkAcquiredLock(final Policy policy, final LockGraphNode acquiredLock) {
            Preconditions.checkState(this != acquiredLock, (Object)("Attempted to acquire multiple locks with the same rank " + acquiredLock.getLockName()));
            if (this.allowedPriorLocks.containsKey(acquiredLock)) {
                return;
            }
            final PotentialDeadlockException previousDeadlockException = this.disallowedPriorLocks.get(acquiredLock);
            if (previousDeadlockException != null) {
                final PotentialDeadlockException exception = new PotentialDeadlockException(acquiredLock, this, previousDeadlockException.getConflictingStackTrace());
                policy.handlePotentialDeadlock(exception);
                return;
            }
            final Set<LockGraphNode> seen = Sets.newIdentityHashSet();
            final ExampleStackTrace path = acquiredLock.findPathTo(this, seen);
            if (path == null) {
                this.allowedPriorLocks.put(acquiredLock, new ExampleStackTrace(acquiredLock, this));
            }
            else {
                final PotentialDeadlockException exception2 = new PotentialDeadlockException(acquiredLock, this, path);
                this.disallowedPriorLocks.put(acquiredLock, exception2);
                policy.handlePotentialDeadlock(exception2);
            }
        }
        
        @Nullable
        private ExampleStackTrace findPathTo(final LockGraphNode node, final Set<LockGraphNode> seen) {
            if (!seen.add(this)) {
                return null;
            }
            ExampleStackTrace found = this.allowedPriorLocks.get(node);
            if (found != null) {
                return found;
            }
            for (final Map.Entry<LockGraphNode, ExampleStackTrace> entry : this.allowedPriorLocks.entrySet()) {
                final LockGraphNode preAcquiredLock = entry.getKey();
                found = preAcquiredLock.findPathTo(node, seen);
                if (found != null) {
                    final ExampleStackTrace path = new ExampleStackTrace(preAcquiredLock, this);
                    path.setStackTrace(entry.getValue().getStackTrace());
                    path.initCause(found);
                    return path;
                }
            }
            return null;
        }
    }
    
    final class CycleDetectingReentrantLock extends ReentrantLock implements CycleDetectingLock
    {
        private final LockGraphNode lockGraphNode;
        
        private CycleDetectingReentrantLock(final LockGraphNode lockGraphNode, final boolean fair) {
            super(fair);
            this.lockGraphNode = Preconditions.checkNotNull(lockGraphNode);
        }
        
        @Override
        public LockGraphNode getLockGraphNode() {
            return this.lockGraphNode;
        }
        
        @Override
        public boolean isAcquiredByCurrentThread() {
            return this.isHeldByCurrentThread();
        }
        
        @Override
        public void lock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                super.lock();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this);
            }
        }
        
        @Override
        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                super.lockInterruptibly();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this);
            }
        }
        
        @Override
        public boolean tryLock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                return super.tryLock();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this);
            }
        }
        
        @Override
        public boolean tryLock(final long timeout, final TimeUnit unit) throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this);
            try {
                return super.tryLock(timeout, unit);
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this);
            }
        }
        
        @Override
        public void unlock() {
            try {
                super.unlock();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this);
            }
        }
    }
    
    final class CycleDetectingReentrantReadWriteLock extends ReentrantReadWriteLock implements CycleDetectingLock
    {
        private final CycleDetectingReentrantReadLock readLock;
        private final CycleDetectingReentrantWriteLock writeLock;
        private final LockGraphNode lockGraphNode;
        
        private CycleDetectingReentrantReadWriteLock(final LockGraphNode lockGraphNode, final boolean fair) {
            super(fair);
            this.readLock = new CycleDetectingReentrantReadLock(this);
            this.writeLock = new CycleDetectingReentrantWriteLock(this);
            this.lockGraphNode = Preconditions.checkNotNull(lockGraphNode);
        }
        
        @Override
        public ReadLock readLock() {
            return this.readLock;
        }
        
        @Override
        public WriteLock writeLock() {
            return this.writeLock;
        }
        
        @Override
        public LockGraphNode getLockGraphNode() {
            return this.lockGraphNode;
        }
        
        @Override
        public boolean isAcquiredByCurrentThread() {
            return this.isWriteLockedByCurrentThread() || this.getReadHoldCount() > 0;
        }
    }
    
    private class CycleDetectingReentrantReadLock extends ReentrantReadWriteLock.ReadLock
    {
        final CycleDetectingReentrantReadWriteLock readWriteLock;
        
        CycleDetectingReentrantReadLock(final CycleDetectingReentrantReadWriteLock readWriteLock) {
            super(readWriteLock);
            this.readWriteLock = readWriteLock;
        }
        
        @Override
        public void lock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lock();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }
        
        @Override
        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lockInterruptibly();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }
        
        @Override
        public boolean tryLock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                return super.tryLock();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }
        
        @Override
        public boolean tryLock(final long timeout, final TimeUnit unit) throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                return super.tryLock(timeout, unit);
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }
        
        @Override
        public void unlock() {
            try {
                super.unlock();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }
    }
    
    private class CycleDetectingReentrantWriteLock extends ReentrantReadWriteLock.WriteLock
    {
        final CycleDetectingReentrantReadWriteLock readWriteLock;
        
        CycleDetectingReentrantWriteLock(final CycleDetectingReentrantReadWriteLock readWriteLock) {
            super(readWriteLock);
            this.readWriteLock = readWriteLock;
        }
        
        @Override
        public void lock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lock();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }
        
        @Override
        public void lockInterruptibly() throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                super.lockInterruptibly();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }
        
        @Override
        public boolean tryLock() {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                return super.tryLock();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }
        
        @Override
        public boolean tryLock(final long timeout, final TimeUnit unit) throws InterruptedException {
            CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
            try {
                return super.tryLock(timeout, unit);
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }
        
        @Override
        public void unlock() {
            try {
                super.unlock();
            }
            finally {
                CycleDetectingLockFactory.this.lockStateChanged(this.readWriteLock);
            }
        }
    }
    
    private interface CycleDetectingLock
    {
        LockGraphNode getLockGraphNode();
        
        boolean isAcquiredByCurrentThread();
    }
    
    @Beta
    @ThreadSafe
    public interface Policy
    {
        void handlePotentialDeadlock(final PotentialDeadlockException p0);
    }
}
