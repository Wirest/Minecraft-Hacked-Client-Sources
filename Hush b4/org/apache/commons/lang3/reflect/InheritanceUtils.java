// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.commons.lang3.reflect;

import org.apache.commons.lang3.BooleanUtils;

public class InheritanceUtils
{
    public static int distance(final Class<?> child, final Class<?> parent) {
        if (child == null || parent == null) {
            return -1;
        }
        if (child.equals(parent)) {
            return 0;
        }
        final Class<?> cParent = child.getSuperclass();
        int d = BooleanUtils.toInteger(parent.equals(cParent));
        if (d == 1) {
            return d;
        }
        d += distance(cParent, parent);
        return (d > 0) ? (d + 1) : -1;
    }
}
