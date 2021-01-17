// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import java.util.Iterator;
import java.util.List;
import java.io.File;

public class FileConfigurationMonitor implements ConfigurationMonitor
{
    private static final int MASK = 15;
    private static final int MIN_INTERVAL = 5;
    private static final int MILLIS_PER_SECOND = 1000;
    private final File file;
    private long lastModified;
    private final List<ConfigurationListener> listeners;
    private final int interval;
    private long nextCheck;
    private volatile int counter;
    private final Reconfigurable reconfigurable;
    
    public FileConfigurationMonitor(final Reconfigurable reconfigurable, final File file, final List<ConfigurationListener> listeners, final int interval) {
        this.counter = 0;
        this.reconfigurable = reconfigurable;
        this.file = file;
        this.lastModified = file.lastModified();
        this.listeners = listeners;
        this.interval = ((interval < 5) ? 5 : interval) * 1000;
        this.nextCheck = System.currentTimeMillis() + interval;
    }
    
    @Override
    public void checkConfiguration() {
        if ((++this.counter & 0xF) == 0x0) {
            synchronized (this) {
                final long current = System.currentTimeMillis();
                if (current >= this.nextCheck) {
                    this.nextCheck = current + this.interval;
                    if (this.file.lastModified() > this.lastModified) {
                        this.lastModified = this.file.lastModified();
                        for (final ConfigurationListener listener : this.listeners) {
                            listener.onChange(this.reconfigurable);
                        }
                    }
                }
            }
        }
    }
}
