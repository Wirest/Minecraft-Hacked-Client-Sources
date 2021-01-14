package io.netty.util.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

abstract class MpscLinkedQueueTailRef<E>
        extends MpscLinkedQueuePad1<E> {
    private static final long serialVersionUID = 8717072462993327429L;
    private static final AtomicReferenceFieldUpdater<MpscLinkedQueueTailRef, MpscLinkedQueueNode> UPDATER;

    static {
        AtomicReferenceFieldUpdater localAtomicReferenceFieldUpdater = PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueTailRef.class, "tailRef");
        if (localAtomicReferenceFieldUpdater == null) {
            localAtomicReferenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueTailRef.class, MpscLinkedQueueNode.class, "tailRef");
        }
        UPDATER = localAtomicReferenceFieldUpdater;
    }

    private volatile transient MpscLinkedQueueNode<E> tailRef;

    protected final MpscLinkedQueueNode<E> tailRef() {
        return this.tailRef;
    }

    protected final void setTailRef(MpscLinkedQueueNode<E> paramMpscLinkedQueueNode) {
        this.tailRef = paramMpscLinkedQueueNode;
    }

    protected final MpscLinkedQueueNode<E> getAndSetTailRef(MpscLinkedQueueNode<E> paramMpscLinkedQueueNode) {
        return (MpscLinkedQueueNode) UPDATER.getAndSet(this, paramMpscLinkedQueueNode);
    }
}




