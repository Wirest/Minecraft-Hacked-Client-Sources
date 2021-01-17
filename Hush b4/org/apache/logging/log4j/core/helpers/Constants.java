// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.helpers;

import org.apache.logging.log4j.util.PropertiesUtil;

public final class Constants
{
    public static final String LOG4J_LOG_EVENT_FACTORY = "Log4jLogEventFactory";
    public static final String LOG4J_CONTEXT_SELECTOR = "Log4jContextSelector";
    public static final String LOG4J_DEFAULT_STATUS_LEVEL = "Log4jDefaultStatusLevel";
    public static final String JNDI_CONTEXT_NAME = "java:comp/env/log4j/context-name";
    public static final String LINE_SEP;
    public static final int MILLIS_IN_SECONDS = 1000;
    
    private Constants() {
    }
    
    static {
        LINE_SEP = PropertiesUtil.getProperties().getStringProperty("line.separator", "\n");
    }
}
