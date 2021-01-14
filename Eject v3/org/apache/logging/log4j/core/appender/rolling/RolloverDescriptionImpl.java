package org.apache.logging.log4j.core.appender.rolling;

import org.apache.logging.log4j.core.appender.rolling.helper.Action;

public final class RolloverDescriptionImpl
        implements RolloverDescription {
    private final String activeFileName;
    private final boolean append;
    private final Action synchronous;
    private final Action asynchronous;

    public RolloverDescriptionImpl(String paramString, boolean paramBoolean, Action paramAction1, Action paramAction2) {
        if (paramString == null) {
            throw new NullPointerException("activeFileName");
        }
        this.append = paramBoolean;
        this.activeFileName = paramString;
        this.synchronous = paramAction1;
        this.asynchronous = paramAction2;
    }

    public String getActiveFileName() {
        return this.activeFileName;
    }

    public boolean getAppend() {
        return this.append;
    }

    public Action getSynchronous() {
        return this.synchronous;
    }

    public Action getAsynchronous() {
        return this.asynchronous;
    }
}




