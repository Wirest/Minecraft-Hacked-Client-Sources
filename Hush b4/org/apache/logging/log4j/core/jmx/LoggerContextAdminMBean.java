// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import java.util.Map;
import java.io.IOException;
import java.net.URISyntaxException;

public interface LoggerContextAdminMBean
{
    public static final String PATTERN = "org.apache.logging.log4j2:type=LoggerContext,ctx=%s";
    public static final String NOTIF_TYPE_RECONFIGURED = "com.apache.logging.log4j.core.jmx.config.reconfigured";
    
    String getStatus();
    
    String getName();
    
    String getConfigLocationURI();
    
    void setConfigLocationURI(final String p0) throws URISyntaxException, IOException;
    
    String getConfigText() throws IOException;
    
    String getConfigText(final String p0) throws IOException;
    
    void setConfigText(final String p0, final String p1);
    
    String getConfigName();
    
    String getConfigClassName();
    
    String getConfigFilter();
    
    String getConfigMonitorClassName();
    
    Map<String, String> getConfigProperties();
}
