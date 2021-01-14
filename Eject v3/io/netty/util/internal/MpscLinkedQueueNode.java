package io.netty.util.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public abstract class MpscLinkedQueueNode<T> {
    private static final AtomicReferenceFieldUpdater<MpscLinkedQueueNode, MpscLinkedQueueNode> nextUpdater;

    static {
        AtomicReferenceFieldUpdater localAtomicReferenceFieldUpdater = PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueNode.class, "next");
        if (localAtomicReferenceFieldUpdater == null) {
            localAtomicReferenceFieldUpdater = AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueNode.class, MpscLinkedQueueNode.class, "next");
        }
        nextUpdater = localAtomicReferenceFieldUpdater;
    }

    private volatile MpscLinkedQueueNode<T> next;

    final MpscLinkedQueueNode<T> next() {
        return this.next;
    }

    final void setNext(MpscLinkedQueueNode<T> paramMpscLinkedQueueNode) {
        nextUpdater.lazySet(this, paramMpscLinkedQueueNode);
    }

    public abstract T value();

    protected T clearMaybe() {
        return (T) value();
    }

    void unlink() {
        setNext(null);
    }
}




