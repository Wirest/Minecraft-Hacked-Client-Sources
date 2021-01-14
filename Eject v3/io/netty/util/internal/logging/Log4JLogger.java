package io.netty.util.internal.logging;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

class Log4JLogger
        extends AbstractInternalLogger {
    static final String FQCN = Log4JLogger.class.getName();
    private static final long serialVersionUID = 2851357342488183058L;
    final transient Logger logger;
    final boolean traceCapable;

    Log4JLogger(Logger paramLogger) {
        super(paramLogger.getName());
        this.logger = paramLogger;
        this.traceCapable = isTraceCapable();
    }

    private boolean isTraceCapable() {
        try {
            this.logger.isTraceEnabled();
            return true;
        } catch (NoSuchMethodError localNoSuchMethodError) {
        }
        return false;
    }

    public boolean isTraceEnabled() {
        if (this.traceCapable) {
            return this.logger.isTraceEnabled();
        }
        return this.logger.isDebugEnabled();
    }

    public void trace(String paramString) {
        this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, paramString, null);
    }

    public void trace(String paramString, Object paramObject) {
        if (isTraceEnabled()) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject);
            this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void trace(String paramString, Object paramObject1, Object paramObject2) {
        if (isTraceEnabled()) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject1, paramObject2);
            this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void trace(String paramString, Object... paramVarArgs) {
        if (isTraceEnabled()) {
            FormattingTuple localFormattingTuple = MessageFormatter.arrayFormat(paramString, paramVarArgs);
            this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void trace(String paramString, Throwable paramThrowable) {
        this.logger.log(FQCN, this.traceCapable ? Level.TRACE : Level.DEBUG, paramString, paramThrowable);
    }

    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    public void debug(String paramString) {
        this.logger.log(FQCN, Level.DEBUG, paramString, null);
    }

    public void debug(String paramString, Object paramObject) {
        if (this.logger.isDebugEnabled()) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject);
            this.logger.log(FQCN, Level.DEBUG, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void debug(String paramString, Object paramObject1, Object paramObject2) {
        if (this.logger.isDebugEnabled()) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject1, paramObject2);
            this.logger.log(FQCN, Level.DEBUG, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void debug(String paramString, Object... paramVarArgs) {
        if (this.logger.isDebugEnabled()) {
            FormattingTuple localFormattingTuple = MessageFormatter.arrayFormat(paramString, paramVarArgs);
            this.logger.log(FQCN, Level.DEBUG, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void debug(String paramString, Throwable paramThrowable) {
        this.logger.log(FQCN, Level.DEBUG, paramString, paramThrowable);
    }

    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    public void info(String paramString) {
        this.logger.log(FQCN, Level.INFO, paramString, null);
    }

    public void info(String paramString, Object paramObject) {
        if (this.logger.isInfoEnabled()) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject);
            this.logger.log(FQCN, Level.INFO, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void info(String paramString, Object paramObject1, Object paramObject2) {
        if (this.logger.isInfoEnabled()) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject1, paramObject2);
            this.logger.log(FQCN, Level.INFO, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void info(String paramString, Object... paramVarArgs) {
        if (this.logger.isInfoEnabled()) {
            FormattingTuple localFormattingTuple = MessageFormatter.arrayFormat(paramString, paramVarArgs);
            this.logger.log(FQCN, Level.INFO, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void info(String paramString, Throwable paramThrowable) {
        this.logger.log(FQCN, Level.INFO, paramString, paramThrowable);
    }

    public boolean isWarnEnabled() {
        return this.logger.isEnabledFor(Level.WARN);
    }

    public void warn(String paramString) {
        this.logger.log(FQCN, Level.WARN, paramString, null);
    }

    public void warn(String paramString, Object paramObject) {
        if (this.logger.isEnabledFor(Level.WARN)) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject);
            this.logger.log(FQCN, Level.WARN, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void warn(String paramString, Object paramObject1, Object paramObject2) {
        if (this.logger.isEnabledFor(Level.WARN)) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject1, paramObject2);
            this.logger.log(FQCN, Level.WARN, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void warn(String paramString, Object... paramVarArgs) {
        if (this.logger.isEnabledFor(Level.WARN)) {
            FormattingTuple localFormattingTuple = MessageFormatter.arrayFormat(paramString, paramVarArgs);
            this.logger.log(FQCN, Level.WARN, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void warn(String paramString, Throwable paramThrowable) {
        this.logger.log(FQCN, Level.WARN, paramString, paramThrowable);
    }

    public boolean isErrorEnabled() {
        return this.logger.isEnabledFor(Level.ERROR);
    }

    public void error(String paramString) {
        this.logger.log(FQCN, Level.ERROR, paramString, null);
    }

    public void error(String paramString, Object paramObject) {
        if (this.logger.isEnabledFor(Level.ERROR)) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject);
            this.logger.log(FQCN, Level.ERROR, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void error(String paramString, Object paramObject1, Object paramObject2) {
        if (this.logger.isEnabledFor(Level.ERROR)) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject1, paramObject2);
            this.logger.log(FQCN, Level.ERROR, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void error(String paramString, Object... paramVarArgs) {
        if (this.logger.isEnabledFor(Level.ERROR)) {
            FormattingTuple localFormattingTuple = MessageFormatter.arrayFormat(paramString, paramVarArgs);
            this.logger.log(FQCN, Level.ERROR, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void error(String paramString, Throwable paramThrowable) {
        this.logger.log(FQCN, Level.ERROR, paramString, paramThrowable);
    }
}




