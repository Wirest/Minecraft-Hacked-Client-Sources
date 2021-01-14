package org.apache.logging.log4j.core.appender.rolling.helper;

import java.io.IOException;

public abstract interface Action
        extends Runnable {
    public abstract boolean execute()
            throws IOException;

    public abstract void close();

    public abstract boolean isComplete();
}




