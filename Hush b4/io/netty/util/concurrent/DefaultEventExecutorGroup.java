// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.concurrent;

import java.util.concurrent.ThreadFactory;

public class DefaultEventExecutorGroup extends MultithreadEventExecutorGroup
{
    public DefaultEventExecutorGroup(final int nThreads) {
        this(nThreads, null);
    }
    
    public DefaultEventExecutorGroup(final int nThreads, final ThreadFactory threadFactory) {
        super(nThreads, threadFactory, new Object[0]);
    }
    
    @Override
    protected EventExecutor newChild(final ThreadFactory threadFactory, final Object... args) throws Exception {
        return new DefaultEventExecutor(this, threadFactory);
    }
}
