// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.handler.codec;

public class UnsupportedMessageTypeException extends CodecException
{
    private static final long serialVersionUID = 2799598826487038726L;
    
    public UnsupportedMessageTypeException(final Object message, final Class<?>... expectedTypes) {
        super(message((message == null) ? "null" : message.getClass().getName(), expectedTypes));
    }
    
    public UnsupportedMessageTypeException() {
    }
    
    public UnsupportedMessageTypeException(final String message, final Throwable cause) {
        super(message, cause);
    }
    
    public UnsupportedMessageTypeException(final String s) {
        super(s);
    }
    
    public UnsupportedMessageTypeException(final Throwable cause) {
        super(cause);
    }
    
    private static String message(final String actualType, final Class<?>... expectedTypes) {
        final StringBuilder buf = new StringBuilder(actualType);
        if (expectedTypes != null && expectedTypes.length > 0) {
            buf.append(" (expected: ").append(expectedTypes[0].getName());
            for (int i = 1; i < expectedTypes.length; ++i) {
                final Class<?> t = expectedTypes[i];
                if (t == null) {
                    break;
                }
                buf.append(", ").append(t.getName());
            }
            buf.append(')');
        }
        return buf.toString();
    }
}
