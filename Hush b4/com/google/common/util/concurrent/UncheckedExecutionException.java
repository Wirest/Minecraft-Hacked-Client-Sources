// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public class UncheckedExecutionException extends RuntimeException
{
    private static final long serialVersionUID = 0L;
    
    protected UncheckedExecutionException() {
    }
    
    protected UncheckedExecutionException(@Nullable final String message) {
        super(message);
    }
    
    public UncheckedExecutionException(@Nullable final String message, @Nullable final Throwable cause) {
        super(message, cause);
    }
    
    public UncheckedExecutionException(@Nullable final Throwable cause) {
        super(cause);
    }
}
