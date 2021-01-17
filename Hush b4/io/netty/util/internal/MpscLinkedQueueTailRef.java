// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

abstract class MpscLinkedQueueTailRef<E> extends MpscLinkedQueuePad1<E>
{
    private static final long serialVersionUID = 8717072462993327429L;
    private static final AtomicReferenceFieldUpdater<MpscLinkedQueueTailRef, MpscLinkedQueueNode> UPDATER;
    private transient volatile MpscLinkedQueueNode<E> tailRef;
    
    protected final MpscLinkedQueueNode<E> tailRef() {
        return this.tailRef;
    }
    
    protected final void setTailRef(final MpscLinkedQueueNode<E> tailRef) {
        this.tailRef = tailRef;
    }
    
    protected final MpscLinkedQueueNode<E> getAndSetTailRef(final MpscLinkedQueueNode<E> tailRef) {
        return MpscLinkedQueueTailRef.UPDATER.getAndSet(this, tailRef);
    }
    
    static {
        AtomicReferenceFieldUpdater<MpscLinkedQueueTailRef, MpscLinkedQueueNode> updater = (AtomicReferenceFieldUpdater<MpscLinkedQueueTailRef, MpscLinkedQueueNode>)PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueTailRef.class, "tailRef");
        if (updater == null) {
            updater = (AtomicReferenceFieldUpdater<MpscLinkedQueueTailRef, MpscLinkedQueueNode>)AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueTailRef.class, MpscLinkedQueueNode.class, "tailRef");
        }
        UPDATER = updater;
    }
}
