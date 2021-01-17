// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public enum BoundType
{
    OPEN {
        @Override
        BoundType flip() {
            return BoundType$1.CLOSED;
        }
    }, 
    CLOSED {
        @Override
        BoundType flip() {
            return BoundType$2.OPEN;
        }
    };
    
    static BoundType forBoolean(final boolean inclusive) {
        return inclusive ? BoundType.CLOSED : BoundType.OPEN;
    }
    
    abstract BoundType flip();
}
