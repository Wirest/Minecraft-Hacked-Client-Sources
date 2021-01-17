// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import org.apache.logging.log4j.status.StatusData;
import java.util.List;

public interface StatusLoggerAdminMBean
{
    public static final String NAME = "org.apache.logging.log4j2:type=StatusLogger";
    public static final String NOTIF_TYPE_DATA = "com.apache.logging.log4j.core.jmx.statuslogger.data";
    public static final String NOTIF_TYPE_MESSAGE = "com.apache.logging.log4j.core.jmx.statuslogger.message";
    
    List<StatusData> getStatusData();
    
    String[] getStatusDataHistory();
    
    String getLevel();
    
    void setLevel(final String p0);
}
