// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.jmx;

import java.util.List;
import org.apache.logging.log4j.status.StatusData;
import javax.management.Notification;
import org.apache.logging.log4j.status.StatusLogger;
import javax.management.MBeanNotificationInfo;
import java.util.concurrent.Executor;
import org.apache.logging.log4j.Level;
import javax.management.ObjectName;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.logging.log4j.status.StatusListener;
import javax.management.NotificationBroadcasterSupport;

public class StatusLoggerAdmin extends NotificationBroadcasterSupport implements StatusListener, StatusLoggerAdminMBean
{
    private final AtomicLong sequenceNo;
    private final ObjectName objectName;
    private Level level;
    
    public StatusLoggerAdmin(final Executor executor) {
        super(executor, new MBeanNotificationInfo[] { createNotificationInfo() });
        this.sequenceNo = new AtomicLong();
        this.level = Level.WARN;
        try {
            this.objectName = new ObjectName("org.apache.logging.log4j2:type=StatusLogger");
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
        StatusLogger.getLogger().registerListener(this);
    }
    
    private static MBeanNotificationInfo createNotificationInfo() {
        final String[] notifTypes = { "com.apache.logging.log4j.core.jmx.statuslogger.data", "com.apache.logging.log4j.core.jmx.statuslogger.message" };
        final String name = Notification.class.getName();
        final String description = "StatusLogger has logged an event";
        return new MBeanNotificationInfo(notifTypes, name, "StatusLogger has logged an event");
    }
    
    @Override
    public String[] getStatusDataHistory() {
        final List<StatusData> data = this.getStatusData();
        final String[] result = new String[data.size()];
        for (int i = 0; i < result.length; ++i) {
            result[i] = data.get(i).getFormattedStatus();
        }
        return result;
    }
    
    @Override
    public List<StatusData> getStatusData() {
        return StatusLogger.getLogger().getStatusData();
    }
    
    @Override
    public String getLevel() {
        return this.level.name();
    }
    
    @Override
    public Level getStatusLevel() {
        return this.level;
    }
    
    @Override
    public void setLevel(final String level) {
        this.level = Level.toLevel(level, Level.ERROR);
    }
    
    @Override
    public void log(final StatusData data) {
        final Notification notifMsg = new Notification("com.apache.logging.log4j.core.jmx.statuslogger.message", this.getObjectName(), this.nextSeqNo(), this.now(), data.getFormattedStatus());
        this.sendNotification(notifMsg);
        final Notification notifData = new Notification("com.apache.logging.log4j.core.jmx.statuslogger.data", this.getObjectName(), this.nextSeqNo(), this.now());
        notifData.setUserData(data);
        this.sendNotification(notifData);
    }
    
    public ObjectName getObjectName() {
        return this.objectName;
    }
    
    private long nextSeqNo() {
        return this.sequenceNo.getAndIncrement();
    }
    
    private long now() {
        return System.currentTimeMillis();
    }
}
