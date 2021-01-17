// 
// Decompiled by Procyon v0.5.36
// 

package io.netty.util.internal.logging;

import io.netty.util.internal.StringUtil;
import java.io.ObjectStreamException;
import java.io.Serializable;

public abstract class AbstractInternalLogger implements InternalLogger, Serializable
{
    private static final long serialVersionUID = -6382972526573193470L;
    private final String name;
    
    protected AbstractInternalLogger(final String name) {
        if (name == null) {
            throw new NullPointerException("name");
        }
        this.name = name;
    }
    
    @Override
    public String name() {
        return this.name;
    }
    
    @Override
    public boolean isEnabled(final InternalLogLevel level) {
        switch (level) {
            case TRACE: {
                return this.isTraceEnabled();
            }
            case DEBUG: {
                return this.isDebugEnabled();
            }
            case INFO: {
                return this.isInfoEnabled();
            }
            case WARN: {
                return this.isWarnEnabled();
            }
            case ERROR: {
                return this.isErrorEnabled();
            }
            default: {
                throw new Error();
            }
        }
    }
    
    @Override
    public void log(final InternalLogLevel level, final String msg, final Throwable cause) {
        switch (level) {
            case TRACE: {
                this.trace(msg, cause);
                break;
            }
            case DEBUG: {
                this.debug(msg, cause);
                break;
            }
            case INFO: {
                this.info(msg, cause);
                break;
            }
            case WARN: {
                this.warn(msg, cause);
                break;
            }
            case ERROR: {
                this.error(msg, cause);
                break;
            }
            default: {
                throw new Error();
            }
        }
    }
    
    @Override
    public void log(final InternalLogLevel level, final String msg) {
        switch (level) {
            case TRACE: {
                this.trace(msg);
                break;
            }
            case DEBUG: {
                this.debug(msg);
                break;
            }
            case INFO: {
                this.info(msg);
                break;
            }
            case WARN: {
                this.warn(msg);
                break;
            }
            case ERROR: {
                this.error(msg);
                break;
            }
            default: {
                throw new Error();
            }
        }
    }
    
    @Override
    public void log(final InternalLogLevel level, final String format, final Object arg) {
        switch (level) {
            case TRACE: {
                this.trace(format, arg);
                break;
            }
            case DEBUG: {
                this.debug(format, arg);
                break;
            }
            case INFO: {
                this.info(format, arg);
                break;
            }
            case WARN: {
                this.warn(format, arg);
                break;
            }
            case ERROR: {
                this.error(format, arg);
                break;
            }
            default: {
                throw new Error();
            }
        }
    }
    
    @Override
    public void log(final InternalLogLevel level, final String format, final Object argA, final Object argB) {
        switch (level) {
            case TRACE: {
                this.trace(format, argA, argB);
                break;
            }
            case DEBUG: {
                this.debug(format, argA, argB);
                break;
            }
            case INFO: {
                this.info(format, argA, argB);
                break;
            }
            case WARN: {
                this.warn(format, argA, argB);
                break;
            }
            case ERROR: {
                this.error(format, argA, argB);
                break;
            }
            default: {
                throw new Error();
            }
        }
    }
    
    @Override
    public void log(final InternalLogLevel level, final String format, final Object... arguments) {
        switch (level) {
            case TRACE: {
                this.trace(format, arguments);
                break;
            }
            case DEBUG: {
                this.debug(format, arguments);
                break;
            }
            case INFO: {
                this.info(format, arguments);
                break;
            }
            case WARN: {
                this.warn(format, arguments);
                break;
            }
            case ERROR: {
                this.error(format, arguments);
                break;
            }
            default: {
                throw new Error();
            }
        }
    }
    
    protected Object readResolve() throws ObjectStreamException {
        return InternalLoggerFactory.getInstance(this.name());
    }
    
    @Override
    public String toString() {
        return StringUtil.simpleClassName(this) + '(' + this.name() + ')';
    }
}
