// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

public class PVecToTrieCompactHandler implements PropsVectors.CompactHandler
{
    public IntTrieBuilder builder;
    public int initialValue;
    
    public void setRowIndexForErrorValue(final int rowIndex) {
    }
    
    public void setRowIndexForInitialValue(final int rowIndex) {
        this.initialValue = rowIndex;
    }
    
    public void setRowIndexForRange(final int start, final int end, final int rowIndex) {
        this.builder.setRange(start, end + 1, rowIndex, true);
    }
    
    public void startRealValues(final int rowIndex) {
        if (rowIndex > 65535) {
            throw new IndexOutOfBoundsException();
        }
        this.builder = new IntTrieBuilder(null, 100000, this.initialValue, this.initialValue, false);
    }
}
