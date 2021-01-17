// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import javax.annotation.Nullable;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.Beta;

@Beta
@GwtCompatible
public final class Verify
{
    public static void verify(final boolean expression) {
        if (!expression) {
            throw new VerifyException();
        }
    }
    
    public static void verify(final boolean expression, @Nullable final String errorMessageTemplate, @Nullable final Object... errorMessageArgs) {
        if (!expression) {
            throw new VerifyException(Preconditions.format(errorMessageTemplate, errorMessageArgs));
        }
    }
    
    public static <T> T verifyNotNull(@Nullable final T reference) {
        return verifyNotNull(reference, "expected a non-null reference", new Object[0]);
    }
    
    public static <T> T verifyNotNull(@Nullable final T reference, @Nullable final String errorMessageTemplate, @Nullable final Object... errorMessageArgs) {
        verify(reference != null, errorMessageTemplate, errorMessageArgs);
        return reference;
    }
    
    private Verify() {
    }
}
