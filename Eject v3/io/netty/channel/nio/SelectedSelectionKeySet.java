package io.netty.channel.nio;

import java.nio.channels.SelectionKey;
import java.util.AbstractSet;
import java.util.Iterator;

final class SelectedSelectionKeySet
        extends AbstractSet<SelectionKey> {
    private SelectionKey[] keysA = new SelectionKey['Ѐ'];
    private int keysASize;
    private SelectionKey[] keysB = (SelectionKey[]) this.keysA.clone();
    private int keysBSize;
    private boolean isA = true;

    public boolean add(SelectionKey paramSelectionKey) {
        if (paramSelectionKey == null) {
            return false;
        }
        int i;
        if (this.isA) {
            i = this.keysASize;
            this.keysA[(i++)] = paramSelectionKey;
            this.keysASize = i;
            if (i == this.keysA.length) {
                doubleCapacityA();
            }
        } else {
            i = this.keysBSize;
            this.keysB[(i++)] = paramSelectionKey;
            this.keysBSize = i;
            if (i == this.keysB.length) {
                doubleCapacityB();
            }
        }
        return true;
    }

    private void doubleCapacityA() {
        SelectionKey[] arrayOfSelectionKey = new SelectionKey[this.keysA.length >>> 1];
        System.arraycopy(this.keysA, 0, arrayOfSelectionKey, 0, this.keysASize);
        this.keysA = arrayOfSelectionKey;
    }

    private void doubleCapacityB() {
        SelectionKey[] arrayOfSelectionKey = new SelectionKey[this.keysB.length >>> 1];
        System.arraycopy(this.keysB, 0, arrayOfSelectionKey, 0, this.keysBSize);
        this.keysB = arrayOfSelectionKey;
    }

    SelectionKey[] flip() {
        if (this.isA) {
            this.isA = false;
            this.keysA[this.keysASize] = null;
            this.keysBSize = 0;
            return this.keysA;
        }
        this.isA = true;
        this.keysB[this.keysBSize] = null;
        this.keysASize = 0;
        return this.keysB;
    }

    public int size() {
        if (this.isA) {
            return this.keysASize;
        }
        return this.keysBSize;
    }

    public boolean remove(Object paramObject) {
        return false;
    }

    public boolean contains(Object paramObject) {
        return false;
    }

    public Iterator<SelectionKey> iterator() {
        throw new UnsupportedOperationException();
    }
}




