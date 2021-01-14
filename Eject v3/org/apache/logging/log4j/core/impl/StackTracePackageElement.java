package org.apache.logging.log4j.core.impl;

import java.io.Serializable;

public class StackTracePackageElement
        implements Serializable {
    private static final long serialVersionUID = -2171069569241280505L;
    private final String location;
    private final String version;
    private final boolean isExact;

    public StackTracePackageElement(String paramString1, String paramString2, boolean paramBoolean) {
        this.location = paramString1;
        this.version = paramString2;
        this.isExact = paramBoolean;
    }

    public String getLocation() {
        return this.location;
    }

    public String getVersion() {
        return this.version;
    }

    public boolean isExact() {
        return this.isExact;
    }

    public String toString() {
        String str = this.isExact ? "" : "~";
        return str + "[" + this.location + ":" + this.version + "]";
    }
}




