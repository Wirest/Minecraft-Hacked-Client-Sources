// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import org.apache.logging.log4j.message.ParameterizedMessageFactory;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.status.StatusLogger;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Logger;

public abstract class AbstractLogger implements Logger
{
    public static final Marker FLOW_MARKER;
    public static final Marker ENTRY_MARKER;
    public static final Marker EXIT_MARKER;
    public static final Marker EXCEPTION_MARKER;
    public static final Marker THROWING_MARKER;
    public static final Marker CATCHING_MARKER;
    public static final Class<? extends MessageFactory> DEFAULT_MESSAGE_FACTORY_CLASS;
    private static final String FQCN;
    private static final String THROWING = "throwing";
    private static final String CATCHING = "catching";
    private final String name;
    private final MessageFactory messageFactory;
    
    public AbstractLogger() {
        this.name = this.getClass().getName();
        this.messageFactory = this.createDefaultMessageFactory();
    }
    
    public AbstractLogger(final String name) {
        this.name = name;
        this.messageFactory = this.createDefaultMessageFactory();
    }
    
    public AbstractLogger(final String name, final MessageFactory messageFactory) {
        this.name = name;
        this.messageFactory = ((messageFactory == null) ? this.createDefaultMessageFactory() : messageFactory);
    }
    
    public static void checkMessageFactory(final Logger logger, final MessageFactory messageFactory) {
        final String name = logger.getName();
        final MessageFactory loggerMessageFactory = logger.getMessageFactory();
        if (messageFactory != null && !loggerMessageFactory.equals(messageFactory)) {
            StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with the message factory {}, which may create log events with unexpected formatting.", name, loggerMessageFactory, messageFactory);
        }
        else if (messageFactory == null && !loggerMessageFactory.getClass().equals(AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS)) {
            StatusLogger.getLogger().warn("The Logger {} was created with the message factory {} and is now requested with a null message factory (defaults to {}), which may create log events with unexpected formatting.", name, loggerMessageFactory, AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS.getName());
        }
    }
    
    @Override
    public void catching(final Level level, final Throwable t) {
        this.catching(AbstractLogger.FQCN, level, t);
    }
    
    @Override
    public void catching(final Throwable t) {
        this.catching(AbstractLogger.FQCN, Level.ERROR, t);
    }
    
    protected void catching(final String fqcn, final Level level, final Throwable t) {
        if (this.isEnabled(level, AbstractLogger.CATCHING_MARKER, (Object)null, null)) {
            this.log(AbstractLogger.CATCHING_MARKER, fqcn, level, this.messageFactory.newMessage("catching"), t);
        }
    }
    
    private MessageFactory createDefaultMessageFactory() {
        try {
            return (MessageFactory)AbstractLogger.DEFAULT_MESSAGE_FACTORY_CLASS.newInstance();
        }
        catch (InstantiationException e) {
            throw new IllegalStateException(e);
        }
        catch (IllegalAccessException e2) {
            throw new IllegalStateException(e2);
        }
    }
    
    @Override
    public void debug(final Marker marker, final Message msg) {
        if (this.isEnabled(Level.DEBUG, marker, msg, null)) {
            this.log(marker, AbstractLogger.FQCN, Level.DEBUG, msg, null);
        }
    }
    
    @Override
    public void debug(final Marker marker, final Message msg, final Throwable t) {
        if (this.isEnabled(Level.DEBUG, marker, msg, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.DEBUG, msg, t);
        }
    }
    
