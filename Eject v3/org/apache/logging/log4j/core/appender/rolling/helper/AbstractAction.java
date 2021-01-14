package org.apache.logging.log4j.core.appender.rolling.helper;

import org.apache.logging.log4j.Logger;

import java.io.IOException;

public abstract class AbstractAction
        implements Action {
    protected static final Logger LOGGER = ;
    private boolean complete = false;
    private boolean interrupted = false;

    public abstract boolean execute()
            throws IOException;

    public synchronized void run() {
        if (!this.interrupted) {
            try {
                execute();
            } catch (IOException localIOException) {
                reportException(localIOException);
            }
            this.complete = true;
            this.interrupted = true;
        }
    }

    public synchronized void close() {
        this.interrupted = true;
    }

    public boolean isComplete() {
        return this.complete;
    }

    protected void reportException(Exception paramException) {
    }
}




