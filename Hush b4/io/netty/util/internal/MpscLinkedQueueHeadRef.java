// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.io.Serializable;

abstract class MpscLinkedQueueHeadRef<E> extends MpscLinkedQueuePad0<E> implements Serializable
{
    private static final long serialVersionUID = 8467054865577874285L;
    private static final AtomicReferenceFieldUpdater<MpscLinkedQueueHeadRef, MpscLinkedQueueNode> UPDATER;
    private transient volatile MpscLinkedQueueNode<E> headRef;
    
    protected final MpscLinkedQueueNode<E> headRef() {
        return this.headRef;
    }
    
    protected final void setHeadRef(final MpscLinkedQueueNode<E> headRef) {
        this.headRef = headRef;
    }
    
    protected final void lazySetHeadRef(final MpscLinkedQueueNode<E> headRef) {
        MpscLinkedQueueHeadRef.UPDATER.lazySet(this, headRef);
    }
    
    static {
        AtomicReferenceFieldUpdater<MpscLinkedQueueHeadRef, MpscLinkedQueueNode> updater = (AtomicReferenceFieldUpdater<MpscLinkedQueueHeadRef, MpscLinkedQueueNode>)PlatformDependent.newAtomicReferenceFieldUpdater(MpscLinkedQueueHeadRef.class, "headRef");
        if (updater == null) {
            updater = (AtomicReferenceFieldUpdater<MpscLinkedQueueHeadRef, MpscLinkedQueueNode>)AtomicReferenceFieldUpdater.newUpdater(MpscLinkedQueueHeadRef.class, MpscLinkedQueueNode.class, "headRef");
        }
        UPDATER = updater;
    }
}
