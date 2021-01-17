// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import java.util.SortedSet;

interface SortedMultisetBridge<E> extends Multiset<E>
{
    SortedSet<E> elementSet();
}
