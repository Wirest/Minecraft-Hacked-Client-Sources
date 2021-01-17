// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec.http.multipart;

import java.io.Serializable;
import java.util.Comparator;

final class CaseIgnoringComparator implements Comparator<CharSequence>, Serializable
{
    private static final long serialVersionUID = 4582133183775373862L;
    static final CaseIgnoringComparator INSTANCE;
    
    private CaseIgnoringComparator() {
    }
    
    @Override
    public int compare(final CharSequence o1, final CharSequence o2) {
        final int o1Length = o1.length();
        final int o2Length = o2.length();
        for (int min = Math.min(o1Length, o2Length), i = 0; i < min; ++i) {
            char c1 = o1.charAt(i);
            char c2 = o2.charAt(i);
            if (c1 != c2) {
                c1 = Character.toUpperCase(c1);
                c2 = Character.toUpperCase(c2);
                if (c1 != c2) {
                    c1 = Character.toLowerCase(c1);
                    c2 = Character.toLowerCase(c2);
                    if (c1 != c2) {
                        return c1 - c2;
                    }
                }
            }
        }
        return o1Length - o2Length;
    }
    
    private Object readResolve() {
        return CaseIgnoringComparator.INSTANCE;
    }
    
    static {
        INSTANCE = new CaseIgnoringComparator();
    }
}
