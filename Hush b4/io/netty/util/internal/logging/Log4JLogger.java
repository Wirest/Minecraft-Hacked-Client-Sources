// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal.logging;

import org.apache.log4j.Priority;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

class Log4JLogger extends AbstractInternalLogger
{
    private static final long serialVersionUID = 2851357342488183058L;
    final transient Logger logger;
    static final String FQCN;
    final boolean traceCapable;
    
    Log4JLogger(final Logger logger) {
        super(logger.getName());
        this.logger = logger;
        this.traceCapable = this.isTraceCapable();
    }
    
    private boolean isTraceCapable() {
        try {
            this.logger.isTraceEnabled();
            return true;
        }
        catch (NoSuchMethodError ignored) {
            return false;
        }
    }
    
    @Override
    public boolean isTraceEnabled() {
        if (this.traceCapable) {
            return this.logger.isTraceEnabled();
        }
        return this.logger.isDebugEnabled();
    }
    
    @Override
    public void trace(final String msg) {
        this.logger.log(Log4JLogger.FQCN, (Priority)(this.traceCapable ? Level.TRACE : Level.DEBUG), (Object)msg, (Throwable)null);
    }
    
    @Override
    public void trace(final String format, final Object arg) {
        if (this.isTraceEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.log(Log4JLogger.FQCN, (Priority)(this.traceCapable ? Level.TRACE : Level.DEBUG), (Object)ft.getMessage(), ft.getThrowable());
        }
    }
    
    @Override
    public void trace(final String format, final Object argA, final Object argB) {
        if (this.isTraceEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.log(Log4JLogger.FQCN, (Priority)(this.traceCapable ? Level.TRACE : Level.DEBUG), (Object)ft.getMessage(), ft.getThrowable());
        }
    }
    
    @Override
    public void trace(final String format, final Object... arguments) {
        if (this.isTraceEnabled()) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            this.logger.log(Log4JLogger.FQCN, (Priority)(this.traceCapable ? Level.TRACE : Level.DEBUG), (Object)ft.getMessage(), ft.getThrowable());
        }
    }
    
    @Override
    public void trace(final String msg, final Throwable t) {
        this.logger.log(Log4JLogger.FQCN, (Priority)(this.traceCapable ? Level.TRACE : Level.DEBUG), (Object)msg, t);
    }
    
    @Override
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }
    
    @Override
    public void debug(final String msg) {
        this.logger.log(Log4JLogger.FQCN, (Priority)Level.DEBUG, (Object)msg, (Throwable)null);
    }
    
    @Override
    public void debug(final String format, final Object arg) {
        if (this.logger.isDebugEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.DEBUG, (Object)ft.getMessage(), ft.getThrowable());
        }
    }
    
    @Override
    public void debug(final String format, final Object argA, final Object argB) {
        if (this.logger.isDebugEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.DEBUG, (Object)ft.getMessage(), ft.getThrowable());
        }
    }
    
    @Override
    public void debug(final String format, final Object... arguments) {
        if (this.logger.isDebugEnabled()) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, arguments);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.DEBUG, (Object)ft.getMessage(), ft.getThrowable());
        }
    }
    
    @Override
    public void debug(final String msg, final Throwable t) {
        this.logger.log(Log4JLogger.FQCN, (Priority)Level.DEBUG, (Object)msg, t);
    }
    
    @Override
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }
    
    @Override
    public void info(final String msg) {
        this.logger.log(Log4JLogger.FQCN, (Priority)Level.INFO, (Object)msg, (Throwable)null);
    }
    
    @Override
    public void info(final String format, final Object arg) {
        if (this.logger.isInfoEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.INFO, (Object)ft.getMessage(), ft.getThrowable());
        }
    }
    
    @Override
    public void info(final String format, final Object argA, final Object argB) {
        if (this.logger.isInfoEnabled()) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.INFO, (Object)ft.getMessage(), ft.getThrowable());
        }
    }
    
    @Override
    public void info(final String format, final Object... argArray) {
        if (this.logger.isInfoEnabled()) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.INFO, (Object)ft.getMessage(), ft.getThrowable());
        }
    }
    
    @Override
    public void info(final String msg, final Throwable t) {
        this.logger.log(Log4JLogger.FQCN, (Priority)Level.INFO, (Object)msg, t);
    }
    
    @Override
    public boolean isWarnEnabled() {
        return this.logger.isEnabledFor((Priority)Level.WARN);
    }
    
    @Override
    public void warn(final String msg) {
        this.logger.log(Log4JLogger.FQCN, (Priority)Level.WARN, (Object)msg, (Throwable)null);
    }
    
    @Override
    public void warn(final String format, final Object arg) {
        if (this.logger.isEnabledFor((Priority)Level.WARN)) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.WARN, (Object)ft.getMessage(), ft.getThrowable());
        }
    }
    
    @Override
    public void warn(final String format, final Object argA, final Object argB) {
        if (this.logger.isEnabledFor((Priority)Level.WARN)) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.WARN, (Object)ft.getMessage(), ft.getThrowable());
        }
    }
    
    @Override
    public void warn(final String format, final Object... argArray) {
        if (this.logger.isEnabledFor((Priority)Level.WARN)) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.WARN, (Object)ft.getMessage(), ft.getThrowable());
        }
    }
    
    @Override
    public void warn(final String msg, final Throwable t) {
        this.logger.log(Log4JLogger.FQCN, (Priority)Level.WARN, (Object)msg, t);
    }
    
    @Override
    public boolean isErrorEnabled() {
        return this.logger.isEnabledFor((Priority)Level.ERROR);
    }
    
    @Override
    public void error(final String msg) {
        this.logger.log(Log4JLogger.FQCN, (Priority)Level.ERROR, (Object)msg, (Throwable)null);
    }
    
    @Override
    public void error(final String format, final Object arg) {
        if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
            final FormattingTuple ft = MessageFormatter.format(format, arg);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.ERROR, (Object)ft.getMessage(), ft.getThrowable());
        }
    }
    
    @Override
    public void error(final String format, final Object argA, final Object argB) {
        if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
            final FormattingTuple ft = MessageFormatter.format(format, argA, argB);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.ERROR, (Object)ft.getMessage(), ft.getThrowable());
        }
    }
    
    @Override
    public void error(final String format, final Object... argArray) {
        if (this.logger.isEnabledFor((Priority)Level.ERROR)) {
            final FormattingTuple ft = MessageFormatter.arrayFormat(format, argArray);
            this.logger.log(Log4JLogger.FQCN, (Priority)Level.ERROR, (Object)ft.getMessage(), ft.getThrowable());
        }
    }
    
    @Override
    public void error(final String msg, final Throwable t) {
        this.logger.log(Log4JLogger.FQCN, (Priority)Level.ERROR, (Object)msg, t);
    }
    
    static {
        FQCN = Log4JLogger.class.getName();
    }
}
