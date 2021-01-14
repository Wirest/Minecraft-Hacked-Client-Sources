package io.netty.util.internal.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

class JdkLogger
        extends AbstractInternalLogger {
    static final String SELF = JdkLogger.class.getName();
    static final String SUPER = AbstractInternalLogger.class.getName();
    private static final long serialVersionUID = -1767272577989225979L;
    final transient Logger logger;

    JdkLogger(Logger paramLogger) {
        super(paramLogger.getName());
        this.logger = paramLogger;
    }

    private static void fillCallerData(String paramString, LogRecord paramLogRecord) {
        StackTraceElement[] arrayOfStackTraceElement = new Throwable().getStackTrace();
        int i = -1;
        for (int j = 0; j < arrayOfStackTraceElement.length; j++) {
            String str1 = arrayOfStackTraceElement[j].getClassName();
            if ((str1.equals(paramString)) || (str1.equals(SUPER))) {
                i = j;
                break;
            }
        }
        j = -1;
        for (int k = i | 0x1; k < arrayOfStackTraceElement.length; k++) {
            String str2 = arrayOfStackTraceElement[k].getClassName();
            if ((!str2.equals(paramString)) && (!str2.equals(SUPER))) {
                j = k;
                break;
            }
        }
        if (j != -1) {
            StackTraceElement localStackTraceElement = arrayOfStackTraceElement[j];
            paramLogRecord.setSourceClassName(localStackTraceElement.getClassName());
            paramLogRecord.setSourceMethodName(localStackTraceElement.getMethodName());
        }
    }

    public boolean isTraceEnabled() {
        return this.logger.isLoggable(Level.FINEST);
    }

    public void trace(String paramString) {
        if (this.logger.isLoggable(Level.FINEST)) {
            log(SELF, Level.FINEST, paramString, null);
        }
    }

    public void trace(String paramString, Object paramObject) {
        if (this.logger.isLoggable(Level.FINEST)) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject);
            log(SELF, Level.FINEST, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void trace(String paramString, Object paramObject1, Object paramObject2) {
        if (this.logger.isLoggable(Level.FINEST)) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject1, paramObject2);
            log(SELF, Level.FINEST, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void trace(String paramString, Object... paramVarArgs) {
        if (this.logger.isLoggable(Level.FINEST)) {
            FormattingTuple localFormattingTuple = MessageFormatter.arrayFormat(paramString, paramVarArgs);
            log(SELF, Level.FINEST, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void trace(String paramString, Throwable paramThrowable) {
        if (this.logger.isLoggable(Level.FINEST)) {
            log(SELF, Level.FINEST, paramString, paramThrowable);
        }
    }

    public boolean isDebugEnabled() {
        return this.logger.isLoggable(Level.FINE);
    }

    public void debug(String paramString) {
        if (this.logger.isLoggable(Level.FINE)) {
            log(SELF, Level.FINE, paramString, null);
        }
    }

    public void debug(String paramString, Object paramObject) {
        if (this.logger.isLoggable(Level.FINE)) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject);
            log(SELF, Level.FINE, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void debug(String paramString, Object paramObject1, Object paramObject2) {
        if (this.logger.isLoggable(Level.FINE)) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject1, paramObject2);
            log(SELF, Level.FINE, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void debug(String paramString, Object... paramVarArgs) {
        if (this.logger.isLoggable(Level.FINE)) {
            FormattingTuple localFormattingTuple = MessageFormatter.arrayFormat(paramString, paramVarArgs);
            log(SELF, Level.FINE, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void debug(String paramString, Throwable paramThrowable) {
        if (this.logger.isLoggable(Level.FINE)) {
            log(SELF, Level.FINE, paramString, paramThrowable);
        }
    }

    public boolean isInfoEnabled() {
        return this.logger.isLoggable(Level.INFO);
    }

    public void info(String paramString) {
        if (this.logger.isLoggable(Level.INFO)) {
            log(SELF, Level.INFO, paramString, null);
        }
    }

    public void info(String paramString, Object paramObject) {
        if (this.logger.isLoggable(Level.INFO)) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject);
            log(SELF, Level.INFO, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void info(String paramString, Object paramObject1, Object paramObject2) {
        if (this.logger.isLoggable(Level.INFO)) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject1, paramObject2);
            log(SELF, Level.INFO, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void info(String paramString, Object... paramVarArgs) {
        if (this.logger.isLoggable(Level.INFO)) {
            FormattingTuple localFormattingTuple = MessageFormatter.arrayFormat(paramString, paramVarArgs);
            log(SELF, Level.INFO, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void info(String paramString, Throwable paramThrowable) {
        if (this.logger.isLoggable(Level.INFO)) {
            log(SELF, Level.INFO, paramString, paramThrowable);
        }
    }

    public boolean isWarnEnabled() {
        return this.logger.isLoggable(Level.WARNING);
    }

    public void warn(String paramString) {
        if (this.logger.isLoggable(Level.WARNING)) {
            log(SELF, Level.WARNING, paramString, null);
        }
    }

    public void warn(String paramString, Object paramObject) {
        if (this.logger.isLoggable(Level.WARNING)) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject);
            log(SELF, Level.WARNING, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void warn(String paramString, Object paramObject1, Object paramObject2) {
        if (this.logger.isLoggable(Level.WARNING)) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject1, paramObject2);
            log(SELF, Level.WARNING, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void warn(String paramString, Object... paramVarArgs) {
        if (this.logger.isLoggable(Level.WARNING)) {
            FormattingTuple localFormattingTuple = MessageFormatter.arrayFormat(paramString, paramVarArgs);
            log(SELF, Level.WARNING, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void warn(String paramString, Throwable paramThrowable) {
        if (this.logger.isLoggable(Level.WARNING)) {
            log(SELF, Level.WARNING, paramString, paramThrowable);
        }
    }

    public boolean isErrorEnabled() {
        return this.logger.isLoggable(Level.SEVERE);
    }

    public void error(String paramString) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            log(SELF, Level.SEVERE, paramString, null);
        }
    }

    public void error(String paramString, Object paramObject) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject);
            log(SELF, Level.SEVERE, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void error(String paramString, Object paramObject1, Object paramObject2) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            FormattingTuple localFormattingTuple = MessageFormatter.format(paramString, paramObject1, paramObject2);
            log(SELF, Level.SEVERE, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void error(String paramString, Object... paramVarArgs) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            FormattingTuple localFormattingTuple = MessageFormatter.arrayFormat(paramString, paramVarArgs);
            log(SELF, Level.SEVERE, localFormattingTuple.getMessage(), localFormattingTuple.getThrowable());
        }
    }

    public void error(String paramString, Throwable paramThrowable) {
        if (this.logger.isLoggable(Level.SEVERE)) {
            log(SELF, Level.SEVERE, paramString, paramThrowable);
        }
    }

    private void log(String paramString1, Level paramLevel, String paramString2, Throwable paramThrowable) {
        LogRecord localLogRecord = new LogRecord(paramLevel, paramString2);
        localLogRecord.setLoggerName(name());
        localLogRecord.setThrown(paramThrowable);
        fillCallerData(paramString1, localLogRecord);
        this.logger.log(localLogRecord);
    }
}




