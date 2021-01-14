package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.appender.rolling.helper.Action;

public abstract interface RolloverDescription {
    public abstract String getActiveFileName();

    public abstract boolean getAppend();

    public abstract Action getSynchronous();

    public abstract Action getAsynchronous();
}




