// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.message;

public abstract class AbstractMessageFactory implements MessageFactory
{
    @Override
    public Message newMessage(final Object message) {
        return new ObjectMessage(message);
    }
    
    @Override
    public Message newMessage(final String message) {
        return new SimpleMessage(message);
    }
    
    @Override
    public abstract Message newMessage(final String p0, final Object... p1);
}