    @Override
    public void debug(final Marker marker, final Object message) {
        if (this.isEnabled(Level.DEBUG, marker, message, null)) {
            this.log(marker, AbstractLogger.FQCN, Level.DEBUG, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void debug(final Marker marker, final Object message, final Throwable t) {
        if (this.isEnabled(Level.DEBUG, marker, message, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.DEBUG, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void debug(final Marker marker, final String message) {
        if (this.isEnabled(Level.DEBUG, marker, message)) {
            this.log(marker, AbstractLogger.FQCN, Level.DEBUG, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void debug(final Marker marker, final String message, final Object... params) {
        if (this.isEnabled(Level.DEBUG, marker, message, params)) {
            final Message msg = this.messageFactory.newMessage(message, params);
            this.log(marker, AbstractLogger.FQCN, Level.DEBUG, msg, msg.getThrowable());
        }
    }
    
    @Override
    public void debug(final Marker marker, final String message, final Throwable t) {
        if (this.isEnabled(Level.DEBUG, marker, message, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.DEBUG, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void debug(final Message msg) {
        if (this.isEnabled(Level.DEBUG, null, msg, null)) {
            this.log(null, AbstractLogger.FQCN, Level.DEBUG, msg, null);
        }
    }
    
    @Override
    public void debug(final Message msg, final Throwable t) {
        if (this.isEnabled(Level.DEBUG, null, msg, t)) {
            this.log(null, AbstractLogger.FQCN, Level.DEBUG, msg, t);
        }
    }
    
    @Override
    public void debug(final Object message) {
        if (this.isEnabled(Level.DEBUG, null, message, null)) {
            this.log(null, AbstractLogger.FQCN, Level.DEBUG, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void debug(final Object message, final Throwable t) {
        if (this.isEnabled(Level.DEBUG, null, message, t)) {
            this.log(null, AbstractLogger.FQCN, Level.DEBUG, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void debug(final String message) {
        if (this.isEnabled(Level.DEBUG, null, message)) {
            this.log(null, AbstractLogger.FQCN, Level.DEBUG, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void debug(final String message, final Object... params) {
        if (this.isEnabled(Level.DEBUG, null, message, params)) {
            final Message msg = this.messageFactory.newMessage(message, params);
            this.log(null, AbstractLogger.FQCN, Level.DEBUG, msg, msg.getThrowable());
        }
    }
    
    @Override
    public void debug(final String message, final Throwable t) {
        if (this.isEnabled(Level.DEBUG, null, message, t)) {
            this.log(null, AbstractLogger.FQCN, Level.DEBUG, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void entry() {
        this.entry(AbstractLogger.FQCN, new Object[0]);
    }
    
    @Override
    public void entry(final Object... params) {
        this.entry(AbstractLogger.FQCN, params);
    }
    
    protected void entry(final String fqcn, final Object... params) {
        if (this.isEnabled(Level.TRACE, AbstractLogger.ENTRY_MARKER, (Object)null, null)) {
            this.log(AbstractLogger.ENTRY_MARKER, fqcn, Level.TRACE, this.entryMsg(params.length, params), null);
        }
    }
    
    private Message entryMsg(final int count, final Object... params) {
        if (count == 0) {
            return this.messageFactory.newMessage("entry");
        }
        final StringBuilder sb = new StringBuilder("entry params(");
        int i = 0;
        for (final Object parm : params) {
            if (parm != null) {
                sb.append(parm.toString());
            }
            else {
                sb.append("null");
            }
            if (++i < params.length) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return this.messageFactory.newMessage(sb.toString());
    }
    
    @Override
    public void error(final Marker marker, final Message msg) {
        if (this.isEnabled(Level.ERROR, marker, msg, null)) {
            this.log(marker, AbstractLogger.FQCN, Level.ERROR, msg, null);
        }
    }
    
    @Override
    public void error(final Marker marker, final Message msg, final Throwable t) {
        if (this.isEnabled(Level.ERROR, marker, msg, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.ERROR, msg, t);
        }
    }
    
    @Override
    public void error(final Marker marker, final Object message) {
        if (this.isEnabled(Level.ERROR, marker, message, null)) {
            this.log(marker, AbstractLogger.FQCN, Level.ERROR, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void error(final Marker marker, final Object message, final Throwable t) {
        if (this.isEnabled(Level.ERROR, marker, message, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.ERROR, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void error(final Marker marker, final String message) {
        if (this.isEnabled(Level.ERROR, marker, message)) {
            this.log(marker, AbstractLogger.FQCN, Level.ERROR, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void error(final Marker marker, final String message, final Object... params) {
        if (this.isEnabled(Level.ERROR, marker, message, params)) {
            final Message msg = this.messageFactory.newMessage(message, params);
            this.log(marker, AbstractLogger.FQCN, Level.ERROR, msg, msg.getThrowable());
        }
    }
    
    @Override
    public void error(final Marker marker, final String message, final Throwable t) {
        if (this.isEnabled(Level.ERROR, marker, message, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.ERROR, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void error(final Message msg) {
        if (this.isEnabled(Level.ERROR, null, msg, null)) {
            this.log(null, AbstractLogger.FQCN, Level.ERROR, msg, null);
        }
    }
    
    @Override
    public void error(final Message msg, final Throwable t) {
        if (this.isEnabled(Level.ERROR, null, msg, t)) {
            this.log(null, AbstractLogger.FQCN, Level.ERROR, msg, t);
        }
    }
    
    @Override
    public void error(final Object message) {
        if (this.isEnabled(Level.ERROR, null, message, null)) {
            this.log(null, AbstractLogger.FQCN, Level.ERROR, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void error(final Object message, final Throwable t) {
        if (this.isEnabled(Level.ERROR, null, message, t)) {
            this.log(null, AbstractLogger.FQCN, Level.ERROR, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void error(final String message) {
        if (this.isEnabled(Level.ERROR, null, message)) {
            this.log(null, AbstractLogger.FQCN, Level.ERROR, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void error(final String message, final Object... params) {
        if (this.isEnabled(Level.ERROR, null, message, params)) {
            final Message msg = this.messageFactory.newMessage(message, params);
            this.log(null, AbstractLogger.FQCN, Level.ERROR, msg, msg.getThrowable());
        }
    }
    
    @Override
    public void error(final String message, final Throwable t) {
        if (this.isEnabled(Level.ERROR, null, message, t)) {
            this.log(null, AbstractLogger.FQCN, Level.ERROR, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void exit() {
        this.exit(AbstractLogger.FQCN, (Object)null);
    }
    
    @Override
    public <R> R exit(final R result) {
        return this.exit(AbstractLogger.FQCN, result);
    }
    
    protected <R> R exit(final String fqcn, final R result) {
        if (this.isEnabled(Level.TRACE, AbstractLogger.EXIT_MARKER, (Object)null, null)) {
            this.log(AbstractLogger.EXIT_MARKER, fqcn, Level.TRACE, this.toExitMsg(result), null);
        }
        return result;
    }
    
    @Override
    public void fatal(final Marker marker, final Message msg) {
        if (this.isEnabled(Level.FATAL, marker, msg, null)) {
            this.log(marker, AbstractLogger.FQCN, Level.FATAL, msg, null);
        }
    }
    
    @Override
    public void fatal(final Marker marker, final Message msg, final Throwable t) {
        if (this.isEnabled(Level.FATAL, marker, msg, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.FATAL, msg, t);
        }
    }
    
    @Override
    public void fatal(final Marker marker, final Object message) {
        if (this.isEnabled(Level.FATAL, marker, message, null)) {
            this.log(marker, AbstractLogger.FQCN, Level.FATAL, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void fatal(final Marker marker, final Object message, final Throwable t) {
        if (this.isEnabled(Level.FATAL, marker, message, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.FATAL, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void fatal(final Marker marker, final String message) {
        if (this.isEnabled(Level.FATAL, marker, message)) {
            this.log(marker, AbstractLogger.FQCN, Level.FATAL, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void fatal(final Marker marker, final String message, final Object... params) {
        if (this.isEnabled(Level.FATAL, marker, message, params)) {
            final Message msg = this.messageFactory.newMessage(message, params);
            this.log(marker, AbstractLogger.FQCN, Level.FATAL, msg, msg.getThrowable());
        }
    }
    
    @Override
    public void fatal(final Marker marker, final String message, final Throwable t) {
        if (this.isEnabled(Level.FATAL, marker, message, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.FATAL, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void fatal(final Message msg) {
        if (this.isEnabled(Level.FATAL, null, msg, null)) {
            this.log(null, AbstractLogger.FQCN, Level.FATAL, msg, null);
        }
    }
    
    @Override
    public void fatal(final Message msg, final Throwable t) {
        if (this.isEnabled(Level.FATAL, null, msg, t)) {
            this.log(null, AbstractLogger.FQCN, Level.FATAL, msg, t);
        }
    }
    
    @Override
    public void fatal(final Object message) {
        if (this.isEnabled(Level.FATAL, null, message, null)) {
            this.log(null, AbstractLogger.FQCN, Level.FATAL, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void fatal(final Object message, final Throwable t) {
        if (this.isEnabled(Level.FATAL, null, message, t)) {
            this.log(null, AbstractLogger.FQCN, Level.FATAL, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void fatal(final String message) {
        if (this.isEnabled(Level.FATAL, null, message)) {
            this.log(null, AbstractLogger.FQCN, Level.FATAL, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void fatal(final String message, final Object... params) {
        if (this.isEnabled(Level.FATAL, null, message, params)) {
            final Message msg = this.messageFactory.newMessage(message, params);
            this.log(null, AbstractLogger.FQCN, Level.FATAL, msg, msg.getThrowable());
        }
    }
    
    @Override
    public void fatal(final String message, final Throwable t) {
        if (this.isEnabled(Level.FATAL, null, message, t)) {
            this.log(null, AbstractLogger.FQCN, Level.FATAL, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public MessageFactory getMessageFactory() {
        return this.messageFactory;
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public void info(final Marker marker, final Message msg) {
        if (this.isEnabled(Level.INFO, marker, msg, null)) {
            this.log(marker, AbstractLogger.FQCN, Level.INFO, msg, null);
        }
    }
    
    @Override
    public void info(final Marker marker, final Message msg, final Throwable t) {
        if (this.isEnabled(Level.INFO, marker, msg, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.INFO, msg, t);
        }
    }
    
    @Override
    public void info(final Marker marker, final Object message) {
        if (this.isEnabled(Level.INFO, marker, message, null)) {
            this.log(marker, AbstractLogger.FQCN, Level.INFO, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void info(final Marker marker, final Object message, final Throwable t) {
        if (this.isEnabled(Level.INFO, marker, message, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.INFO, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void info(final Marker marker, final String message) {
        if (this.isEnabled(Level.INFO, marker, message)) {
            this.log(marker, AbstractLogger.FQCN, Level.INFO, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void info(final Marker marker, final String message, final Object... params) {
        if (this.isEnabled(Level.INFO, marker, message, params)) {
            final Message msg = this.messageFactory.newMessage(message, params);
            this.log(marker, AbstractLogger.FQCN, Level.INFO, msg, msg.getThrowable());
        }
    }
    
    @Override
    public void info(final Marker marker, final String message, final Throwable t) {
        if (this.isEnabled(Level.INFO, marker, message, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.INFO, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void info(final Message msg) {
        if (this.isEnabled(Level.INFO, null, msg, null)) {
            this.log(null, AbstractLogger.FQCN, Level.INFO, msg, null);
        }
    }
    
    @Override
    public void info(final Message msg, final Throwable t) {
        if (this.isEnabled(Level.INFO, null, msg, t)) {
            this.log(null, AbstractLogger.FQCN, Level.INFO, msg, t);
        }
    }
    
    @Override
    public void info(final Object message) {
        if (this.isEnabled(Level.INFO, null, message, null)) {
            this.log(null, AbstractLogger.FQCN, Level.INFO, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void info(final Object message, final Throwable t) {
        if (this.isEnabled(Level.INFO, null, message, t)) {
            this.log(null, AbstractLogger.FQCN, Level.INFO, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void info(final String message) {
        if (this.isEnabled(Level.INFO, null, message)) {
            this.log(null, AbstractLogger.FQCN, Level.INFO, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void info(final String message, final Object... params) {
        if (this.isEnabled(Level.INFO, null, message, params)) {
            final Message msg = this.messageFactory.newMessage(message, params);
            this.log(null, AbstractLogger.FQCN, Level.INFO, msg, msg.getThrowable());
        }
    }
    
    @Override
    public void info(final String message, final Throwable t) {
        if (this.isEnabled(Level.INFO, null, message, t)) {
            this.log(null, AbstractLogger.FQCN, Level.INFO, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this.isEnabled(Level.DEBUG, null, null);
    }
    
    @Override
    public boolean isDebugEnabled(final Marker marker) {
        return this.isEnabled(Level.DEBUG, marker, (Object)null, null);
    }
    
    @Override
    public boolean isEnabled(final Level level) {
        return this.isEnabled(level, null, (Object)null, null);
    }
    
    protected abstract boolean isEnabled(final Level p0, final Marker p1, final Message p2, final Throwable p3);
    
    protected abstract boolean isEnabled(final Level p0, final Marker p1, final Object p2, final Throwable p3);
    
    protected abstract boolean isEnabled(final Level p0, final Marker p1, final String p2);
    
    protected abstract boolean isEnabled(final Level p0, final Marker p1, final String p2, final Object... p3);
    
    protected abstract boolean isEnabled(final Level p0, final Marker p1, final String p2, final Throwable p3);
    
    @Override
    public boolean isErrorEnabled() {
        return this.isEnabled(Level.ERROR, null, (Object)null, null);
    }
    
    @Override
    public boolean isErrorEnabled(final Marker marker) {
        return this.isEnabled(Level.ERROR, marker, (Object)null, null);
    }
    
    @Override
    public boolean isFatalEnabled() {
        return this.isEnabled(Level.FATAL, null, (Object)null, null);
    }
    
    @Override
    public boolean isFatalEnabled(final Marker marker) {
        return this.isEnabled(Level.FATAL, marker, (Object)null, null);
    }
    
    @Override
    public boolean isInfoEnabled() {
        return this.isEnabled(Level.INFO, null, (Object)null, null);
    }
    
    @Override
    public boolean isInfoEnabled(final Marker marker) {
        return this.isEnabled(Level.INFO, marker, (Object)null, null);
    }
    
    @Override
    public boolean isTraceEnabled() {
        return this.isEnabled(Level.TRACE, null, (Object)null, null);
    }
    
    @Override
    public boolean isTraceEnabled(final Marker marker) {
        return this.isEnabled(Level.TRACE, marker, (Object)null, null);
    }
    
    @Override
    public boolean isWarnEnabled() {
        return this.isEnabled(Level.WARN, null, (Object)null, null);
    }
    
    @Override
    public boolean isWarnEnabled(final Marker marker) {
        return this.isEnabled(Level.WARN, marker, (Object)null, null);
    }
    
    @Override
    public boolean isEnabled(final Level level, final Marker marker) {
        return this.isEnabled(level, marker, (Object)null, null);
    }
    
    @Override
    public void log(final Level level, final Marker marker, final Message msg) {
        if (this.isEnabled(level, marker, msg, null)) {
            this.log(marker, AbstractLogger.FQCN, level, msg, null);
        }
    }
    
    @Override
    public void log(final Level level, final Marker marker, final Message msg, final Throwable t) {
        if (this.isEnabled(level, marker, msg, t)) {
            this.log(marker, AbstractLogger.FQCN, level, msg, t);
        }
    }
    
    @Override
    public void log(final Level level, final Marker marker, final Object message) {
        if (this.isEnabled(level, marker, message, null)) {
            this.log(marker, AbstractLogger.FQCN, level, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void log(final Level level, final Marker marker, final Object message, final Throwable t) {
        if (this.isEnabled(level, marker, message, t)) {
            this.log(marker, AbstractLogger.FQCN, level, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void log(final Level level, final Marker marker, final String message) {
        if (this.isEnabled(level, marker, message)) {
            this.log(marker, AbstractLogger.FQCN, level, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void log(final Level level, final Marker marker, final String message, final Object... params) {
        if (this.isEnabled(level, marker, message, params)) {
            final Message msg = this.messageFactory.newMessage(message, params);
            this.log(marker, AbstractLogger.FQCN, level, msg, msg.getThrowable());
        }
    }
    
    @Override
    public void log(final Level level, final Marker marker, final String message, final Throwable t) {
        if (this.isEnabled(level, marker, message, t)) {
            this.log(marker, AbstractLogger.FQCN, level, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void log(final Level level, final Message msg) {
        if (this.isEnabled(level, null, msg, null)) {
            this.log(null, AbstractLogger.FQCN, level, msg, null);
        }
    }
    
    @Override
    public void log(final Level level, final Message msg, final Throwable t) {
        if (this.isEnabled(level, null, msg, t)) {
            this.log(null, AbstractLogger.FQCN, level, msg, t);
        }
    }
    
    @Override
    public void log(final Level level, final Object message) {
        if (this.isEnabled(level, null, message, null)) {
            this.log(null, AbstractLogger.FQCN, level, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void log(final Level level, final Object message, final Throwable t) {
        if (this.isEnabled(level, null, message, t)) {
            this.log(null, AbstractLogger.FQCN, level, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void log(final Level level, final String message) {
        if (this.isEnabled(level, null, message)) {
            this.log(null, AbstractLogger.FQCN, level, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void log(final Level level, final String message, final Object... params) {
        if (this.isEnabled(level, null, message, params)) {
            final Message msg = this.messageFactory.newMessage(message, params);
            this.log(null, AbstractLogger.FQCN, level, msg, msg.getThrowable());
        }
    }
    
    @Override
    public void log(final Level level, final String message, final Throwable t) {
        if (this.isEnabled(level, null, message, t)) {
            this.log(null, AbstractLogger.FQCN, level, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void printf(final Level level, final String format, final Object... params) {
        if (this.isEnabled(level, null, format, params)) {
            final Message msg = new StringFormattedMessage(format, params);
            this.log(null, AbstractLogger.FQCN, level, msg, msg.getThrowable());
        }
    }
    
    @Override
    public void printf(final Level level, final Marker marker, final String format, final Object... params) {
        if (this.isEnabled(level, marker, format, params)) {
            final Message msg = new StringFormattedMessage(format, params);
            this.log(marker, AbstractLogger.FQCN, level, msg, msg.getThrowable());
        }
    }
    
    public abstract void log(final Marker p0, final String p1, final Level p2, final Message p3, final Throwable p4);
    
    @Override
    public <T extends Throwable> T throwing(final Level level, final T t) {
        return this.throwing(AbstractLogger.FQCN, level, t);
    }
    
    @Override
    public <T extends Throwable> T throwing(final T t) {
        return this.throwing(AbstractLogger.FQCN, Level.ERROR, t);
    }
    
    protected <T extends Throwable> T throwing(final String fqcn, final Level level, final T t) {
        if (this.isEnabled(level, AbstractLogger.THROWING_MARKER, (Object)null, null)) {
            this.log(AbstractLogger.THROWING_MARKER, fqcn, level, this.messageFactory.newMessage("throwing"), t);
        }
        return t;
    }
    
    private Message toExitMsg(final Object result) {
        if (result == null) {
            return this.messageFactory.newMessage("exit");
        }
        return this.messageFactory.newMessage("exit with(" + result + ")");
    }
    
    @Override
    public String toString() {
        return this.name;
    }
    
    @Override
    public void trace(final Marker marker, final Message msg) {
        if (this.isEnabled(Level.TRACE, marker, msg, null)) {
            this.log(marker, AbstractLogger.FQCN, Level.TRACE, msg, null);
        }
    }
    
    @Override
    public void trace(final Marker marker, final Message msg, final Throwable t) {
        if (this.isEnabled(Level.TRACE, marker, msg, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.TRACE, msg, t);
        }
    }
    
    @Override
    public void trace(final Marker marker, final Object message) {
        if (this.isEnabled(Level.TRACE, marker, message, null)) {
            this.log(marker, AbstractLogger.FQCN, Level.TRACE, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void trace(final Marker marker, final Object message, final Throwable t) {
        if (this.isEnabled(Level.TRACE, marker, message, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.TRACE, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void trace(final Marker marker, final String message) {
        if (this.isEnabled(Level.TRACE, marker, message)) {
            this.log(marker, AbstractLogger.FQCN, Level.TRACE, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void trace(final Marker marker, final String message, final Object... params) {
        if (this.isEnabled(Level.TRACE, marker, message, params)) {
            final Message msg = this.messageFactory.newMessage(message, params);
            this.log(marker, AbstractLogger.FQCN, Level.TRACE, msg, msg.getThrowable());
        }
    }
    
    @Override
    public void trace(final Marker marker, final String message, final Throwable t) {
        if (this.isEnabled(Level.TRACE, marker, message, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.TRACE, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void trace(final Message msg) {
        if (this.isEnabled(Level.TRACE, null, msg, null)) {
            this.log(null, AbstractLogger.FQCN, Level.TRACE, msg, null);
        }
    }
    
    @Override
    public void trace(final Message msg, final Throwable t) {
        if (this.isEnabled(Level.TRACE, null, msg, t)) {
            this.log(null, AbstractLogger.FQCN, Level.TRACE, msg, t);
        }
    }
    
    @Override
    public void trace(final Object message) {
        if (this.isEnabled(Level.TRACE, null, message, null)) {
            this.log(null, AbstractLogger.FQCN, Level.TRACE, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void trace(final Object message, final Throwable t) {
        if (this.isEnabled(Level.TRACE, null, message, t)) {
            this.log(null, AbstractLogger.FQCN, Level.TRACE, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void trace(final String message) {
        if (this.isEnabled(Level.TRACE, null, message)) {
            this.log(null, AbstractLogger.FQCN, Level.TRACE, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void trace(final String message, final Object... params) {
        if (this.isEnabled(Level.TRACE, null, message, params)) {
            final Message msg = this.messageFactory.newMessage(message, params);
            this.log(null, AbstractLogger.FQCN, Level.TRACE, msg, msg.getThrowable());
        }
    }
    
    @Override
    public void trace(final String message, final Throwable t) {
        if (this.isEnabled(Level.TRACE, null, message, t)) {
            this.log(null, AbstractLogger.FQCN, Level.TRACE, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void warn(final Marker marker, final Message msg) {
        if (this.isEnabled(Level.WARN, marker, msg, null)) {
            this.log(marker, AbstractLogger.FQCN, Level.WARN, msg, null);
        }
    }
    
    @Override
    public void warn(final Marker marker, final Message msg, final Throwable t) {
        if (this.isEnabled(Level.WARN, marker, msg, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.WARN, msg, t);
        }
    }
    
    @Override
    public void warn(final Marker marker, final Object message) {
        if (this.isEnabled(Level.WARN, marker, message, null)) {
            this.log(marker, AbstractLogger.FQCN, Level.WARN, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void warn(final Marker marker, final Object message, final Throwable t) {
        if (this.isEnabled(Level.WARN, marker, message, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.WARN, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void warn(final Marker marker, final String message) {
        if (this.isEnabled(Level.WARN, marker, message)) {
            this.log(marker, AbstractLogger.FQCN, Level.WARN, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void warn(final Marker marker, final String message, final Object... params) {
        if (this.isEnabled(Level.WARN, marker, message, params)) {
            final Message msg = this.messageFactory.newMessage(message, params);
            this.log(marker, AbstractLogger.FQCN, Level.WARN, msg, msg.getThrowable());
        }
    }
    
    @Override
    public void warn(final Marker marker, final String message, final Throwable t) {
        if (this.isEnabled(Level.WARN, marker, message, t)) {
            this.log(marker, AbstractLogger.FQCN, Level.WARN, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void warn(final Message msg) {
        if (this.isEnabled(Level.WARN, null, msg, null)) {
            this.log(null, AbstractLogger.FQCN, Level.WARN, msg, null);
        }
    }
    
    @Override
    public void warn(final Message msg, final Throwable t) {
        if (this.isEnabled(Level.WARN, null, msg, t)) {
            this.log(null, AbstractLogger.FQCN, Level.WARN, msg, t);
        }
    }
    
    @Override
    public void warn(final Object message) {
        if (this.isEnabled(Level.WARN, null, message, null)) {
            this.log(null, AbstractLogger.FQCN, Level.WARN, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void warn(final Object message, final Throwable t) {
        if (this.isEnabled(Level.WARN, null, message, t)) {
            this.log(null, AbstractLogger.FQCN, Level.WARN, this.messageFactory.newMessage(message), t);
        }
    }
    
    @Override
    public void warn(final String message) {
        if (this.isEnabled(Level.WARN, null, message)) {
            this.log(null, AbstractLogger.FQCN, Level.WARN, this.messageFactory.newMessage(message), null);
        }
    }
    
    @Override
    public void warn(final String message, final Object... params) {
        if (this.isEnabled(Level.WARN, null, message, params)) {
            final Message msg = this.messageFactory.newMessage(message, params);
            this.log(null, AbstractLogger.FQCN, Level.WARN, msg, msg.getThrowable());
        }
    }
    
    @Override
    public void warn(final String message, final Throwable t) {
        if (this.isEnabled(Level.WARN, null, message, t)) {
            this.log(null, AbstractLogger.FQCN, Level.WARN, this.messageFactory.newMessage(message), t);
        }
    }
    
    static {
        FLOW_MARKER = MarkerManager.getMarker("FLOW");
        ENTRY_MARKER = MarkerManager.getMarker("ENTRY", AbstractLogger.FLOW_MARKER);
        EXIT_MARKER = MarkerManager.getMarker("EXIT", AbstractLogger.FLOW_MARKER);
        EXCEPTION_MARKER = MarkerManager.getMarker("EXCEPTION");
        THROWING_MARKER = MarkerManager.getMarker("THROWING", AbstractLogger.EXCEPTION_MARKER);
        CATCHING_MARKER = MarkerManager.getMarker("CATCHING", AbstractLogger.EXCEPTION_MARKER);
        DEFAULT_MESSAGE_FACTORY_CLASS = ParameterizedMessageFactory.class;
        FQCN = AbstractLogger.class.getName();
    }
}
