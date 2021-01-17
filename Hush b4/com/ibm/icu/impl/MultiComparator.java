// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.Comparator;

public class MultiComparator<T> implements Comparator<T>
{
    private Comparator<T>[] comparators;
    
    public MultiComparator(final Comparator<T>... comparators) {
        this.comparators = comparators;
    }
    
    public int compare(final T arg0, final T arg1) {
        int i = 0;
        while (i < this.comparators.length) {
            final int result = this.comparators[i].compare(arg0, arg1);
            if (result == 0) {
                ++i;
            }
            else {
                if (result > 0) {
                    return i + 1;
                }
                return -(i + 1);
            }
        }
        return 0;
    }
}
