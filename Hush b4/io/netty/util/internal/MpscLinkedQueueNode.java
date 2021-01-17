// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public abstract class MpscLinkedQueueNode<T>
{
    private static final AtomicReferenceFieldUpdater<MpscLinkedQueueNode, MpscLinkedQueueNode> nextUpdater;
    private volatile MpscLinkedQueueNode<T> next;
    
    final MpscLinkedQueueNode<T> next() {
        return this.next;
    }
    
    final void setNext(final MpscLinkedQueueNode<T> newNext) {
        MpscLinkedQueueNode.nextUpdater.lazySet(this, newNext);
    }
    
    public abstract T value();
    
    protected T clearMaybe() {
        return this.value();
    }
    
    void unlink() {
        this.setNext(null);
    }
    
    static {
        AtomicReferenceFieldUpdater<MpscLinkedQueueNode, MpscLinkedQueueNode> u = (AtomicReferenceFieldUpdater<MpscLinkedQueueNode, MpscLinkedQueueNode>)PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueNode.class, "next");
        if (u == null) {
            u = (AtomicReferenceFieldUpdater<MpscLinkedQueueNode, MpscLinkedQueueNode>)AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueNode.class, MpscLinkedQueueNode.class, "next");
        }
        nextUpdater = u;
    }
}
