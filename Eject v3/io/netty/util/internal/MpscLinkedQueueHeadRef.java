package io.netty.util.internal;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

abstract class MpscLinkedQueueHeadRef<E>
        extends MpscLinkedQueuePad0<E>
        implements Serializable {
    private static final long serialVersionUID = 8467054865577874285L;
    private static final AtomicReferenceFieldUpdater<MpscLinkedQueueHeadRef, MpscLinkedQueueNode> UPDATER;

    static {
        AtomicReferenceFieldUpdater localAtomicReferenceFieldUpdater = PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueHeadRef.class, "headRef");
        if (localAtomicReferenceFieldUpdater == null) {
            localAtomicReferenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueHeadRef.class, MpscLinkedQueueNode.class, "headRef");
        }
        UPDATER = localAtomicReferenceFieldUpdater;
    }

    private volatile transient MpscLinkedQueueNode<E> headRef;

    protected final MpscLinkedQueueNode<E> headRef() {
        return this.headRef;
    }

    protected final void setHeadRef(MpscLinkedQueueNode<E> paramMpscLinkedQueueNode) {
        this.headRef = paramMpscLinkedQueueNode;
    }

    protected final void lazySetHeadRef(MpscLinkedQueueNode<E> paramMpscLinkedQueueNode) {
        UPDATER.lazySet(this, paramMpscLinkedQueueNode);
    }
}




