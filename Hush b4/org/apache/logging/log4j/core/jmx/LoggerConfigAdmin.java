// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import java.util.List;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.helpers.Assert;
import javax.management.ObjectName;
import org.apache.logging.log4j.core.config.LoggerConfig;

public class LoggerConfigAdmin implements LoggerConfigAdminMBean
{
    private final String contextName;
    private final LoggerConfig loggerConfig;
    private final ObjectName objectName;
    
    public LoggerConfigAdmin(final String contextName, final LoggerConfig loggerConfig) {
        this.contextName = Assert.isNotNull(contextName, "contextName");
        this.loggerConfig = Assert.isNotNull(loggerConfig, "loggerConfig");
        try {
            final String ctxName = Server.escape(this.contextName);
            final String configName = Server.escape(loggerConfig.getName());
            final String name = String.format("org.apache.logging.log4j2:type=LoggerContext,ctx=%s,sub=LoggerConfig,name=%s", ctxName, configName);
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
        return this.loggerConfig.getName();
    }
    
    @Override
    public String getLevel() {
        return this.loggerConfig.getLevel().name();
    }
    
    @Override
    public void setLevel(final String level) {
        this.loggerConfig.setLevel(Level.valueOf(level));
    }
    
    @Override
    public boolean isAdditive() {
        return this.loggerConfig.isAdditive();
    }
    
    @Override
    public void setAdditive(final boolean additive) {
        this.loggerConfig.setAdditive(additive);
    }
    
    @Override
    public boolean isIncludeLocation() {
        return this.loggerConfig.isIncludeLocation();
    }
    
    @Override
    public String getFilter() {
        return String.valueOf(this.loggerConfig.getFilter());
    }
    
    @Override
    public String[] getAppenderRefs() {
        final List<AppenderRef> refs = this.loggerConfig.getAppenderRefs();
        final String[] result = new String[refs.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = refs.get(i).getRef();
        }
        return result;
    }
}
