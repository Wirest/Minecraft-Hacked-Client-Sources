// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.escape;

import com.google.common.annotations.VisibleForTesting;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import com.google.common.base.Preconditions;
import java.util.Map;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public final class ArrayBasedEscaperMap
{
    private final char[][] replacementArray;
    private static final char[][] EMPTY_REPLACEMENT_ARRAY;
    
    public static ArrayBasedEscaperMap create(final Map<Character, String> replacements) {
        return new ArrayBasedEscaperMap(createReplacementArray(replacements));
    }
    
    private ArrayBasedEscaperMap(final char[][] replacementArray) {
        this.replacementArray = replacementArray;
    }
    
    char[][] getReplacementArray() {
        return this.replacementArray;
    }
    
    @VisibleForTesting
    static char[][] createReplacementArray(final Map<Character, String> map) {
        Preconditions.checkNotNull(map);
        if (map.isEmpty()) {
            return ArrayBasedEscaperMap.EMPTY_REPLACEMENT_ARRAY;
        }
        final char max = Collections.max((Collection<? extends Character>)map.keySet());
        final char[][] replacements = new char[max + '\u0001'][];
        for (final char c : map.keySet()) {
            replacements[c] = map.get(c).toCharArray();
        }
        return replacements;
    }
    
    static {
        EMPTY_REPLACEMENT_ARRAY = new char[0][0];
    }
}
