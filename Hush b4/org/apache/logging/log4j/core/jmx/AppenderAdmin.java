// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import org.apache.logging.log4j.core.helpers.Assert;
import javax.management.ObjectName;
import org.apache.logging.log4j.core.Appender;

public class AppenderAdmin implements AppenderAdminMBean
{
    private final String contextName;
    private final Appender appender;
    private final ObjectName objectName;
    
    public AppenderAdmin(final String contextName, final Appender appender) {
        this.contextName = Assert.isNotNull(contextName, "contextName");
        this.appender = Assert.isNotNull(appender, "appender");
        try {
            final String ctxName = Server.escape(this.contextName);
            final String configName = Server.escape(appender.getName());
            final String name = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=Appender,name=%s", ctxName, configName);
            this.objectName = new ObjectName(name);
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public ObjectName getObjectName() {
        return this.objectName;
    }
    
    @Override
    public String getName() {
        return this.appender.getName();
    }
    
    @Override
    public String getLayout() {
        return String.valueOf(this.appender.getLayout());
    }
    
    @Override
    public boolean isExceptionSuppressed() {
        return this.appender.ignoreExceptions();
    }
    
    @Override
    public String getErrorHandler() {
        return String.valueOf(this.appender.getHandler());
    }
}
