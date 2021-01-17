// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

public interface AppenderAdminMBean
{
    public static final String PATTERN = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=Appender,name=%s";
    
    String getName();
    
    String getLayout();
    
    boolean isExceptionSuppressed();
    
    String getErrorHandler();
}
