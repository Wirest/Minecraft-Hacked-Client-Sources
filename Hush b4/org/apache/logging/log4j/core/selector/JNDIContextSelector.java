// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.selector;

import java.util.concurrent.ConcurrentHashMap;
import javax.naming.NameNotFoundException;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import org.apache.logging.log4j.core.impl.ContextAnchor;
import java.net.URI;
import org.apache.logging.log4j.status.StatusLogger;
import java.util.concurrent.ConcurrentMap;
import org.apache.logging.log4j.core.LoggerContext;

public class JNDIContextSelector implements NamedContextSelector
{
    private static final LoggerContext CONTEXT;
    private static final ConcurrentMap<String, LoggerContext> CONTEXT_MAP;
    private static final StatusLogger LOGGER;
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext) {
        return this.getContext(fqcn, loader, currentContext, null);
    }
    
    @Override
    public LoggerContext getContext(final String fqcn, final ClassLoader loader, final boolean currentContext, final URI configLocation) {
        final LoggerContext lc = ContextAnchor.THREAD_CONTEXT.get();
        if (lc != null) {
            return lc;
        }
        String loggingContextName = null;
        try {
            final Context ctx = new InitialContext();
            loggingContextName = (String)lookup(ctx, "java:comp/env/log4j/context-name");
        }
        catch (NamingException ne) {
            JNDIContextSelector.LOGGER.error("Unable to lookup java:comp/env/log4j/context-name", ne);
        }
        return (loggingContextName == null) ? JNDIContextSelector.CONTEXT : this.locateContext(loggingContextName, null, configLocation);
    }
    
    @Override
    public LoggerContext locateContext(final String name, final Object externalContext, final URI configLocation) {
        if (name == null) {
            JNDIContextSelector.LOGGER.error("A context name is required to locate a LoggerContext");
            return null;
        }
        if (!JNDIContextSelector.CONTEXT_MAP.containsKey(name)) {
            final LoggerContext ctx = new LoggerContext(name, externalContext, configLocation);
            JNDIContextSelector.CONTEXT_MAP.putIfAbsent(name, ctx);
        }
        return JNDIContextSelector.CONTEXT_MAP.get(name);
    }
    
    @Override
    public void removeContext(final LoggerContext context) {
        for (final Map.Entry<String, LoggerContext> entry : JNDIContextSelector.CONTEXT_MAP.entrySet()) {
            if (entry.getValue().equals(context)) {
                JNDIContextSelector.CONTEXT_MAP.remove(entry.getKey());
            }
        }
    }
    
    @Override
    public LoggerContext removeContext(final String name) {
        return JNDIContextSelector.CONTEXT_MAP.remove(name);
    }
    
    @Override
    public List<LoggerContext> getLoggerContexts() {
        final List<LoggerContext> list = new ArrayList<LoggerContext>(JNDIContextSelector.CONTEXT_MAP.values());
        return Collections.unmodifiableList((List<? extends LoggerContext>)list);
    }
    
    protected static Object lookup(final Context ctx, final String name) throws NamingException {
        if (ctx == null) {
            return null;
        }
        try {
            return ctx.lookup(name);
        }
        catch (NameNotFoundException e) {
            JNDIContextSelector.LOGGER.error("Could not find name [" + name + "].");
            throw e;
        }
    }
    
    static {
        CONTEXT = new LoggerContext("Default");
        CONTEXT_MAP = new ConcurrentHashMap<String, LoggerContext>();
        LOGGER = StatusLogger.getLogger();
    }
}
