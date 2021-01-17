// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu.tessellation;

class PriorityQHeap extends PriorityQ
{
    PQnode[] nodes;
    PQhandleElem[] handles;
    int size;
    int max;
    int freeList;
    boolean initialized;
    Leq leq;
    
    PriorityQHeap(final Leq leq) {
        this.size = 0;
        this.max = 32;
        this.nodes = new PQnode[33];
        for (int i = 0; i < this.nodes.length; ++i) {
            this.nodes[i] = new PQnode();
        }
        this.handles = new PQhandleElem[33];
        for (int i = 0; i < this.handles.length; ++i) {
            this.handles[i] = new PQhandleElem();
        }
        this.initialized = false;
        this.freeList = 0;
        this.leq = leq;
        this.nodes[1].handle = 1;
        this.handles[1].key = null;
    }
    
    @Override
    void pqDeletePriorityQ() {
        this.handles = null;
        this.nodes = null;
    }
    
    void FloatDown(int curr) {
        final PQnode[] n = this.nodes;
        final PQhandleElem[] h = this.handles;
        final int hCurr = n[curr].handle;
        while (true) {
            int child = curr << 1;
            if (child < this.size && PriorityQ.LEQ(this.leq, h[n[child + 1].handle].key, h[n[child].handle].key)) {
                ++child;
            }
            assert child <= this.max;
            final int hChild = n[child].handle;
            if (child > this.size || PriorityQ.LEQ(this.leq, h[hCurr].key, h[hChild].key)) {
                n[curr].handle = hCurr;
                h[hCurr].node = curr;
                return;
            }
            n[curr].handle = hChild;
            h[hChild].node = curr;
            curr = child;
        }
    }
    
    void FloatUp(int curr) {
        final PQnode[] n = this.nodes;
        final PQhandleElem[] h = this.handles;
        final int hCurr = n[curr].handle;
        while (true) {
            final int parent = curr >> 1;
            final int hParent = n[parent].handle;
            if (parent == 0 || PriorityQ.LEQ(this.leq, h[hParent].key, h[hCurr].key)) {
                break;
            }
            n[curr].handle = hParent;
            h[hParent].node = curr;
            curr = parent;
        }
        n[curr].handle = hCurr;
        h[hCurr].node = curr;
    }
    
    @Override
    boolean pqInit() {
        for (int i = this.size; i >= 1; --i) {
            this.FloatDown(i);
        }
        return this.initialized = true;
    }
    
    @Override
    int pqInsert(final Object keyNew) {
        final int curr = ++this.size;
        if (curr * 2 > this.max) {
            final PQnode[] saveNodes = this.nodes;
            final PQhandleElem[] saveHandles = this.handles;
            this.max <<= 1;
            final PQnode[] pqNodes = new PQnode[this.max + 1];
            System.arraycopy(this.nodes, 0, pqNodes, 0, this.nodes.length);
            for (int i = this.nodes.length; i < pqNodes.length; ++i) {
                pqNodes[i] = new PQnode();
            }
            this.nodes = pqNodes;
            if (this.nodes == null) {
                this.nodes = saveNodes;
                return Integer.MAX_VALUE;
            }
            final PQhandleElem[] pqHandles = new PQhandleElem[this.max + 1];
            System.arraycopy(this.handles, 0, pqHandles, 0, this.handles.length);
            for (int j = this.handles.length; j < pqHandles.length; ++j) {
                pqHandles[j] = new PQhandleElem();
            }
            this.handles = pqHandles;
            if (this.handles == null) {
                this.handles = saveHandles;
                return Integer.MAX_VALUE;
            }
        }
        int free;
        if (this.freeList == 0) {
            free = curr;
        }
        else {
            free = this.freeList;
            this.freeList = this.handles[free].node;
        }
        this.nodes[curr].handle = free;
        this.handles[free].node = curr;
        this.handles[free].key = keyNew;
        if (this.initialized) {
            this.FloatUp(curr);
        }
        assert free != Integer.MAX_VALUE;
        return free;
    }
    
    @Override
    Object pqExtractMin() {
        final PQnode[] n = this.nodes;
        final PQhandleElem[] h = this.handles;
        final int hMin = n[1].handle;
        final Object min = h[hMin].key;
        if (this.size > 0) {
            n[1].handle = n[this.size].handle;
            h[n[1].handle].node = 1;
            h[hMin].key = null;
            h[hMin].node = this.freeList;
            this.freeList = hMin;
            if (--this.size > 0) {
                this.FloatDown(1);
            }
        }
        return min;
    }
    
    @Override
    void pqDelete(final int hCurr) {
        final PQnode[] n = this.nodes;
        final PQhandleElem[] h = this.handles;
        assert hCurr >= 1 && hCurr <= this.max && h[hCurr].key != null;
        final int curr = h[hCurr].node;
        n[curr].handle = n[this.size].handle;
        if ((h[n[curr].handle].node = curr) <= --this.size) {
            if (curr <= 1 || PriorityQ.LEQ(this.leq, h[n[curr >> 1].handle].key, h[n[curr].handle].key)) {
                this.FloatDown(curr);
            }
            else {
                this.FloatUp(curr);
            }
        }
        h[hCurr].key = null;
        h[hCurr].node = this.freeList;
        this.freeList = hCurr;
    }
    
    @Override
    Object pqMinimum() {
        return this.handles[this.nodes[1].handle].key;
    }
    
    @Override
    boolean pqIsEmpty() {
        return this.size == 0;
    }
}
