// 
// Decompiled by Procyon v0.5.36
// 

package org.lwjgl.util.glu.tessellation;

class PriorityQSort extends PriorityQ
{
    PriorityQHeap heap;
    Object[] keys;
    int[] order;
    int size;
    int max;
    boolean initialized;
    Leq leq;
    
    PriorityQSort(final Leq leq) {
        this.heap = new PriorityQHeap(leq);
        this.keys = new Object[32];
        this.size = 0;
        this.max = 32;
        this.initialized = false;
        this.leq = leq;
    }
    
    @Override
    void pqDeletePriorityQ() {
        if (this.heap != null) {
            this.heap.pqDeletePriorityQ();
        }
        this.order = null;
        this.keys = null;
    }
    
    private static boolean LT(final Leq leq, final Object x, final Object y) {
        return !PriorityQ.LEQ(leq, y, x);
    }
    
    private static boolean GT(final Leq leq, final Object x, final Object y) {
        return !PriorityQ.LEQ(leq, x, y);
    }
    
    private static void Swap(final int[] array, final int a, final int b) {
        final int tmp = array[a];
        array[a] = array[b];
        array[b] = tmp;
    }
    
    @Override
    boolean pqInit() {
        final Stack[] stack = new Stack[50];
        for (int k = 0; k < stack.length; ++k) {
            stack[k] = new Stack();
        }
        int top = 0;
        int seed = 2016473283;
        this.order = new int[this.size + 1];
        int p = 0;
        int r = this.size - 1;
        int piv = 0;
        for (int i = p; i <= r; ++i) {
            this.order[i] = piv;
            ++piv;
        }
        stack[top].p = p;
        stack[top].r = r;
        ++top;
        while (--top >= 0) {
            p = stack[top].p;
            r = stack[top].r;
            while (r > p + 10) {
                seed = Math.abs(seed * 1539415821 + 1);
                int i = p + seed % (r - p + 1);
                piv = this.order[i];
                this.order[i] = this.order[p];
                this.order[p] = piv;
                i = p - 1;
                int j = r + 1;
                while (true) {
                    ++i;
                    if (!GT(this.leq, this.keys[this.order[i]], this.keys[piv])) {
                        do {
                            --j;
                        } while (LT(this.leq, this.keys[this.order[j]], this.keys[piv]));
                        Swap(this.order, i, j);
                        if (i >= j) {
                            break;
                        }
                        continue;
                    }
                }
                Swap(this.order, i, j);
                if (i - p < r - j) {
                    stack[top].p = j + 1;
                    stack[top].r = r;
                    ++top;
                    r = i - 1;
                }
                else {
                    stack[top].p = p;
                    stack[top].r = i - 1;
                    ++top;
                    p = j + 1;
                }
            }
            for (int i = p + 1; i <= r; ++i) {
                int j;
                for (piv = this.order[i], j = i; j > p && LT(this.leq, this.keys[this.order[j - 1]], this.keys[piv]); --j) {
                    this.order[j] = this.order[j - 1];
                }
                this.order[j] = piv;
            }
        }
        this.max = this.size;
        this.initialized = true;
        this.heap.pqInit();
        return true;
    }
    
    @Override
    int pqInsert(final Object keyNew) {
        if (this.initialized) {
            return this.heap.pqInsert(keyNew);
        }
        final int curr = this.size;
        if (++this.size >= this.max) {
            final Object[] saveKey = this.keys;
            this.max <<= 1;
            final Object[] pqKeys = new Object[this.max];
            System.arraycopy(this.keys, 0, pqKeys, 0, this.keys.length);
            this.keys = pqKeys;
            if (this.keys == null) {
                this.keys = saveKey;
                return Integer.MAX_VALUE;
            }
        }
        assert curr != Integer.MAX_VALUE;
        this.keys[curr] = keyNew;
        return -(curr + 1);
    }
    
    @Override
    Object pqExtractMin() {
        if (this.size == 0) {
            return this.heap.pqExtractMin();
        }
        final Object sortMin = this.keys[this.order[this.size - 1]];
        if (!this.heap.pqIsEmpty()) {
            final Object heapMin = this.heap.pqMinimum();
            if (PriorityQ.LEQ(this.leq, heapMin, sortMin)) {
                return this.heap.pqExtractMin();
            }
        }
        do {
            --this.size;
        } while (this.size > 0 && this.keys[this.order[this.size - 1]] == null);
        return sortMin;
    }
    
    @Override
    Object pqMinimum() {
        if (this.size == 0) {
            return this.heap.pqMinimum();
        }
        final Object sortMin = this.keys[this.order[this.size - 1]];
        if (!this.heap.pqIsEmpty()) {
            final Object heapMin = this.heap.pqMinimum();
            if (PriorityQ.LEQ(this.leq, heapMin, sortMin)) {
                return heapMin;
            }
        }
        return sortMin;
    }
    
    @Override
    boolean pqIsEmpty() {
        return this.size == 0 && this.heap.pqIsEmpty();
    }
    
    @Override
    void pqDelete(int curr) {
        if (curr >= 0) {
            this.heap.pqDelete(curr);
            return;
        }
        curr = -(curr + 1);
        assert curr < this.max && this.keys[curr] != null;
        this.keys[curr] = null;
        while (this.size > 0 && this.keys[this.order[this.size - 1]] == null) {
            --this.size;
        }
    }
    
    private static class Stack
    {
        int p;
        int r;
    }
}
