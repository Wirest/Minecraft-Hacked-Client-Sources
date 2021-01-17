// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.util.concurrent;

import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;

@GwtCompatible
public class ExecutionError extends Error
{
    private static final long serialVersionUID = 0L;
    
    protected ExecutionError() {
    }
    
    protected ExecutionError(@Nullable final String message) {
        super(message);
    }
    
    public ExecutionError(@Nullable final String message, @Nullable final Error cause) {
        super(message, cause);
    }
    
    public ExecutionError(@Nullable final Error cause) {
        super(cause);
    }
}
