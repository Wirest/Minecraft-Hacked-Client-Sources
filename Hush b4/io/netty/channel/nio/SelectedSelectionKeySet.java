// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.channel.nio;

import java.util.Iterator;
import java.nio.channels.SelectionKey;
import java.util.AbstractSet;

final class SelectedSelectionKeySet extends AbstractSet<SelectionKey>
{
    private SelectionKey[] keysA;
    private int keysASize;
    private SelectionKey[] keysB;
    private int keysBSize;
    private boolean isA;
    
    SelectedSelectionKeySet() {
        this.isA = true;
        this.keysA = new SelectionKey[1024];
        this.keysB = this.keysA.clone();
    }
    
    @Override
    public boolean add(final SelectionKey o) {
        if (o == null) {
            return false;
        }
        if (this.isA) {
            int size = this.keysASize;
            this.keysA[size++] = o;
            if ((this.keysASize = size) == this.keysA.length) {
                this.doubleCapacityA();
            }
        }
        else {
            int size = this.keysBSize;
            this.keysB[size++] = o;
            if ((this.keysBSize = size) == this.keysB.length) {
                this.doubleCapacityB();
            }
        }
        return true;
    }
    
    private void doubleCapacityA() {
        final SelectionKey[] newKeysA = new SelectionKey[this.keysA.length << 1];
        System.arraycopy(this.keysA, 0, newKeysA, 0, this.keysASize);
        this.keysA = newKeysA;
    }
    
    private void doubleCapacityB() {
        final SelectionKey[] newKeysB = new SelectionKey[this.keysB.length << 1];
        System.arraycopy(this.keysB, 0, newKeysB, 0, this.keysBSize);
        this.keysB = newKeysB;
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
    
    @Override
    public int size() {
        if (this.isA) {
            return this.keysASize;
        }
        return this.keysBSize;
    }
    
    @Override
    public boolean remove(final Object o) {
        return false;
    }
    
    @Override
    public boolean contains(final Object o) {
        return false;
    }
    
    @Override
    public Iterator<SelectionKey> iterator() {
        throw new UnsupportedOperationException();
    }
}
