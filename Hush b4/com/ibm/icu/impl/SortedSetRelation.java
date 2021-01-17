// 
// Decompiled by Procyon v0.5.36
// 

package com.ibm.icu.impl;

import java.util.TreeSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;

public class SortedSetRelation
{
    public static final int A_NOT_B = 4;
    public static final int A_AND_B = 2;
    public static final int B_NOT_A = 1;
    public static final int ANY = 7;
    public static final int CONTAINS = 6;
    public static final int DISJOINT = 5;
    public static final int ISCONTAINED = 3;
    public static final int NO_B = 4;
    public static final int EQUALS = 2;
    public static final int NO_A = 1;
    public static final int NONE = 0;
    public static final int ADDALL = 7;
    public static final int A = 6;
    public static final int COMPLEMENTALL = 5;
    public static final int B = 3;
    public static final int REMOVEALL = 4;
    public static final int RETAINALL = 2;
    public static final int B_REMOVEALL = 1;
    
    public static <T extends java.lang.Object> boolean hasRelation(final SortedSet<T> a, final int allow, final SortedSet<T> b) {
        if (allow < 0 || allow > 7) {
            throw new IllegalArgumentException("Relation " + allow + " out of range");
        }
        final boolean anb = (allow & 0x4) != 0x0;
        final boolean ab = (allow & 0x2) != 0x0;
        final boolean bna = (allow & 0x1) != 0x0;
        switch (allow) {
            case 6: {
                if (a.size() < b.size()) {
                    return false;
                }
                break;
            }
            case 3: {
                if (a.size() > b.size()) {
                    return false;
                }
                break;
            }
            case 2: {
                if (a.size() != b.size()) {
                    return false;
                }
                break;
            }
        }
        if (a.size() == 0) {
            return b.size() == 0 || bna;
        }
        if (b.size() == 0) {
            return anb;
        }
        final Iterator<? extends T> ait = a.iterator();
        final Iterator<? extends T> bit = b.iterator();
        T aa = (T)ait.next();
        T bb = (T)bit.next();
        while (true) {
            final int comp = ((Comparable)aa).compareTo(bb);
            if (comp == 0) {
                if (!ab) {
                    return false;
                }
                if (!ait.hasNext()) {
                    return !bit.hasNext() || bna;
                }
                if (!bit.hasNext()) {
                    return anb;
                }
                aa = (T)ait.next();
                bb = (T)bit.next();
            }
            else if (comp < 0) {
                if (!anb) {
                    return false;
                }
                if (!ait.hasNext()) {
                    return bna;
                }
                aa = (T)ait.next();
            }
            else {
                if (!bna) {
                    return false;
                }
                if (!bit.hasNext()) {
                    return anb;
                }
                bb = (T)bit.next();
            }
        }
    }
    
    public static <T extends java.lang.Object> SortedSet<? extends T> doOperation(final SortedSet<T> a, final int relation, final SortedSet<T> b) {
        switch (relation) {
            case 7: {
                a.addAll((Collection<?>)b);
                return (SortedSet<? extends T>)a;
            }
            case 6: {
                return (SortedSet<? extends T>)a;
            }
            case 3: {
                a.clear();
                a.addAll((Collection<?>)b);
                return (SortedSet<? extends T>)a;
            }
            case 4: {
                a.removeAll((Collection<?>)b);
                return (SortedSet<? extends T>)a;
            }
            case 2: {
                a.retainAll((Collection<?>)b);
                return (SortedSet<? extends T>)a;
            }
            case 5: {
                final TreeSet<? extends T> temp = (TreeSet<? extends T>)new TreeSet<T>((SortedSet<? extends T>)b);
                temp.removeAll((Collection<?>)a);
                a.removeAll((Collection<?>)b);
                a.addAll((Collection<?>)temp);
                return (SortedSet<? extends T>)a;
            }
            case 1: {
                final TreeSet<? extends T> temp = (TreeSet<? extends T>)new TreeSet<T>((SortedSet<? extends T>)b);
                temp.removeAll((Collection<?>)a);
                a.clear();
                a.addAll((Collection<?>)temp);
                return (SortedSet<? extends T>)a;
            }
            case 0: {
                a.clear();
                return (SortedSet<? extends T>)a;
            }
            default: {
                throw new IllegalArgumentException("Relation " + relation + " out of range");
            }
        }
    }
}
