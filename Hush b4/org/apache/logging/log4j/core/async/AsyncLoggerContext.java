// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.async;

import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.MessageFactory;
import java.net.URI;
import org.apache.logging.log4j.core.LoggerContext;

public class AsyncLoggerContext extends LoggerContext
{
    public AsyncLoggerContext(final String name) {
        super(name);
    }
    
    public AsyncLoggerContext(final String name, final Object externalContext) {
        super(name, externalContext);
    }
    
    public AsyncLoggerContext(final String name, final Object externalContext, final URI configLocn) {
        super(name, externalContext, configLocn);
    }
    
    public AsyncLoggerContext(final String name, final Object externalContext, final String configLocn) {
        super(name, externalContext, configLocn);
    }
    
    @Override
    protected Logger newInstance(final LoggerContext ctx, final String name, final MessageFactory messageFactory) {
        return new AsyncLogger(ctx, name, messageFactory);
    }
    
    @Override
    public void stop() {
        AsyncLogger.stop();
        super.stop();
    }
}
