// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal.logging;

class FormattingTuple
{
    static final FormattingTuple NULL;
    private final String message;
    private final Throwable throwable;
    private final Object[] argArray;
    
    FormattingTuple(final String message) {
        this(message, null, null);
    }
    
    FormattingTuple(final String message, final Object[] argArray, final Throwable throwable) {
        this.message = message;
        this.throwable = throwable;
        if (throwable == null) {
            this.argArray = argArray;
        }
        else {
            this.argArray = trimmedCopy(argArray);
        }
    }
    
    static Object[] trimmedCopy(final Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            throw new IllegalStateException("non-sensical empty or null argument array");
        }
        final int trimemdLen = argArray.length - 1;
        final Object[] trimmed = new Object[trimemdLen];
        System.arraycopy(argArray, 0, trimmed, 0, trimemdLen);
        return trimmed;
    }
    
    public String getMessage() {
        return this.message;
    }
    
    public Object[] getArgArray() {
        return this.argArray;
    }
    
    public Throwable getThrowable() {
        return this.throwable;
    }
    
    static {
        NULL = new FormattingTuple(null);
    }
}
