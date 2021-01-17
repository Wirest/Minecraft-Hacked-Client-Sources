// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

public final class ParameterizedMessageFactory extends AbstractMessageFactory
{
    public static final ParameterizedMessageFactory INSTANCE;
    
    @Override
    public Message newMessage(final String message, final Object... params) {
        return new ParameterizedMessage(message, params);
    }
    
    static {
        INSTANCE = new ParameterizedMessageFactory();
    }
}
