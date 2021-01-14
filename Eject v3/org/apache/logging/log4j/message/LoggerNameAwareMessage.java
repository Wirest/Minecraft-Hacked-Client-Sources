package org.apache.logging.log4j.message;

public abstract interface LoggerNameAwareMessage {
    public abstract String getLoggerName();

    public abstract void setLoggerName(String paramString);
}




